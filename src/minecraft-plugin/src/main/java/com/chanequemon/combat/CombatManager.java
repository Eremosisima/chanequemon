package com.chanequemon.combat;

import com.chanequemon.capture.CaptureManager;
import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CombatManager {
    private final Map<UUID, CombatSession> activeBattles = new ConcurrentHashMap<>();
    private final CombatGUI gui;
    private final CaptureManager captureManager;
    private final ChanequeCurseManager curseManager;
    private final org.bukkit.plugin.java.JavaPlugin plugin;

    public CombatManager(CaptureManager captureManager, ChanequeCurseManager curseManager,
                         org.bukkit.plugin.java.JavaPlugin plugin) {
        this.gui = new CombatGUI(this, captureManager);
        this.captureManager = captureManager;
        this.curseManager = curseManager;
        this.plugin = plugin;
    }

    public CombatGUI gui() { return gui; }
    public CaptureManager captureManager() { return captureManager; }
    public ChanequeCurseManager curseManager() { return curseManager; }

    public void startBattle(Player player, CreatureInstance playerCreature,
                            List<CreatureInstance> party, CreatureInstance wildCreature) {
        CombatSession session = new CombatSession(
            player.getUniqueId(), playerCreature, party, wildCreature
        );
        activeBattles.put(player.getUniqueId(), session);
        gui.openMainCombatScreen(player, session);

        if (!session.isPlayerTurn()) {
            scheduleWildTurn(session, player);
        }
    }

    public CombatSession getSession(Player player) {
        return activeBattles.get(player.getUniqueId());
    }

    public void endBattle(Player player) {
        activeBattles.remove(player.getUniqueId());
        player.closeInventory();
        player.setWalkSpeed(0.2f);
    }

    public void executePlayerMove(Player player, int moveIndex) {
        CombatSession session = getSession(player);
        if (session == null || session.state() != CombatSession.State.FIGHT_SELECT) return;

        CreatureInstance attacker = session.playerCreature();
        Move move = attacker.moves().get(moveIndex);
        if (!attacker.hasAp(moveIndex)) {
            session.setMessage("No hay AP para " + move.displayName() + "!");
            gui.openMainCombatScreen(player, session);
            return;
        }

        session.setState(CombatSession.State.ANIMATING);
        executeMove(attacker, move, moveIndex, session.wildCreature(), session);
        attacker.useAp(moveIndex);

        if (session.wildCreature().isFainted()) {
            session.setMessage("Has vencido a " + session.wildCreature().template().displayName() + "!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
            return;
        }

        if (session.playerCreature().isFainted()) {
            session.setMessage("Tu " + session.playerCreature().template().displayName() + " ha sido vencido!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
            return;
        }

        scheduleWildTurn(session, player);
    }

    public void executeWildTurn(Player player) {
        CombatSession session = getSession(player);
        if (session == null) return;

        CreatureInstance wild = session.wildCreature();
        if (wild.isFainted()) {
            endBattle(player);
            return;
        }

        List<Move> moves = wild.moves();
        int idx = new Random().nextInt(moves.size());
        if (!wild.hasAp(idx)) {
            for (int i = 0; i < moves.size(); i++) {
                if (wild.hasAp(i)) { idx = i; break; }
            }
            if (!wild.hasAp(idx)) {
                session.setMessage(wild.template().displayName() + " se queda mirando...");
                gui.openMainCombatScreen(player, session);
                return;
            }
        }

        Move move = moves.get(idx);
        session.setState(CombatSession.State.ANIMATING);
        executeMove(wild, move, idx, session.playerCreature(), session);
        wild.useAp(idx);

        if (session.playerCreature().isFainted()) {
            session.setMessage("Tu " + session.playerCreature().template().displayName() + " ha sido vencido!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
            return;
        }

        session.setPlayerTurn(true);
        session.setState(CombatSession.State.MAIN);
        gui.openMainCombatScreen(player, session);
    }

    private void executeMove(CreatureInstance attacker, Move move, int moveIndex,
                              CreatureInstance defender, CombatSession session) {
        int damage = attacker.calculateDamage(move, defender);
        defender.setHp(defender.hp() - damage);

        String msg = attacker.template().displayName() + " uso " + move.displayName();
        if (damage > 0) {
            msg += "! " + damage + " de dano!";
        } else {
            msg += "! (sin dano)";
        }
        session.setMessage(msg);

        if (move.statusEffect() != null && !move.statusEffect().isEmpty()
            && defender.statusEffect() == null && damage > 0) {
            defender.setStatusEffect(move.statusEffect());
            session.setMessage(session.message() + " " + defender.template().displayName()
                + " ahora tiene " + move.statusEffect() + "!");
        }
    }

    private void scheduleWildTurn(CombatSession session, Player player) {
        session.setPlayerTurn(false);
        session.setState(CombatSession.State.MAIN);
        gui.openMainCombatScreen(player, session);
        Bukkit.getScheduler().runTaskLater(plugin, () -> executeWildTurn(player), 40L);
    }

    public void attemptCapture(Player player) {
        CombatSession session = getSession(player);
        if (session == null || session.state() != CombatSession.State.BAG_SELECT) return;

        ItemStack curseBook = captureManager.removeCurseBook(player);
        if (curseBook == null) {
            session.setMessage("No tienes un libro de la Maldicion de la Lesser Key!");
            session.setState(CombatSession.State.MAIN);
            gui.openMainCombatScreen(player, session);
            return;
        }

        CreatureInstance wild = session.wildCreature();
        int level = captureManager.getCreatureLevel(wild);
        boolean success = captureManager.attemptCapture(wild);

        if (success) {
            ItemStack capturedBook = curseManager.createCapturedBook(curseBook, wild, level);
            player.getInventory().addItem(capturedBook);

            com.chanequemon.player.PlayerData data = ((com.chanequemon.ChanequemonPlugin)plugin)
                .playerDataManager().get(player);
            data.addCaptured(wild.template().id());

            plugin.getLogger().info(player.getName() + " captured " + wild.template().id() + " (Lv." + level + ")");
            session.setMessage("Has capturado a " + wild.template().displayName() + " (Nivel " + level + ")!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
        } else {
            session.setMessage(wild.template().displayName() + " resistio el Sello de Salomon! "
                + "El libro se ha consumido.");
            session.setState(CombatSession.State.MAIN);
            gui.openMainCombatScreen(player, session);
        }
    }
}

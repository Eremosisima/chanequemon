package com.chanequemon.combat;

import com.chanequemon.capture.CaptureManager;
import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
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
        session.wildCreature().processStatusEndOfTurn();

        if (session.wildCreature().isFainted()) {
            awardXp(attacker, session.wildCreature());
            saveProgression(player, attacker);
            autoCaptureIfBinder(player, attacker, session.wildCreature());
            session.setMessage("Has vencido a " + session.wildCreature().template().displayName() + "!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
            return;
        }

        if (session.playerCreature().isFainted()) {
            playerFaintCleanup(player, session.playerCreature());
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

        session.playerCreature().processStatusEndOfTurn();
        if (session.playerCreature().isFainted()) {
            session.setMessage("Tu " + session.playerCreature().template().displayName() + " ha sido vencido!");
            session.setState(CombatSession.State.ENDED);
            endBattle(player);
            return;
        }

        if (!session.playerCreature().canAct()) {
            session.setMessage("Tu " + session.playerCreature().template().displayName()
                + " esta en " + session.playerCreature().statusEffect() + " y no puede moverse!");
            session.setPlayerTurn(true);
            session.setState(CombatSession.State.MAIN);
            gui.openMainCombatScreen(player, session);
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

    private void awardXp(CreatureInstance winner, CreatureInstance loser) {
        int totalBase = loser.template().baseHp() + loser.template().baseAttack()
            + loser.template().baseDefense() + loser.template().baseSpeed()
            + loser.template().baseLck() + loser.template().baseWis();
        int xpGain = (int) (totalBase * (0.10 + Math.random() * 0.10));
        winner.gainXp(xpGain);
    }

    private void autoCaptureIfBinder(Player player, CreatureInstance binder, CreatureInstance wild) {
        if (!"espiritu_vinculante".equals(binder.template().id())) return;
        ItemStack curseBook = curseManager.findCurseBookWithBinder(player);
        if (curseBook == null) return;
        int level = captureManager.getCreatureLevel(wild);
        ItemStack capturedBook = curseManager.createCapturedBook(curseBook, wild, level);
        curseBook.setAmount(curseBook.getAmount() - 1);
        if (curseBook.getAmount() <= 0) {
            player.getInventory().remove(curseBook);
        }
        player.getInventory().addItem(capturedBook);
        com.chanequemon.player.PlayerData data = ((com.chanequemon.ChanequemonPlugin)plugin)
            .playerDataManager().get(player);
        data.addCaptured(wild.template().id());
        plugin.getLogger().info(player.getName() + " auto-captured " + wild.template().id() + " via binder");
    }

    private void playerFaintCleanup(Player player, CreatureInstance creature) {
        String id = creature.template().id();
        if ("espiritu_vinculante".equals(id)) {
            curseManager.setBinderCooldown(player, System.currentTimeMillis() + 300000);
            player.sendMessage(Component.text("Tu Espiritu Vinculante se ha desvanecido. 5 min de reuso.", NamedTextColor.GRAY));
        } else {
            curseManager.removeBookFromInventory(player, id);
            player.sendMessage(Component.text("El libro de " + creature.template().displayName() + " se ha desintegrado!", NamedTextColor.RED));
        }
    }

    private void saveProgression(Player player, CreatureInstance creature) {
        com.chanequemon.player.PlayerData data = ((com.chanequemon.ChanequemonPlugin)plugin)
            .playerDataManager().get(player);
        data.setProgression(creature.template().id(), creature.battleLevel(), creature.xp(), creature.statBonus());
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
            session.setMessage("No tienes un grimorio de la Maldicion de Lemegeton Clavicula!");
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

package com.chanequemon.combat;

import com.chanequemon.capture.CaptureManager;
import com.chanequemon.model.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class CombatGUI implements Listener {
    private final CombatManager combatManager;
    private final CaptureManager captureManager;

    public CombatGUI(CombatManager combatManager, CaptureManager captureManager) {
        this.combatManager = combatManager;
        this.captureManager = captureManager;
    }

    public void openMainCombatScreen(Player player, CombatSession session) {
        Inventory inv = Bukkit.createInventory(new CombatHolder(session), 54,
            Component.text("Chanequemon - Combate", NamedTextColor.DARK_RED));

        Creature wild = session.wildCreature().template();
        Creature playerC = session.playerCreature().template();

        // Row 0: Wild creature display
        inv.setItem(0, creatureHead(wild, session.wildCreature(), false));

        // HP bar wild (slots 1-8)
        fillHpBar(inv, 1, 8, session.wildCreature().hpRatio());

        // Row 1: Wild creature info
        int level = captureManager.getCreatureLevel(session.wildCreature());
        inv.setItem(9, label(wild.displayName() + " (Salvaje) Nv." + level, NamedTextColor.RED));
        inv.setItem(17, label("HP: " + session.wildCreature().hp() + "/" + session.wildCreature().maxHp(), NamedTextColor.GREEN));

        // Row 2: Message area
        String msg = session.message();
        if (msg != null && !msg.isEmpty()) {
            inv.setItem(22, label(">" + msg, NamedTextColor.YELLOW));
        }

        // Divider
        for (int i = 18; i <= 26; i++) inv.setItem(i, filler());
        for (int i = 27; i <= 35; i++) inv.setItem(i, filler());

        // Row 4: Player creature
        CreatureInstance pc = session.playerCreature();
        inv.setItem(36, creatureHead(playerC, pc, true));
        fillHpBar(inv, 37, 44, pc.hpRatio());
        inv.setItem(45, label(playerC.displayName() + " Nv." + pc.battleLevel(), NamedTextColor.AQUA));
        inv.setItem(53, label("HP: " + pc.hp() + "/" + pc.maxHp(), NamedTextColor.GREEN));

        // Row 5: Action buttons
        inv.setItem(47, button(Material.DIAMOND_SWORD, "PELEAR", NamedTextColor.RED));
        inv.setItem(48, button(Material.ENDER_PEARL, "CRIATURAS", NamedTextColor.LIGHT_PURPLE));
        inv.setItem(49, button(Material.ENCHANTED_BOOK, "CAPTURAR", NamedTextColor.GOLD));
        inv.setItem(50, button(Material.FEATHER, "HUIR", NamedTextColor.GRAY));

        player.openInventory(inv);
        player.setWalkSpeed(0f);
    }

    public void openFightScreen(Player player, CombatSession session) {
        Inventory inv = Bukkit.createInventory(new CombatHolder(session), 54,
            Component.text("Selecciona un movimiento", NamedTextColor.RED));

        CreatureInstance ci = session.playerCreature();
        List<Move> moves = ci.moves();
        for (int i = 0; i < Math.min(moves.size(), 4); i++) {
            Move m = moves.get(i);
            int ap = ci.moveAp().get(i);
            Material mat = switch (m.moveType()) {
                case PHYSICAL -> Material.IRON_SWORD;
                case MAGICAL -> Material.BLAZE_ROD;
                case STATUS -> Material.BREWING_STAND;
            };
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(m.displayName(), NamedTextColor.WHITE, TextDecoration.BOLD));
            meta.lore(List.of(
                Component.text("Poder: " + m.power(), NamedTextColor.GRAY),
                Component.text("Elemento: " + m.element(), NamedTextColor.DARK_GRAY),
                Component.text("Tipo: " + m.moveType(), NamedTextColor.GRAY),
                Component.text("AP: " + ap + "/" + (m.apCost() * 3), NamedTextColor.GOLD)
            ));
            item.setItemMeta(meta);
            inv.setItem(i * 9, item);
        }

        inv.setItem(49, button(Material.BARRIER, "VOLVER", NamedTextColor.RED));
        player.openInventory(inv);
    }

    public void openCaptureScreen(Player player, CombatSession session) {
        Inventory inv = Bukkit.createInventory(new CombatHolder(session), 54,
            Component.text("Capturar con el Libro Maldito", NamedTextColor.GOLD));

        CreatureInstance wild = session.wildCreature();
        int level = captureManager.getCreatureLevel(wild);
        String difficulty = captureManager.getDifficultyLabel(wild);
        int bookCount = captureManager.getCurseBookCount(player);
        double threshold = captureManager.getCaptureThreshold(wild);

        // Wild creature info
        inv.setItem(4, creatureHead(wild.template(), wild, false));

        inv.setItem(18, label("Nivel: " + level, NamedTextColor.DARK_GREEN));
        inv.setItem(19, label("Dificultad: " + difficulty, getDifficultyColor(difficulty)));
        inv.setItem(20, label("HP: " + wild.hp() + "/" + wild.maxHp(), NamedTextColor.GREEN));

        // Probabilidad estimada
        int pct = (int) Math.round(threshold * 100);
        NamedTextColor pctColor = pct >= 50 ? NamedTextColor.GREEN : pct >= 25 ? NamedTextColor.GOLD : NamedTextColor.RED;
        inv.setItem(22, label("Probabilidad: " + pct + "%", pctColor));

        // Status
        if (wild.statusEffect() != null && !wild.statusEffect().isEmpty()) {
            inv.setItem(23, label("Estado: " + wild.statusEffect(), NamedTextColor.LIGHT_PURPLE));
        }

        // Libros malditos disponibles
        inv.setItem(24, label("Libros Malditos: " + bookCount, NamedTextColor.DARK_PURPLE));

        // Boton de captura
        if (bookCount > 0) {
            inv.setItem(40, curseBookButton(bookCount));
        } else {
            inv.setItem(40, button(Material.BARRIER, "SIN LIBROS MALDITOS!", NamedTextColor.RED));
        }

        // Hover info para captura
        inv.setItem(31, infoBook(level, wild));

        inv.setItem(49, button(Material.BARRIER, "VOLVER", NamedTextColor.RED));
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof CombatHolder holder)) return;
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;

        CombatSession session = holder.session();
        if (session == null || session.state() == CombatSession.State.ENDED) return;

        int slot = event.getSlot();
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType() == Material.AIR) return;
        if (item.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        switch (session.state()) {
            case MAIN -> handleMainClick(player, session, slot);
            case FIGHT_SELECT -> handleFightClick(player, session, slot);
            case BAG_SELECT -> handleCaptureClick(player, session, slot);
            case CREATURE_SELECT -> handleCreatureClick(player, session, slot);
        }
    }

    private void handleMainClick(Player player, CombatSession session, int slot) {
        if (slot == 47) {
            session.setState(CombatSession.State.FIGHT_SELECT);
            openFightScreen(player, session);
        } else if (slot == 48) {
            session.setMessage("Cambiar criatura no implementado aun");
            openMainCombatScreen(player, session);
        } else if (slot == 49) {
            session.setState(CombatSession.State.BAG_SELECT);
            openCaptureScreen(player, session);
        } else if (slot == 50) {
            if (Math.random() < 0.5) {
                session.setMessage("Huis te del combate!");
                combatManager.endBattle(player);
            } else {
                session.setMessage("No pudiste huir!");
                openMainCombatScreen(player, session);
            }
        }
    }

    private void handleFightClick(Player player, CombatSession session, int slot) {
        if (slot == 49) {
            session.setState(CombatSession.State.MAIN);
            openMainCombatScreen(player, session);
            return;
        }
        int moveIndex = slot / 9;
        List<Move> moves = session.playerCreature().moves();
        if (moveIndex >= 0 && moveIndex < moves.size()) {
            combatManager.executePlayerMove(player, moveIndex);
        }
    }

    private void handleCaptureClick(Player player, CombatSession session, int slot) {
        if (slot == 49) {
            session.setState(CombatSession.State.MAIN);
            openMainCombatScreen(player, session);
            return;
        }
        if (slot == 40) {
            if (captureManager.getCurseBookCount(player) <= 0) {
                session.setMessage("No tienes libros malditos para capturar!");
                openCaptureScreen(player, session);
                return;
            }
            combatManager.attemptCapture(player);
        }
    }

    private void handleCreatureClick(Player player, CombatSession session, int slot) {
        if (slot == 49) {
            session.setState(CombatSession.State.MAIN);
            openMainCombatScreen(player, session);
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder(false) instanceof CombatHolder) {
            event.setCancelled(true);
        }
    }

    // --- Item builders ---

    private ItemStack creatureHead(Creature template, CreatureInstance instance, boolean playerOwned) {
        ItemStack head = new ItemStack(playerOwned ? Material.PLAYER_HEAD : Material.ZOMBIE_HEAD);
        ItemMeta meta = head.getItemMeta();
        meta.displayName(Component.text(template.displayName(),
            playerOwned ? NamedTextColor.AQUA : NamedTextColor.RED));
        List<Component> lore = new ArrayList<>();
        int level = captureManager.getCreatureLevel(instance);
        lore.add(Component.text("Nivel " + level, NamedTextColor.DARK_GREEN));
        lore.add(Component.text("Tipo: " + template.type(), NamedTextColor.GRAY));
        lore.add(Component.text("HP: " + instance.hp() + "/" + instance.maxHp(), NamedTextColor.GREEN));
        if (instance.statusEffect() != null && !instance.statusEffect().isEmpty()) {
            String mech = switch (instance.statusEffect().toUpperCase()) {
                case "SLEEP" -> " (50% dormido)";
                case "PARALYSIS" -> " (Velocidad/2, 25% paralisis)";
                case "POISON" -> " (Dano por turno: 1/8 HP)";
                case "BURN" -> " (Ataque/2, dano 1/16 HP)";
                default -> "";
            };
            lore.add(Component.text("Estado: " + instance.statusEffect() + mech, NamedTextColor.LIGHT_PURPLE));
        }
        if (playerOwned) {
            int xp = instance.xp();
            int next = instance.xpToNextLevel();
            lore.add(Component.text("XP: " + xp + "/" + next, NamedTextColor.DARK_AQUA));
            if (next > 0) {
                int pct = Math.min(100, xp * 100 / next);
                lore.add(Component.text("Progreso: " + pct + "%", NamedTextColor.GRAY));
            }
            lore.add(Component.text("Potenciador: +" + instance.statBonus(), NamedTextColor.GOLD));
        }
        lore.add(Component.text("Origen: " + template.loreOrigin(), NamedTextColor.DARK_GRAY));
        meta.lore(lore);
        head.setItemMeta(meta);
        return head;
    }

    private void fillHpBar(Inventory inv, int start, int end, double ratio) {
        int green = (int) Math.round((end - start + 1) * ratio);
        for (int i = start; i <= end; i++) {
            Material mat;
            if (i - start < green) {
                mat = ratio > 0.5 ? Material.LIME_STAINED_GLASS_PANE : Material.YELLOW_STAINED_GLASS_PANE;
            } else {
                mat = Material.RED_STAINED_GLASS_PANE;
            }
            inv.setItem(i, hpPiece(mat));
        }
    }

    private ItemStack hpPiece(Material mat) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(" "));
        meta.lore(List.of());
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack label(String text, NamedTextColor color) {
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(text, color));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack button(Material mat, String text, NamedTextColor color) {
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(text, color, TextDecoration.BOLD));
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack curseBookButton(int count) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("USAR LIBRO MALDITO", NamedTextColor.GOLD, TextDecoration.BOLD));
        meta.lore(List.of(
            Component.text("Cantidad: " + count, NamedTextColor.GRAY),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("El libro absorbira el espiritu", NamedTextColor.DARK_PURPLE),
            Component.text("de la criatura en sus paginas.", NamedTextColor.DARK_PURPLE),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("Click para intentar captura", NamedTextColor.YELLOW)
        ));
        book.setItemMeta(meta);
        return book;
    }

    private ItemStack infoBook(int level, CreatureInstance creature) {
        ItemStack book = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text("Info de Captura", NamedTextColor.GRAY));
        meta.lore(List.of(
            Component.text("Nivel " + level + " — ", NamedTextColor.DARK_GREEN)
                .append(Component.text("Dificultad " + captureManager.getDifficultyLabel(creature),
                    getDifficultyColor(captureManager.getDifficultyLabel(creature)))),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("A menor HP, mayor probabilidad", NamedTextColor.GRAY),
            Component.text("Estados alterados ayudan (+15%)", NamedTextColor.GRAY),
            Component.text("Criaturas de alto nivel resisten mas", NamedTextColor.RED)
        ));
        book.setItemMeta(meta);
        return book;
    }

    private NamedTextColor getDifficultyColor(String diff) {
        return switch (diff) {
            case "Facil" -> NamedTextColor.GREEN;
            case "Media" -> NamedTextColor.YELLOW;
            case "Dificil" -> NamedTextColor.GOLD;
            case "Muy Dificil" -> NamedTextColor.RED;
            case "Legendaria" -> NamedTextColor.DARK_RED;
            default -> NamedTextColor.GRAY;
        };
    }

    private ItemStack filler() {
        ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.text(" "));
        item.setItemMeta(meta);
        return item;
    }

    private static class CombatHolder implements InventoryHolder {
        private final CombatSession session;
        CombatHolder(CombatSession session) { this.session = session; }
        public CombatSession session() { return session; }
        @Override public Inventory getInventory() { return null; }
    }
}

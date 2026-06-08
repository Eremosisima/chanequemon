package com.chanequemon.enchant;

import com.chanequemon.model.CreatureInstance;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class ChanequeCurseManager {
    public static final String CURSE_NAME = "Maldicion de Lemegeton Clavicula";
    private static final double ENCHANT_TABLE_CURSE_CHANCE = 0.35;
    private static final double SUSPICIOUS_SAND_CURSE_CHANCE = 0.12;
    private static final double SUSPICIOUS_GRAVEL_CURSE_CHANCE = 0.10;
    private static final int ENCOUNTER_MULTIPLIER_CURSED = 4;
    private static final long COOLDOWN_CURSED_MS = 800;

    private final JavaPlugin plugin;
    private final NamespacedKey curseKey;
    private final NamespacedKey curseBookKey;
    private final NamespacedKey capturedCreatureKey;
    private final Random rng = new Random();

    private static final Map<Enchantment, String> ENCHANT_AURA_MAP = Map.of(
        Enchantment.SHARPNESS, "AMPLIFIER",
        Enchantment.POWER, "AMPLIFIER",
        Enchantment.PROTECTION, "RADIUS",
        Enchantment.UNBREAKING, "RADIUS",
        Enchantment.EFFICIENCY, "SPEED",
        Enchantment.QUICK_CHARGE, "SPEED"
    );

    public ChanequeCurseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.curseKey = new NamespacedKey(plugin, "chaneque_curse");
        this.curseBookKey = new NamespacedKey(plugin, "chaneque_curse_book");
        this.capturedCreatureKey = new NamespacedKey(plugin, "captured_creature");
    }

    public NamespacedKey curseKey() { return curseKey; }
    public NamespacedKey capturedCreatureKey() { return capturedCreatureKey; }
    public int encounterMultiplier() { return ENCOUNTER_MULTIPLIER_CURSED; }
    public long cooldownCursedMs() { return COOLDOWN_CURSED_MS; }
    public double enchantTableChance() { return ENCHANT_TABLE_CURSE_CHANCE; }

    public boolean isSuspiciousBlock(Material type) {
        return type == Material.SUSPICIOUS_SAND || type == Material.SUSPICIOUS_GRAVEL;
    }

    public boolean shouldDropCurseBook(Material type) {
        double chance = type == Material.SUSPICIOUS_SAND
            ? SUSPICIOUS_SAND_CURSE_CHANCE : SUSPICIOUS_GRAVEL_CURSE_CHANCE;
        return rng.nextDouble() < chance;
    }

    public boolean hasCurse(Player player) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && isCursed(item)) return true;
        }
        for (ItemStack armor : player.getInventory().getArmorContents()) {
            if (armor != null && isCursed(armor)) return true;
        }
        ItemStack offhand = player.getInventory().getItemInOffHand();
        return isCursed(offhand);
    }

    public boolean isCursed(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(curseKey, PersistentDataType.BOOLEAN);
    }

    public boolean isCurseBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(curseBookKey, PersistentDataType.BOOLEAN);
    }

    public boolean isCapturedBook(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        return item.getItemMeta().getPersistentDataContainer()
            .has(capturedCreatureKey, PersistentDataType.STRING);
    }

    public String getCapturedCreatureId(ItemStack book) {
        if (!isCapturedBook(book)) return null;
        return book.getItemMeta().getPersistentDataContainer()
            .get(capturedCreatureKey, PersistentDataType.STRING);
    }

    public ItemStack createCurseBook() {
        ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.displayName(Component.text(CURSE_NAME, NamedTextColor.DARK_PURPLE, TextDecoration.BOLD));
        meta.lore(List.of(
            Component.text("Un grimorio en blanco del Lemegeton Clavicula Salomonis.", NamedTextColor.GRAY),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("Al portarlo, atrae criaturas mitologicas", NamedTextColor.DARK_PURPLE),
            Component.text("que emergen del bioma circundante.", NamedTextColor.DARK_PURPLE),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("Maldicion — No removible por medios normales", NamedTextColor.RED),
            Component.text("", NamedTextColor.DARK_GRAY),
            Component.text("Usalo contra una criatura debilitada", NamedTextColor.DARK_PURPLE),
            Component.text("para sellar su escencia con el Sello de Salomon.", NamedTextColor.DARK_PURPLE)
        ));
        meta.getPersistentDataContainer().set(curseBookKey, PersistentDataType.BOOLEAN, true);
        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        book.setItemMeta(meta);
        return book;
    }

    public ItemStack applyCurseToItem(ItemStack target) {
        if (target == null || target.getType() == Material.AIR) return target;
        ItemStack result = target.clone();
        ItemMeta meta = result.getItemMeta();
        meta.getPersistentDataContainer().set(curseKey, PersistentDataType.BOOLEAN, true);

        List<Component> lore = meta.lore() != null ? meta.lore() : new ArrayList<>();
        lore.add(Component.text(""));
        lore.add(Component.text(CURSE_NAME, NamedTextColor.RED, TextDecoration.BOLD));
        meta.lore(lore);

        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        result.setItemMeta(meta);
        return result;
    }

    public ItemStack createCapturedBook(ItemStack curseBook, CreatureInstance creature, int level) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        book.setAmount(1);
        ItemMeta meta = book.getItemMeta();

        meta.getPersistentDataContainer().set(capturedCreatureKey, PersistentDataType.STRING, creature.template().id());

        meta.displayName(Component.text("Esencia de ", NamedTextColor.GOLD)
            .append(Component.text(creature.template().displayName(), NamedTextColor.AQUA, TextDecoration.BOLD)));

        List<Component> lore = new ArrayList<>();
        lore.add(Component.text("Nivel " + level, NamedTextColor.DARK_GREEN));
        lore.add(Component.text("Tipo: " + creature.template().type(), NamedTextColor.GRAY));
        lore.add(Component.text("Esencia: " + creature.template().affinity(), NamedTextColor.DARK_GRAY));
        lore.add(Component.text("", NamedTextColor.DARK_GRAY));
        lore.add(Component.text("Origen: " + creature.template().loreOrigin(), NamedTextColor.DARK_PURPLE, TextDecoration.ITALIC));
        lore.add(Component.text("", NamedTextColor.DARK_GRAY));
        lore.add(Component.text("El espiritu habita en estas paginas...", NamedTextColor.GOLD, TextDecoration.ITALIC));
        lore.add(Component.text("", NamedTextColor.DARK_GRAY));
        lore.add(Component.text("Encantamientos potencian el aura", NamedTextColor.GRAY));
        lore.add(Component.text("de la criatura al ser invocada.", NamedTextColor.GRAY));
        meta.lore(lore);

        meta.addEnchant(Enchantment.MENDING, 1, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        book.setItemMeta(meta);
        return book;
    }

    @SuppressWarnings("deprecation")
    public int getAuraAmplifierBonus(ItemStack book) {
        if (!isCapturedBook(book)) return 0;
        Map<Enchantment, Integer> enchants = book.getEnchantments();
        int bonus = 0;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            String auraType = ENCHANT_AURA_MAP.get(e.getKey());
            if ("AMPLIFIER".equals(auraType)) bonus += e.getValue();
        }
        return bonus;
    }

    @SuppressWarnings("deprecation")
    public int getAuraRadiusBonus(ItemStack book) {
        if (!isCapturedBook(book)) return 0;
        Map<Enchantment, Integer> enchants = book.getEnchantments();
        int bonus = 0;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            String auraType = ENCHANT_AURA_MAP.get(e.getKey());
            if ("RADIUS".equals(auraType)) bonus += e.getValue() * 2;
        }
        return bonus;
    }

    @SuppressWarnings("deprecation")
    public int getAuraSpeedBonus(ItemStack book) {
        if (!isCapturedBook(book)) return 0;
        Map<Enchantment, Integer> enchants = book.getEnchantments();
        int bonus = 0;
        for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
            String auraType = ENCHANT_AURA_MAP.get(e.getKey());
            if ("SPEED".equals(auraType)) bonus += e.getValue() * 5;
        }
        return bonus;
    }

    public boolean rollEnchantTableCurse() {
        return rng.nextDouble() < ENCHANT_TABLE_CURSE_CHANCE;
    }
}

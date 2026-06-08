package com.chanequemon.enchant;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantmentListener implements Listener {
    private final ChanequeCurseManager curseManager;
    private final JavaPlugin plugin;

    public EnchantmentListener(ChanequeCurseManager curseManager, JavaPlugin plugin) {
        this.curseManager = curseManager;
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEnchantItem(EnchantItemEvent event) {
        if (event.isCancelled()) return;
        if (!curseManager.rollEnchantTableCurse()) return;

        Player player = event.getEnchanter();
        ItemStack item = event.getItem();
        if (item == null || item.getType() == Material.AIR) return;

        plugin.getLogger().info(player.getName() + " recibio la Maldicion de Lemegeton al encantar!");

        ItemStack cursed = curseManager.applyCurseToItem(item);
        event.getInventory().setItem(0, cursed);

        player.sendMessage(Component.text("Sientes el peso de un sello antiguo... ", NamedTextColor.DARK_PURPLE)
            .append(Component.text("Maldicion de Lemegeton", NamedTextColor.RED))
            .append(Component.text(" se ha infundido en tu objeto!", NamedTextColor.DARK_PURPLE)));
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        Block block = event.getBlock();
        Material type = block.getType();

        if (!curseManager.isSuspiciousBlock(type)) return;
        if (!curseManager.shouldDropCurseBook(type)) return;

        Player player = event.getPlayer();
        block.getWorld().dropItemNaturally(block.getLocation().add(0.5, 0.5, 0.5), curseManager.createCurseBook());

        player.sendMessage(Component.text("Encuentras un libro antiguo entre la arena...", NamedTextColor.GOLD));
        plugin.getLogger().info(player.getName() + " found a Lemegeton curse book in " + type.name());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack left = anvil.getItem(0);
        ItemStack right = anvil.getItem(1);

        if (left == null || right == null) return;
        if (left.getType() == Material.AIR || right.getType() == Material.AIR) return;

        if (!curseManager.isCurseBook(right)) return;
        if (curseManager.isCursed(left)) return;

        ItemStack result = curseManager.applyCurseToItem(left);
        event.setResult(result);

        anvil.setMaximumRepairCost(40);
    }
}

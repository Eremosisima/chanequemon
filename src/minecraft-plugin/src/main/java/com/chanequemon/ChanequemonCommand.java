package com.chanequemon;

import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.Creature;
import com.chanequemon.player.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ChanequemonCommand implements CommandExecutor {
    private final ChanequemonPlugin plugin;

    public ChanequemonCommand(ChanequemonPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Chanequemon v" + plugin.getDescription().getVersion(),
                NamedTextColor.GOLD));
            sender.sendMessage(Component.text("/chanequemon list — lista de criaturas", NamedTextColor.GRAY));
            sender.sendMessage(Component.text("/chanequemon captured — tus capturas", NamedTextColor.GRAY));
            sender.sendMessage(Component.text("/chanequemon curse — obtener grimorio de Lemegeton Clavicula", NamedTextColor.DARK_PURPLE));
            sender.sendMessage(Component.text("/chanequemon books — ver libros de criaturas", NamedTextColor.AQUA));
            sender.sendMessage(Component.text("/chanequemon invocar — invoca la criatura del libro en mano", NamedTextColor.GOLD));
            sender.sendMessage(Component.text("/chanequemon regresar — devuelve la criatura a su libro", NamedTextColor.GOLD));
            sender.sendMessage(Component.text("/chanequemon dismiss — lo mismo que regresar", NamedTextColor.GRAY));
            return true;
        }

        boolean isPlayer = sender instanceof Player;

        switch (args[0].toLowerCase()) {
            case "list" -> {
                sender.sendMessage(Component.text("--- Criaturas disponibles ---", NamedTextColor.GREEN));
                for (Creature c : plugin.creatureRegistry().all()) {
                    sender.sendMessage(Component.text("  " + c.id() + " — " + c.displayName()
                        + " [" + c.type() + "]", NamedTextColor.AQUA));
                }
            }
            case "captured" -> {
                if (!isPlayer) { sender.sendMessage("Solo jugadores."); return true; }
                Player player = (Player) sender;
                PlayerData data = plugin.playerDataManager().get(player);
                sender.sendMessage(Component.text("--- Tus capturas (" + data.capturedCount() + ") ---",
                    NamedTextColor.GREEN));
                for (String id : data.capturedIds()) {
                    Creature c = plugin.creatureRegistry().get(id);
                    String name = c != null ? c.displayName() : id;
                    sender.sendMessage(Component.text("  " + name, NamedTextColor.AQUA));
                }
            }
            case "curse" -> {
                if (!isPlayer) { sender.sendMessage("Solo jugadores."); return true; }
                Player player = (Player) sender;
                player.getInventory().addItem(plugin.curseManager().createCurseBook());
                sender.sendMessage(Component.text("Grimorio del ", NamedTextColor.DARK_PURPLE)
                    .append(Component.text("Maldicion de Lemegeton Clavicula", NamedTextColor.RED))
                    .append(Component.text(" agregado a tu inventario.", NamedTextColor.DARK_PURPLE)));
            }
            case "books" -> {
                if (!isPlayer) { sender.sendMessage("Solo jugadores."); return true; }
                Player player = (Player) sender;
                List<String> capturedNames = new ArrayList<>();
                for (ItemStack item : player.getInventory().getContents()) {
                    if (item != null && plugin.curseManager().isCapturedBook(item)) {
                        String id = plugin.curseManager().getCapturedCreatureId(item);
                        Creature c = plugin.creatureRegistry().get(id);
                        String name = c != null ? c.displayName() : id;
                        capturedNames.add(name);
                    }
                }
                if (capturedNames.isEmpty()) {
                    sender.sendMessage(Component.text("No tienes libros de criaturas en tu inventario.",
                        NamedTextColor.GRAY));
                } else {
                    sender.sendMessage(Component.text("--- Libros de criaturas en inventario ---",
                        NamedTextColor.GREEN));
                    for (String name : capturedNames) {
                        sender.sendMessage(Component.text("  " + name, NamedTextColor.AQUA));
                    }
                }
            }
            case "invocar" -> {
                if (!isPlayer) { sender.sendMessage("Solo jugadores."); return true; }
                Player player = (Player) sender;
                ItemStack held = player.getInventory().getItemInMainHand();
                if (held == null || !plugin.curseManager().isCapturedBook(held)) {
                    sender.sendMessage(Component.text("Debes sostener un libro de criatura capturada.",
                        NamedTextColor.RED));
                    return true;
                }
                String creatureId = plugin.curseManager().getCapturedCreatureId(held);
                Creature creature = plugin.creatureRegistry().get(creatureId);
                if (creature == null) {
                    sender.sendMessage(Component.text("Criatura no encontrada.", NamedTextColor.RED));
                    return true;
                }
                plugin.summonManager().summonCreature(player, creature, held);
            }
            case "dismiss", "regresar" -> {
                if (!isPlayer) { sender.sendMessage("Solo jugadores."); return true; }
                Player player = (Player) sender;
                plugin.summonManager().dismissSummon(player);
            }
            default -> {
                sender.sendMessage(Component.text("Comando desconocido. Usa /chanequemon",
                    NamedTextColor.RED));
            }
        }
        return true;
    }
}

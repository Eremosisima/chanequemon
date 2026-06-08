package com.chanequemon.summon;

import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.Creature;
import com.chanequemon.model.Move;
import com.chanequemon.model.SupportAbility;
import com.chanequemon.registry.CreatureRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Lectern;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class LecternListener implements Listener {
    private final JavaPlugin plugin;
    private final CreatureRegistry registry;
    private final ChanequeCurseManager curseManager;

    private final Map<Location, LecternCreature> lecternCreatures = new HashMap<>();
    private final NamespacedKey lecternEntityKey;

    private static final int WANDER_RADIUS = 12;
    private static final long WANDER_INTERVAL = 60L;

    public LecternListener(JavaPlugin plugin, CreatureRegistry registry, ChanequeCurseManager curseManager) {
        this.plugin = plugin;
        this.registry = registry;
        this.curseManager = curseManager;
        this.lecternEntityKey = new NamespacedKey(plugin, "lectern_creature");
    }

    @EventHandler
    public void onLecternInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.LECTERN) return;

        Lectern lectern = (Lectern) event.getClickedBlock().getState();
        ItemStack book = lectern.getInventory().getBook();

        if (book == null || !curseManager.isCapturedBook(book)) return;
        event.setCancelled(true);

        String creatureId = curseManager.getCapturedCreatureId(book);
        Creature creature = registry.get(creatureId);
        if (creature == null) return;

        Player player = event.getPlayer();
        openBookPages(player, creature, book);

        Location lecternLoc = event.getClickedBlock().getLocation();
        if (!lecternCreatures.containsKey(lecternLoc)) {
            spawnLecternWanderer(lecternLoc, creature, player);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        removeLecternCreaturesForPlayer(event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() == Material.LECTERN) {
            LecternCreature lc = lecternCreatures.remove(event.getBlock().getLocation());
            if (lc != null) {
                if (lc.entity != null && lc.entity.isValid()) lc.entity.remove();
                if (lc.task != null) lc.task.cancel();
            }
        }
    }

    public void removeLecternCreaturesForPlayer(Player player) {
        lecternCreatures.entrySet().removeIf(e -> {
            if (e.getValue().owner != null && e.getValue().owner.equals(player.getUniqueId())) {
                if (e.getValue().entity != null && e.getValue().entity.isValid()) e.getValue().entity.remove();
                if (e.getValue().task != null) e.getValue().task.cancel();
                return true;
            }
            return false;
        });
    }

    private void spawnLecternWanderer(Location lecternLoc, Creature creature, Player player) {
        EntityType entityType = getLecternEntityType(creature);
        Location spawnLoc = lecternLoc.clone().add(1, 1, 0);
        Entity entity = lecternLoc.getWorld().spawnEntity(spawnLoc, entityType);

        entity.setCustomName(Component.text(creature.displayName(), NamedTextColor.AQUA, TextDecoration.ITALIC));
        entity.setCustomNameVisible(true);
        entity.setPersistent(true);
        entity.setInvulnerable(true);
        entity.getPersistentDataContainer().set(lecternEntityKey, PersistentDataType.STRING, creature.id());

        if (entity instanceof Mob mob) {
            mob.setRemoveWhenFarAway(false);
            mob.setAI(true);
        }

        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!entity.isValid() || entity.isDead()) {
                lecternCreatures.remove(lecternLoc);
                return;
            }
            if (!(lecternLoc.getBlock().getState() instanceof Lectern lecternState)
                || lecternState.getInventory().getBook() == null
                || !curseManager.isCapturedBook(lecternState.getInventory().getBook())) {
                entity.remove();
                lecternCreatures.remove(lecternLoc);
                return;
            }
            if (entity instanceof Mob mob) {
                if (mob.getTarget() != null) mob.setTarget(null);
                if (mob.getLocation().distanceSquared(lecternLoc) > WANDER_RADIUS * WANDER_RADIUS) {
                    mob.teleport(lecternLoc.clone().add(1, 1, 0));
                }
            }
        }, 0L, WANDER_INTERVAL);

        lecternCreatures.put(lecternLoc, new LecternCreature(entity, player.getUniqueId(), task));
    }

    private void openBookPages(Player player, Creature creature, ItemStack book) {
        ItemStack writtenBook = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) writtenBook.getItemMeta();

        meta.setTitle("Esencia de " + creature.displayName());
        meta.setAuthor("Chanequemon");

        List<Component> pages = new ArrayList<>();

        pages.add(Component.text()
            .append(Component.text(creature.displayName(), NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline()).append(Component.newline())
            .append(Component.text("Criatura Mitologica", NamedTextColor.GRAY, TextDecoration.ITALIC))
            .append(Component.newline())
            .append(Component.text("Tipo: ", NamedTextColor.DARK_GRAY)).append(Component.text(creature.type().name(), NamedTextColor.AQUA))
            .append(Component.newline())
            .append(Component.text("Esencia: ", NamedTextColor.DARK_GRAY)).append(Component.text(creature.affinity().name(), NamedTextColor.AQUA))
            .build()
        );

        pages.add(Component.text()
            .append(Component.text("Origen", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline()).append(Component.newline())
            .append(Component.text(creature.loreOrigin(), NamedTextColor.DARK_PURPLE, TextDecoration.ITALIC))
            .append(Component.newline()).append(Component.newline())
            .append(Component.text("Dominio publico: ", NamedTextColor.DARK_GRAY))
            .append(Component.text(creature.isPublicDomain() ? "Si" : "No", creature.isPublicDomain() ? NamedTextColor.GREEN : NamedTextColor.RED))
            .build()
        );

        pages.add(Component.text()
            .append(Component.text("Estadisticas", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline()).append(Component.newline())
            .append(Component.text("HP: ", NamedTextColor.GRAY)).append(Component.text(String.valueOf(creature.baseHp()), NamedTextColor.GREEN))
            .append(Component.newline())
            .append(Component.text("Ataque: ", NamedTextColor.GRAY)).append(Component.text(String.valueOf(creature.baseAttack()), NamedTextColor.RED))
            .append(Component.newline())
            .append(Component.text("Defensa: ", NamedTextColor.GRAY)).append(Component.text(String.valueOf(creature.baseDefense()), NamedTextColor.YELLOW))
            .append(Component.newline())
            .append(Component.text("Velocidad: ", NamedTextColor.GRAY)).append(Component.text(String.valueOf(creature.baseSpeed()), NamedTextColor.AQUA))
            .build()
        );

        Component movesPage = Component.text()
            .append(Component.text("Movimientos", NamedTextColor.GOLD, TextDecoration.BOLD))
            .append(Component.newline()).append(Component.newline());
        for (int i = 0; i < Math.min(creature.moves().size(), 6); i++) {
            Move m = creature.moves().get(i);
            movesPage = movesPage
                .append(Component.text(m.displayName(), NamedTextColor.WHITE))
                .append(Component.newline())
                .append(Component.text("  Poder:" + m.power() + " " + m.element(), NamedTextColor.GRAY))
                .append(Component.newline());
        }
        pages.add(movesPage);

        if (!creature.supportAbilities().isEmpty()) {
            Component supportPage = Component.text()
                .append(Component.text("Aura de Apoyo", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline()).append(Component.newline());
            for (SupportAbility sa : creature.supportAbilities()) {
                supportPage = supportPage
                    .append(Component.text("[" + sa.aura() + "] ", sa.isBuff() ? NamedTextColor.GREEN : NamedTextColor.RED))
                    .append(Component.text(sa.effect(), NamedTextColor.AQUA))
                    .append(Component.newline())
                    .append(Component.text("  Alcance: " + sa.radius() + "m", NamedTextColor.GRAY))
                    .append(Component.newline());
            }
            pages.add(supportPage);
        }

        {
            Component descPage = Component.text()
                .append(Component.text("Descripcion", NamedTextColor.GOLD, TextDecoration.BOLD))
                .append(Component.newline()).append(Component.newline())
                .append(Component.text("Esta criatura mitologica habita", NamedTextColor.DARK_PURPLE))
                .append(Component.newline())
                .append(Component.text("ahora en las paginas de este", NamedTextColor.DARK_PURPLE))
                .append(Component.newline())
                .append(Component.text("libro. Al invocarla, otorga", NamedTextColor.DARK_PURPLE))
                .append(Component.newline())
                .append(Component.text("bendiciones o maldiciones segun", NamedTextColor.DARK_PURPLE))
                .append(Component.newline())
                .append(Component.text("su naturaleza.", NamedTextColor.DARK_PURPLE))
                .append(Component.newline()).append(Component.newline())
                .append(Component.text("Colocada en un atril, deambula", NamedTextColor.GRAY, TextDecoration.ITALIC))
                .append(Component.newline())
                .append(Component.text("en las cercanias.", NamedTextColor.GRAY, TextDecoration.ITALIC))
                .build();
            pages.add(descPage);
        }

        meta.pages(pages);
        writtenBook.setItemMeta(meta);
        player.openBook(writtenBook);
    }

    private EntityType getLecternEntityType(Creature creature) {
        String type = creature.type().name();
        return switch (type) {
            case "SPIRIT", "FEY" -> EntityType.ALLAY;
            case "DRAGON" -> EntityType.ENDER_DRAGON;
            case "UNDEAD" -> EntityType.ZOMBIE;
            case "BEAST" -> EntityType.WOLF;
            case "DEMON" -> EntityType.HOGLIN;
            case "MYTHIC" -> EntityType.VEX;
            case "ELEMENTAL" -> EntityType.BLAZE;
            default -> EntityType.ALLAY;
        };
    }

    private record LecternCreature(Entity entity, UUID owner, BukkitTask task) {}
}

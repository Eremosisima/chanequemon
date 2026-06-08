package com.chanequemon.summon;

import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.Creature;
import com.chanequemon.model.Element;
import com.chanequemon.model.SupportAbility;
import com.chanequemon.registry.CreatureRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.LecternAction;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class SummonManager implements Listener {
    private final JavaPlugin plugin;
    private final CreatureRegistry registry;
    private final ChanequeCurseManager curseManager;

    private final Map<UUID, SummonedCreature> activeSummons = new HashMap<>();

    private final NamespacedKey summonOwnerKey;
    private final NamespacedKey summonCreatureKey;

    private static final long AURA_INTERVAL = 40L;

    private static final Map<String, PotionEffectType> EFFECT_MAP = new HashMap<>();
    static {
        EFFECT_MAP.put("SPEED", PotionEffectType.SPEED);
        EFFECT_MAP.put("SLOWNESS", PotionEffectType.SLOWNESS);
        EFFECT_MAP.put("HASTE", PotionEffectType.HASTE);
        EFFECT_MAP.put("STRENGTH", PotionEffectType.STRENGTH);
        EFFECT_MAP.put("JUMP_BOOST", PotionEffectType.JUMP_BOOST);
        EFFECT_MAP.put("NAUSEA", PotionEffectType.NAUSEA);
        EFFECT_MAP.put("REGENERATION", PotionEffectType.REGENERATION);
        EFFECT_MAP.put("RESISTANCE", PotionEffectType.RESISTANCE);
        EFFECT_MAP.put("FIRE_RESISTANCE", PotionEffectType.FIRE_RESISTANCE);
        EFFECT_MAP.put("WATER_BREATHING", PotionEffectType.WATER_BREATHING);
        EFFECT_MAP.put("INVISIBILITY", PotionEffectType.INVISIBILITY);
        EFFECT_MAP.put("BLINDNESS", PotionEffectType.BLINDNESS);
        EFFECT_MAP.put("NIGHT_VISION", PotionEffectType.NIGHT_VISION);
        EFFECT_MAP.put("WEAKNESS", PotionEffectType.WEAKNESS);
        EFFECT_MAP.put("POISON", PotionEffectType.POISON);
        EFFECT_MAP.put("WITHER", PotionEffectType.WITHER);
        EFFECT_MAP.put("HEALTH_BOOST", PotionEffectType.HEALTH_BOOST);
        EFFECT_MAP.put("ABSORPTION", PotionEffectType.ABSORPTION);
        EFFECT_MAP.put("DOLPHINS_GRACE", PotionEffectType.DOLPHINS_GRACE);
        EFFECT_MAP.put("DARKNESS", PotionEffectType.DARKNESS);
    }

    private static final Map<Element, List<SupportAbility>> DEFAULT_SUPPORT = new HashMap<>();
    static {
        DEFAULT_SUPPORT.put(Element.FIRE, List.of(new SupportAbility("BUFF", "FIRE_RESISTANCE", 0, 8, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.WATER, List.of(new SupportAbility("BUFF", "WATER_BREATHING", 0, 8, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.AIR, List.of(new SupportAbility("BUFF", "SPEED", 0, 8, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.EARTH, List.of(new SupportAbility("BUFF", "RESISTANCE", 0, 8, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.SPIRIT, List.of(new SupportAbility("BUFF", "REGENERATION", 0, 10, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.UNDEAD, List.of(new SupportAbility("DEBUFF", "WEAKNESS", 0, 10, "MOBS", "")));
        DEFAULT_SUPPORT.put(Element.DRAGON, List.of(new SupportAbility("BUFF", "STRENGTH", 0, 12, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.BEAST, List.of(new SupportAbility("DEBUFF", "SLOWNESS", 0, 8, "MOBS", "")));
        DEFAULT_SUPPORT.put(Element.MAGICAL, List.of(new SupportAbility("BUFF", "NIGHT_VISION", 0, 8, "PLAYER", "")));
        DEFAULT_SUPPORT.put(Element.PLANT, List.of(new SupportAbility("BUFF", "REGENERATION", 0, 8, "ALLIES", "")));
    }

    public SummonManager(JavaPlugin plugin, CreatureRegistry registry, ChanequeCurseManager curseManager) {
        this.plugin = plugin;
        this.registry = registry;
        this.curseManager = curseManager;
        this.summonOwnerKey = new NamespacedKey(plugin, "summon_owner");
        this.summonCreatureKey = new NamespacedKey(plugin, "summon_creature");
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;

        if (curseManager.isCapturedBook(item)) {
            if (player.isSneaking()) {
                dismissSummon(player);
                event.setCancelled(true);
                return;
            }
            String creatureId = curseManager.getCapturedCreatureId(item);
            Creature creature = registry.get(creatureId);
            if (creature != null) {
                summonCreature(player, creature);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        dismissSummon(event.getPlayer());
    }

    @EventHandler
    public void onSummonDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        UUID ownerId = getSummonOwner(entity);
        if (ownerId != null) {
            Player owner = Bukkit.getPlayer(ownerId);
            if (owner != null) {
                owner.sendMessage(Component.text("Tu criatura invocada ha muerto!", NamedTextColor.RED));
            }
            cleanupSummon(entity.getUniqueId());
        }
    }

    @EventHandler
    public void onSummonDamage(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (getSummonOwner(entity) != null) {
            event.setDamage(event.getDamage() * 0.3);
        }
    }

    public void summonCreature(Player player, Creature creature) {
        dismissSummon(player);

        Location loc = player.getLocation().add(2, 0, 0);
        EntityType entityType = getEntityType(creature);
        Entity entity = player.getWorld().spawnEntity(loc, entityType);

        entity.setCustomName(Component.text(creature.displayName(), NamedTextColor.AQUA, TextDecoration.BOLD));
        entity.setCustomNameVisible(true);
        entity.setPersistent(true);
        entity.setInvulnerable(false);

        entity.getPersistentDataContainer().set(summonOwnerKey, PersistentDataType.STRING, player.getUniqueId().toString());
        entity.getPersistentDataContainer().set(summonCreatureKey, PersistentDataType.STRING, creature.id());

        if (entity instanceof Tameable tameable) {
            tameable.setOwner(player);
            tameable.setTamed(true);
        }

        if (entity instanceof Mob mob) {
            mob.setTarget(null);
            mob.setRemoveWhenFarAway(false);
            if (mob.getAttribute(Attribute.GENERIC_MAX_HEALTH) != null) {
                double hp = Math.max(20, creature.baseHp() * 0.4);
                mob.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(hp);
                mob.setHealth(hp);
            }
        }

        player.getWorld().spawnParticle(Particle.PORTAL, entity.getLocation().add(0, 1, 0), 30, 0.5, 1, 0.5, 0.1);

        player.sendMessage(Component.text("Has invocado a ", NamedTextColor.GOLD)
            .append(Component.text(creature.displayName(), NamedTextColor.AQUA))
            .append(Component.text("!", NamedTextColor.GOLD)));

        UUID taskId = startAuraTask(player, creature, entity);
        activeSummons.put(player.getUniqueId(), new SummonedCreature(player.getUniqueId(), creature.id(), entity.getUniqueId(), taskId));
    }

    public void dismissSummon(Player player) {
        SummonedCreature prev = activeSummons.remove(player.getUniqueId());
        if (prev != null) {
            cleanupSummon(prev.entityUUID);
            player.sendMessage(Component.text("La criatura ha regresado al libro.", NamedTextColor.GRAY));
        }
    }

    public boolean hasActiveSummon(Player player) {
        return activeSummons.containsKey(player.getUniqueId());
    }

    public void dismissAll() {
        new ArrayList<>(activeSummons.keySet()).forEach(uuid -> {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null) dismissSummon(p);
        });
    }

    private UUID startAuraTask(Player player, Creature creature, Entity entity) {
        List<SupportAbility> abilities = creature.supportAbilities();
        if (abilities.isEmpty()) {
            List<SupportAbility> fallback = DEFAULT_SUPPORT.get(creature.affinity());
            if (fallback == null) fallback = DEFAULT_SUPPORT.get(creature.type());
            if (fallback != null) abilities = fallback;
        }

        List<SupportAbility> finalAbilities = abilities;
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            if (!entity.isValid() || entity.isDead()) {
                cleanupSummon(entity.getUniqueId());
                return;
            }

            Player owner = Bukkit.getPlayer(player.getUniqueId());
            if (owner == null || !owner.isOnline()) {
                cleanupSummon(entity.getUniqueId());
                return;
            }

            Location eLoc = entity.getLocation();
            for (SupportAbility ab : finalAbilities) {
                PotionEffectType effectType = EFFECT_MAP.get(ab.effect().toUpperCase());
                if (effectType == null) continue;

                if (ab.isBuff()) {
                    if ("PLAYER".equalsIgnoreCase(ab.target()) || "ALLIES".equalsIgnoreCase(ab.target())) {
                        owner.addPotionEffect(new PotionEffect(effectType, 100, ab.amplifier(), true, false, true));
                        owner.getWorld().spawnParticle(Particle.HAPPY_VILLAGER,
                            owner.getLocation().add(0, 1, 0), 3, 0.3, 0.3, 0.3, 0.01);
                    }
                    if ("ALLIES".equalsIgnoreCase(ab.target())) {
                        for (Entity nearby : eLoc.getWorld().getNearbyEntities(eLoc, ab.radius(), ab.radius(), ab.radius())) {
                            if (nearby instanceof Player ally && !ally.equals(owner)
                                && ally.getLocation().distanceSquared(eLoc) <= ab.radius() * ab.radius()) {
                                ally.addPotionEffect(new PotionEffect(effectType, 100, ab.amplifier(), true, false, true));
                            }
                        }
                    }
                } else if (ab.isDebuff()) {
                    for (Entity nearby : eLoc.getWorld().getNearbyEntities(eLoc, ab.radius(), ab.radius(), ab.radius())) {
                        if (nearby instanceof Mob mob && !(nearby instanceof Player)) {
                            if (ab.target().equalsIgnoreCase("MOBS") || ab.target().equalsIgnoreCase("ALL")) {
                                mob.addPotionEffect(new PotionEffect(effectType, 100, ab.amplifier(), true, false, true));
                            }
                        }
                    }
                }
            }

            if (entity.getLocation().distanceSquared(owner.getLocation()) > 400) {
                entity.teleport(owner.getLocation().add(2, 0, 0));
            }

            if (entity instanceof Mob mob && mob.getTarget() == null) {
                if (mob.getLocation().distanceSquared(owner.getLocation()) > 25) {
                    mob.setTarget(null);
                    mob.getPathfinder().moveTo(owner.getLocation().add(1, 0, 1), 1.0);
                }
            }
        }, 20L, AURA_INTERVAL);

        return task.getTaskId();
    }

    private void cleanupSummon(UUID entityUUID) {
        Entity entity = Bukkit.getEntity(entityUUID);
        if (entity != null && entity.isValid()) entity.remove();

        activeSummons.entrySet().removeIf(e -> {
            if (e.getValue().entityUUID.equals(entityUUID)) {
                Bukkit.getScheduler().cancelTask(e.getValue().taskUUID);
                return true;
            }
            return false;
        });
    }

    private UUID getSummonOwner(Entity entity) {
        String ownerStr = entity.getPersistentDataContainer()
            .get(summonOwnerKey, PersistentDataType.STRING);
        if (ownerStr == null) return null;
        try { return UUID.fromString(ownerStr); }
        catch (IllegalArgumentException e) { return null; }
    }

    private EntityType getEntityType(Creature creature) {
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

    private record SummonedCreature(UUID playerUUID, String creatureId, UUID entityUUID, UUID taskUUID) {}
}

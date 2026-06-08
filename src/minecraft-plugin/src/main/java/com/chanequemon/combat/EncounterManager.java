package com.chanequemon.combat;

import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.model.Creature;
import com.chanequemon.model.CreatureInstance;
import com.chanequemon.registry.CreatureRegistry;
import org.bukkit.Location;
import org.bukkit.StructureType;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.util.stream.Collectors;

public class EncounterManager implements Listener {
    private final CreatureRegistry registry;
    private final CombatManager combatManager;
    private final JavaPlugin plugin;
    private final ChanequeCurseManager curseManager;

    private final Map<UUID, Long> lastEncounter = new HashMap<>();
    private final Map<UUID, StructureCacheEntry> structureCache = new HashMap<>();

    private static final long COOLDOWN_MS = 2000;
    private static final long STRUCTURE_CACHE_LIFETIME = 10000;
    private static final int STRUCTURE_SEARCH_RADIUS = 48;

    private static final Map<String, StructureType> STRUCTURE_MAP = new LinkedHashMap<>();
    static {
        STRUCTURE_MAP.put("ANCIENT_CITY", StructureType.ANCIENT_CITY);
        STRUCTURE_MAP.put("BASTION_REMNANT", StructureType.BASTION_REMNANT);
        STRUCTURE_MAP.put("BURIED_TREASURE", StructureType.BURIED_TREASURE);
        STRUCTURE_MAP.put("DESERT_PYRAMID", StructureType.DESERT_PYRAMID);
        STRUCTURE_MAP.put("END_CITY", StructureType.END_CITY);
        STRUCTURE_MAP.put("IGLOO", StructureType.IGLOO);
        STRUCTURE_MAP.put("JUNGLE_PYRAMID", StructureType.JUNGLE_PYRAMID);
        STRUCTURE_MAP.put("MINESHAFT", StructureType.MINESHAFT);
        STRUCTURE_MAP.put("NETHER_FORTRESS", StructureType.NETHER_FORTRESS);
        STRUCTURE_MAP.put("OCEAN_MONUMENT", StructureType.OCEAN_MONUMENT);
        STRUCTURE_MAP.put("OCEAN_RUIN", StructureType.OCEAN_RUIN);
        STRUCTURE_MAP.put("PILLAGER_OUTPOST", StructureType.PILLAGER_OUTPOST);
        STRUCTURE_MAP.put("RUINED_PORTAL", StructureType.RUINED_PORTAL);
        STRUCTURE_MAP.put("SHIPWRECK", StructureType.SHIPWRECK);
        STRUCTURE_MAP.put("STRONGHOLD", StructureType.STRONGHOLD);
        STRUCTURE_MAP.put("SWAMP_HUT", StructureType.SWAMP_HUT);
        STRUCTURE_MAP.put("TRAIL_RUINS", StructureType.TRAIL_RUINS);
        STRUCTURE_MAP.put("WOODLAND_MANSION", StructureType.WOODLAND_MANSION);
    }

    public EncounterManager(CreatureRegistry registry, CombatManager combatManager,
                            JavaPlugin plugin, ChanequeCurseManager curseManager) {
        this.registry = registry;
        this.combatManager = combatManager;
        this.plugin = plugin;
        this.curseManager = curseManager;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (event.getFrom().getBlockX() == event.getTo().getBlockX()
            && event.getFrom().getBlockZ() == event.getTo().getBlockZ()) return;
        if (combatManager.getSession(player) != null) return;

        boolean cursed = curseManager.hasCurse(player);
        UUID uid = player.getUniqueId();
        long now = System.currentTimeMillis();
        Long last = lastEncounter.get(uid);
        long cooldown = cursed ? curseManager.cooldownCursedMs() : COOLDOWN_MS;
        if (last != null && (now - last) < cooldown) return;

        Location loc = player.getLocation();
        String biome = loc.getBlock().getBiome().name();
        Set<String> nearbyStructures = getNearbyStructures(player, loc);

        List<Creature> candidates = new ArrayList<>();
        for (Creature c : registry.all()) {
            if (!c.hasSpawnConditions() && !cursed) continue;

            if (cursed) {
                if (checkTime(c, loc)) candidates.add(c);
                continue;
            }

            boolean biomeMatch = c.spawnBiomes().stream().anyMatch(b -> biome.contains(b.toUpperCase()));
            boolean structureMatch = c.spawnStructures().stream().anyMatch(nearbyStructures::contains);

            if (!biomeMatch && !structureMatch) continue;
            if (!checkTime(c, loc)) continue;
            candidates.add(c);
        }

        if (candidates.isEmpty()) return;

        int multiplier = cursed ? curseManager.encounterMultiplier() : 1;
        double totalWeight = 0;
        Map<Creature, Double> weights = new LinkedHashMap<>();
        for (Creature c : candidates) {
            boolean nearStructure = nearbyStructures.stream().anyMatch(s -> c.spawnStructures().contains(s));
            double w = c.spawnProbability() * multiplier * (nearStructure ? 2.5 : 1.0);
            totalWeight += w;
            weights.put(c, w);
        }

        if (totalWeight <= 0) return;

        double roll = Math.random() * totalWeight;
        double cumulative = 0;
        Creature selected = null;
        for (Map.Entry<Creature, Double> e : weights.entrySet()) {
            cumulative += e.getValue();
            if (roll < cumulative) { selected = e.getKey(); break; }
        }
        if (selected == null) return;

        lastEncounter.put(uid, now);
        CreatureInstance wild = new CreatureInstance(selected, true);
        CreatureInstance playerCreature = createStarterCreature(player);
        List<CreatureInstance> party = new ArrayList<>();
        party.add(playerCreature);

        combatManager.startBattle(player, playerCreature, party, wild);
    }

    private Set<String> getNearbyStructures(Player player, Location loc) {
        UUID uid = player.getUniqueId();
        long now = System.currentTimeMillis();
        StructureCacheEntry cached = structureCache.get(uid);
        if (cached != null && (now - cached.timestamp) < STRUCTURE_CACHE_LIFETIME) {
            if (cached.location.distanceSquared(loc) < 100) return cached.structures;
        }

        Set<String> found = new HashSet<>();
        World world = loc.getWorld();
        for (Map.Entry<String, StructureType> entry : STRUCTURE_MAP.entrySet()) {
            Location structLoc = world.locateNearestStructure(loc, entry.getValue(), STRUCTURE_SEARCH_RADIUS, false);
            if (structLoc != null && structLoc.distanceSquared(loc) <= STRUCTURE_SEARCH_RADIUS * STRUCTURE_SEARCH_RADIUS) {
                found.add(entry.getKey());
            }
        }

        structureCache.put(uid, new StructureCacheEntry(loc, found, now));
        return found;
    }

    private boolean checkTime(Creature c, Location loc) {
        String time = c.spawnTime();
        if (time == null || time.isBlank() || time.equals("ANY")) return true;
        long worldTime = loc.getWorld().getTime();
        boolean isNight = worldTime >= 13000 || worldTime < 1000;
        if (time.equals("NIGHT") && !isNight) return false;
        return !time.equals("DAY") || !isNight;
    }

    private CreatureInstance createStarterCreature(Player player) {
        Creature starter = registry.get("vampire_dracula");
        if (starter == null && !registry.all().isEmpty()) {
            starter = registry.all().iterator().next();
        }
        if (starter == null) {
            starter = new Creature("placeholder", "Espiritu Inicial", "placeholder", true,
                "SPIRIT", Map.of("hp", 60, "attack", 40, "defense", 40, "speed", 50),
                "SPIRIT", 0.2, "PORTAL", List.of(), Map.of(), List.of());
        }
        return new CreatureInstance(starter, false);
    }

    private record StructureCacheEntry(Location location, Set<String> structures, long timestamp) {}
}

package com.chanequemon.registry;

import com.chanequemon.model.Creature;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class CreatureRegistry {
    private final Map<String, Creature> creatures = new LinkedHashMap<>();
    private final List<Creature> spawnableList = new ArrayList<>();
    private final Random rng = new Random();

    private static final String[] DEFAULT_CREATURES = {
        "ammit.yml", "anansi.yml", "anubis.yml",
        "baba_yaga.yml", "banshee.yml", "basilisk.yml", "bastet.yml", "behemoth.yml",
        "black_shuck.yml",
        "carmilla.yml", "centaur.yml", "cerberus.yml", "cernunnos.yml", "chimera.yml",
        "draugr.yml",
        "fenrir.yml", "frankenstein.yml",
        "garuda.yml", "golem_judaism.yml", "griffin.yml",
        "hydra.yml",
        "ifrit.yml",
        "jiangshi.yml", "jormungandr.yml",
        "kappa.yml", "kelpie.yml", "kitsune.yml",
        "leshy.yml", "leviathan.yml", "lung_dragon.yml",
        "medusa.yml", "minotaur.yml", "mummy.yml",
        "naga.yml",
        "oni.yml",
        "pazuzu.yml", "phoenix.yml",
        "qilin.yml",
        "rakshasa.yml", "redcap.yml", "rusalka.yml",
        "selkie.yml", "shade_end.yml", "siren.yml", "spriggan.yml",
        "tengu.yml", "thunderbird.yml", "tiamat.yml", "tomte.yml", "tuatha.yml",
        "vampire.yml",
        "wendigo.yml", "werewolf.yml", "wraith.yml",
        "yuki_onna.yml"
    };

    public void loadAll(JavaPlugin plugin) {
        creatures.clear();
        File folder = new File(plugin.getDataFolder(), "creatures");
        folder.mkdirs();

        for (String name : DEFAULT_CREATURES) {
            File target = new File(folder, name);
            if (!target.exists()) {
                try {
                    plugin.saveResource("creatures/" + name, false);
                } catch (Exception e) {
                    // resource not bundled, skip silently
                }
            }
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".yml"));
        if (files == null) return;
        for (File f : files) {
            try {
                YamlConfiguration yaml = YamlConfiguration.loadConfiguration(f);
                String id = yaml.getString("id");
                if (id == null || id.isBlank()) continue;
                List<Map<String, Object>> supportMaps = (List<Map<String, Object>>) yaml.getList("support", List.of());
                Creature c = new Creature(
                    id,
                    yaml.getString("displayName", id),
                    yaml.getString("loreOrigin", ""),
                    yaml.getBoolean("publicDomainSource", true),
                    yaml.getString("type", "BEAST"),
                    yaml.getConfigurationSection("stats") != null ? yaml.getConfigurationSection("stats").getValues(false) : Map.of(),
                    yaml.getString("affinity", "BEAST"),
                    yaml.getDouble("captureRate", 0.1),
                    yaml.getString("summonAnimation", "PORTAL"),
                    (List<Map<String, Object>>) yaml.getList("moves", List.of()),
                    yaml.getConfigurationSection("spawnConditions") != null ? yaml.getConfigurationSection("spawnConditions").getValues(false) : Map.of(),
                    supportMaps
                );
                creatures.put(id, c);
                plugin.getLogger().info("  Loaded creature: " + id);
            } catch (Exception e) {
                plugin.getLogger().warning("  Failed to load " + f.getName() + ": " + e.getMessage());
            }
        }
        spawnableList.clear();
        for (Creature c : creatures.values()) {
            if (!c.spawnBiomes().isEmpty()) spawnableList.add(c);
        }
        plugin.getLogger().info("Loaded " + creatures.size() + " creatures (" + spawnableList.size() + " spawnable)");
    }

    public Creature get(String id) { return creatures.get(id); }
    public Collection<Creature> all() { return creatures.values(); }
    public List<Creature> spawnable() { return spawnableList; }

    public Creature randomSpawnable() {
        if (spawnableList.isEmpty()) return null;
        return spawnableList.get(rng.nextInt(spawnableList.size()));
    }
}

package com.chanequemon.player;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private final JavaPlugin plugin;
    private final Map<UUID, PlayerData> cache = new ConcurrentHashMap<>();
    private final File dataFolder;

    public PlayerDataManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        dataFolder.mkdirs();
    }

    public PlayerData get(Player player) {
        return cache.computeIfAbsent(player.getUniqueId(), uuid -> {
            PlayerData loaded = load(uuid);
            return loaded != null ? loaded : new PlayerData(uuid);
        });
    }

    public void save(Player player) {
        PlayerData data = cache.get(player.getUniqueId());
        if (data == null) return;
        saveToDisk(data);
    }

    public void saveAll() {
        for (PlayerData data : cache.values()) {
            saveToDisk(data);
        }
    }

    private PlayerData load(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return null;
        try {
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            PlayerData data = new PlayerData(uuid);
            data.setChanequeballs(yaml.getInt("chanequeballs", 5));
            List<String> captured = yaml.getStringList("captured");
            for (String id : captured) data.addCaptured(id);
            return data;
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to load player data for " + uuid + ": " + e.getMessage());
            return null;
        }
    }

    private void saveToDisk(PlayerData data) {
        try {
            File file = new File(dataFolder, data.playerId().toString() + ".yml");
            YamlConfiguration yaml = new YamlConfiguration();
            yaml.set("chanequeballs", data.chanequeballs());
            yaml.set("captured", new ArrayList<>(data.capturedIds()));
            yaml.save(file);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to save player data for " + data.playerId() + ": " + e.getMessage());
        }
    }
}

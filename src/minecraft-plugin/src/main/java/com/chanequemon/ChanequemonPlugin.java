package com.chanequemon;

import com.chanequemon.capture.CaptureManager;
import com.chanequemon.combat.CombatManager;
import com.chanequemon.combat.EncounterManager;
import com.chanequemon.enchant.ChanequeCurseManager;
import com.chanequemon.enchant.EnchantmentListener;
import com.chanequemon.player.PlayerDataManager;
import com.chanequemon.registry.CreatureRegistry;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChanequemonPlugin extends JavaPlugin {
    private CreatureRegistry creatureRegistry;
    private ChanequeCurseManager curseManager;
    private CaptureManager captureManager;
    private CombatManager combatManager;
    private EncounterManager encounterManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.creatureRegistry = new CreatureRegistry();
        creatureRegistry.loadAll(this);

        this.curseManager = new ChanequeCurseManager(this);
        this.captureManager = new CaptureManager(curseManager);
        this.combatManager = new CombatManager(captureManager, curseManager, this);
        this.encounterManager = new EncounterManager(creatureRegistry, combatManager, this, curseManager);
        this.playerDataManager = new PlayerDataManager(this);

        getServer().getPluginManager().registerEvents(encounterManager, this);
        getServer().getPluginManager().registerEvents(combatManager.gui(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentListener(curseManager, this), this);

        getCommand("chanequemon").setExecutor(new ChanequemonCommand(this));
        getCommand("chanequemon").setTabCompleter(new ChanequemonTabCompleter());

        getLogger().info("+---------------------------------------------------+");
        getLogger().info("| Chanequemon v" + getDescription().getVersion() + " loaded.                |");
        getLogger().info("| DEFENSIVE PUBLICATION — Public Domain (CC0)       |");
        getLogger().info("| github.com/eremosisima/Chanequemon                |");
        getLogger().info("+---------------------------------------------------+");
    }

    @Override
    public void onDisable() {
        if (playerDataManager != null) playerDataManager.saveAll();
        getLogger().info("Chanequemon disabled.");
    }

    public CreatureRegistry creatureRegistry() { return creatureRegistry; }
    public ChanequeCurseManager curseManager() { return curseManager; }
    public CaptureManager captureManager() { return captureManager; }
    public CombatManager combatManager() { return combatManager; }
    public PlayerDataManager playerDataManager() { return playerDataManager; }
}

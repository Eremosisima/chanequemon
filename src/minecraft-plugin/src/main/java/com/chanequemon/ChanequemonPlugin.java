package com.chanequemon;

import org.bukkit.plugin.java.JavaPlugin;

public final class ChanequemonPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Chanequemon loaded. This is a DEFENSIVE PUBLICATION.");
        getLogger().info("See: https://github.com/eremosisima/Chanequemon");
        getLogger().info("All systems and methods are dedicated to the public domain under CC0.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Chanequemon disabled.");
    }
}

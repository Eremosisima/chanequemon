package com.bestiario;

import org.bukkit.plugin.java.JavaPlugin;

public final class BestiarioPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("BestiarioMundial loaded. This is a DEFENSIVE PUBLICATION.");
        getLogger().info("See: https://github.com/[usuario]/BestiarioMundial");
        getLogger().info("All systems and methods are dedicated to the public domain under CC0.");
    }

    @Override
    public void onDisable() {
        getLogger().info("BestiarioMundial disabled.");
    }
}

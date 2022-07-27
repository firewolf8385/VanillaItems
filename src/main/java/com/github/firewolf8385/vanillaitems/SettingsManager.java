package com.github.firewolf8385.vanillaitems;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Allows easy access to plugin configuration
 * files. Stores spawn and arena locations.
 */
public class SettingsManager {
    private FileConfiguration config;
    private File configFile;

    public SettingsManager(VanillaItems plugin) {
        config = plugin.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
        plugin.saveConfig();
    }

    /**
     * Get the main configuration file.
     * @return Main configuration file.
     */
    public FileConfiguration getConfig() {
        return config;
    }
}
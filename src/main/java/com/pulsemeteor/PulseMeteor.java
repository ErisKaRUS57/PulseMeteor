package com.pulsemeteor;

import com.pulsemeteor.config.ConfigManager;
import com.pulsemeteor.modules.ModuleManager;
import com.pulsemeteor.events.EventManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

/**
 * Pulse Meteor - A premium PvP client-side mod combining Pulse Visuals
 * aesthetics with Meteor Client's modular utility system.
 *
 * Main initializer for the mod. Handles core system bootstrapping.
 */
public class PulseMeteor implements ModInitializer {
    public static final String MOD_ID = "pulsemeteor";
    public static final String MOD_NAME = "Pulse Meteor";
    public static final String MOD_VERSION = "1.0.0";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    private static PulseMeteor INSTANCE;
    private ModuleManager moduleManager;
    private ConfigManager configManager;
    private EventManager eventManager;

    public static PulseMeteor getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitialize() {
        INSTANCE = this;
        LOGGER.info("Initializing {} v{}", MOD_NAME, MOD_VERSION);

        // Initialize event system first
        this.eventManager = new EventManager();

        // Initialize the module system
        this.moduleManager = new ModuleManager();
        this.moduleManager.registerModules();

        // Initialize configuration
        File configDir = new File(FabricLoader.getInstance().getConfigDir().toFile(), MOD_ID);
        this.configManager = new ConfigManager(configDir);
        this.configManager.load();

        // Register shutdown hook for saving config
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Saving configuration...");
            configManager.save();
        }));

        LOGGER.info("{} initialized successfully!", MOD_NAME);
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }
}

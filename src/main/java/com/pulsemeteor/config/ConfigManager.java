package com.pulsemeteor.config;

import com.google.gson.*;
import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.*;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Configuration manager for saving/loading module settings to/from JSON files.
 * Uses Gson for serialization.
 */
public class ConfigManager {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Path configDir;
    private final Path configFile;
    private final Path keybindsFile;

    public ConfigManager(File configDir) {
        this.configDir = configDir.toPath();
        this.configFile = this.configDir.resolve("modules.json");
        this.keybindsFile = this.configDir.resolve("keybinds.json");

        try {
            Files.createDirectories(this.configDir);
        } catch (IOException e) {
            PulseMeteor.LOGGER.error("Failed to create config directory", e);
        }
    }

    /**
     * Save all module settings and keybinds to JSON files.
     */
    public void save() {
        saveModules();
        saveKeybinds();
        PulseMeteor.LOGGER.info("Configuration saved.");
    }

    /**
     * Load all module settings and keybinds from JSON files.
     */
    public void load() {
        loadModules();
        loadKeybinds();
    }

    private void saveModules() {
        JsonObject root = new JsonObject();

        for (Module module : PulseMeteor.getInstance().getModuleManager().getModules()) {
            JsonObject moduleObj = new JsonObject();
            moduleObj.addProperty("enabled", module.isEnabled());

            JsonObject settingsObj = new JsonObject();
            for (Setting<?> setting : module.getSettings()) {
                if (setting instanceof BooleanSetting b) {
                    settingsObj.addProperty(setting.getName(), b.getValue());
                } else if (setting instanceof NumberSetting n) {
                    settingsObj.addProperty(setting.getName(), n.getValue().doubleValue());
                } else if (setting instanceof ModeSetting m) {
                    settingsObj.addProperty(setting.getName(), m.getValue());
                }
            }
            moduleObj.add("settings", settingsObj);
            root.add(module.getName(), moduleObj);
        }

        try (Writer writer = Files.newBufferedWriter(configFile, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            PulseMeteor.LOGGER.error("Failed to save modules config", e);
        }
    }

    private void loadModules() {
        if (!Files.exists(configFile)) return;

        try (Reader reader = Files.newBufferedReader(configFile, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Module module : PulseMeteor.getInstance().getModuleManager().getModules()) {
                JsonObject moduleObj = root.getAsJsonObject(module.getName());
                if (moduleObj == null) continue;

                boolean enabled = moduleObj.get("enabled").getAsBoolean();
                if (enabled) module.enable();
                else module.disable();

                JsonObject settingsObj = moduleObj.getAsJsonObject("settings");
                if (settingsObj == null) continue;

                for (Setting<?> setting : module.getSettings()) {
                    JsonElement element = settingsObj.get(setting.getName());
                    if (element == null) continue;

                    if (setting instanceof BooleanSetting b) {
                        b.setValue(element.getAsBoolean());
                    } else if (setting instanceof NumberSetting n) {
                        n.setValue(element.getAsDouble());
                    } else if (setting instanceof ModeSetting m) {
                        m.setValue(element.getAsString());
                    }
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            PulseMeteor.LOGGER.error("Failed to load modules config", e);
        }
    }

    private void saveKeybinds() {
        JsonObject root = new JsonObject();
        for (Module module : PulseMeteor.getInstance().getModuleManager().getModules()) {
            root.addProperty(module.getName(), module.getKeyCode());
        }

        try (Writer writer = Files.newBufferedWriter(keybindsFile, StandardCharsets.UTF_8)) {
            GSON.toJson(root, writer);
        } catch (IOException e) {
            PulseMeteor.LOGGER.error("Failed to save keybinds", e);
        }
    }

    private void loadKeybinds() {
        if (!Files.exists(keybindsFile)) return;

        try (Reader reader = Files.newBufferedReader(keybindsFile, StandardCharsets.UTF_8)) {
            JsonObject root = GSON.fromJson(reader, JsonObject.class);
            if (root == null) return;

            for (Module module : PulseMeteor.getInstance().getModuleManager().getModules()) {
                JsonElement keyElement = root.get(module.getName());
                if (keyElement != null) {
                    module.setKeyCode(keyElement.getAsInt());
                }
            }
        } catch (IOException | JsonSyntaxException e) {
            PulseMeteor.LOGGER.error("Failed to load keybinds", e);
        }
    }

    /**
     * Reset all modules to default settings.
     */
    public void reset() {
        for (Module module : PulseMeteor.getInstance().getModuleManager().getModules()) {
            module.disable();
            for (Setting<?> setting : module.getSettings()) {
                setting.reset();
            }
            module.setKeyCode(GLFW.GLFW_KEY_UNKNOWN);
        }
        save();
    }
}

package com.pulsemeteor.modules;

import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import com.pulsemeteor.settings.Setting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for all Pulse Meteor modules.
 * Inspired by Meteor Client's module system.
 *
 * Each module has: name, description, category, keybind, settings,
 * and toggle state. Modules can be enabled/disabled and ticked each frame.
 */
public abstract class Module {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    private final String name;
    private final String description;
    private final Category category;
    private int keyCode = GLFW.GLFW_KEY_UNKNOWN;
    private boolean enabled = false;
    private boolean toggled = false;
    private final List<Setting<?>> settings = new ArrayList<>();

    private BooleanSetting enabledSetting;
    private BooleanSetting visibleSetting;

    public Module(String name, String description, Category category) {
        this.name = name;
        this.description = description;
        this.category = category;

        // Built-in settings
        this.enabledSetting = new BooleanSetting("Enabled", "Toggle this module", false);
        this.visibleSetting = new BooleanSetting("Visible", "Show in array list", true);
        addSetting(enabledSetting);
        addSetting(visibleSetting);
    }

    // ==================== Lifecycle Methods ====================

    /** Called when the module is enabled */
    public void onEnable() {}

    /** Called when the module is disabled */
    public void onDisable() {}

    /** Called every client tick */
    public void onTick(MinecraftClient client) {}

    /** Called every render frame */
    public void onRender(float tickDelta) {}

    /** Called when the world is rendered (for world overlays) */
    public void onWorldRender(float tickDelta) {}

    /** Called when GUI/HUD is rendered */
    public void onHudRender(float tickDelta) {}

    // ==================== Toggle Methods ====================

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            toggled = true;
            enabledSetting.setValue(true);
            onEnable();

            // Register this module's events
            com.pulsemeteor.PulseMeteor.LOGGER.debug("Enabled module: {}", name);
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            toggled = true;
            enabledSetting.setValue(false);
            onDisable();

            com.pulsemeteor.PulseMeteor.LOGGER.debug("Disabled module: {}", name);
        }
    }

    // ==================== Getters & Setters ====================

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public int getKeyCode() {
        return keyCode;
    }

    public void setKeyCode(int keyCode) {
        this.keyCode = keyCode;
    }

    public String getKeyBindString() {
        if (keyCode == GLFW.GLFW_KEY_UNKNOWN) return "NONE";
        try {
            return InputUtil.fromKeyCode(keyCode, -1).getLocalizedText().getString();
        } catch (Exception e) {
            return InputUtil.Type.KEYSYM.createFromCode(keyCode).getLocalizedText().getString();
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    public boolean isVisible() {
        return visibleSetting.getValue();
    }

    public void setVisible(boolean visible) {
        visibleSetting.setValue(visible);
    }

    // ==================== Settings ====================

    public void addSetting(Setting<?> setting) {
        settings.add(setting);
    }

    public List<Setting<?>> getSettings() {
        return settings;
    }

    @SuppressWarnings("unchecked")
    public <T extends Setting<?>> T getSetting(String name) {
        for (Setting<?> setting : settings) {
            if (setting.getName().equalsIgnoreCase(name)) {
                return (T) setting;
            }
        }
        return null;
    }

    public BooleanSetting getEnabledSetting() {
        return enabledSetting;
    }

    // ==================== Utility Methods ====================

    @Override
    public String toString() {
        return name;
    }

    /** Helper to add common boolean setting */
    protected BooleanSetting createBoolean(String name, String description, boolean defaultValue) {
        BooleanSetting setting = new BooleanSetting(name, description, defaultValue);
        addSetting(setting);
        return setting;
    }

    /** Helper to add common number setting */
    protected NumberSetting createNumber(String name, String description, Number defaultValue, Number min, Number max, Number step) {
        NumberSetting setting = new NumberSetting(name, description, defaultValue, min, max, step);
        addSetting(setting);
        return setting;
    }

    /** Helper to add common mode setting */
    protected ModeSetting createMode(String name, String description, String defaultValue, String... modes) {
        ModeSetting setting = new ModeSetting(name, description, defaultValue, modes);
        addSetting(setting);
        return setting;
    }
}

package com.pulsemeteor;

import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.gui.HUDEditor;
import com.pulsemeteor.hud.HUDManager;
import com.pulsemeteor.visuals.VisualsManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

/**
 * Client-side initializer for Pulse Meteor.
 * Sets up keybinds, GUI systems, visual enhancements, and HUD.
 */
@Environment(EnvType.CLIENT)
public class PulseMeteorClient implements ClientModInitializer {
    private static PulseMeteorClient INSTANCE;

    private ClickGUI clickGUI;
    private HUDEditor hudEditor;
    private HUDManager hudManager;
    private VisualsManager visualsManager;

    private KeyBinding openClickGUIKey;
    private KeyBinding openHUDEditorKey;
    private KeyBinding toggleModuleKey;

    public static PulseMeteorClient getInstance() {
        return INSTANCE;
    }

    @Override
    public void onInitializeClient() {
        INSTANCE = this;
        PulseMeteor.LOGGER.info("Initializing Pulse Meteor Client...");

        // Initialize HUD system
        this.hudManager = new HUDManager();
        this.hudManager.registerElements();

        // Initialize visual enhancements
        this.visualsManager = new VisualsManager();
        this.visualsManager.initialize();

        // Initialize GUI screens
        this.clickGUI = new ClickGUI();
        this.hudEditor = new HUDEditor();

        // Register keybinds
        registerKeybinds();

        // Register tick event for module updates
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);

        PulseMeteor.LOGGER.info("Pulse Meteor Client initialized!");
    }

    private void registerKeybinds() {
        openClickGUIKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pulsemeteor.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.pulsemeteor"
        ));

        openHUDEditorKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pulsemeteor.hudeditor",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_CONTROL,
                "category.pulsemeteor"
        ));

        toggleModuleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pulsemeteor.togglemodule",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "category.pulsemeteor"
        ));
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null) return;

        // Handle key presses
        while (openClickGUIKey.wasPressed()) {
            client.setScreen(clickGUI);
        }
        while (openHUDEditorKey.wasPressed()) {
            client.setScreen(hudEditor);
        }

        // Update all modules on tick
        PulseMeteor.getInstance().getModuleManager().getActiveModules()
                .forEach(module -> module.onTick(client));
    }

    public ClickGUI getClickGUI() {
        return clickGUI;
    }

    public HUDEditor getHUDEditor() {
        return hudEditor;
    }

    public HUDManager getHudManager() {
        return hudManager;
    }

    public VisualsManager getVisualsManager() {
        return visualsManager;
    }
}

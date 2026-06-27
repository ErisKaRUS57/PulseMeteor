package com.pulsemeteor.modules;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.modules.combat.*;
import com.pulsemeteor.modules.movement.*;
import com.pulsemeteor.modules.player.*;
import com.pulsemeteor.modules.render.*;
import com.pulsemeteor.modules.world.*;
import com.pulsemeteor.modules.utility.*;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Manages all modules in the mod.
 * Handles registration, keybinding, and provides access to modules by name/category.
 */
public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();

    /**
     * Register all modules. Called during mod initialization.
     */
    public void registerModules() {
        PulseMeteor.LOGGER.info("Registering modules...");

        // ── Combat Modules ──
        register(new KillAura());
        register(new CrystalAura());
        register(new AutoTotem());
        register(new BowAim());
        register(new TriggerBot());

        // ── Movement Modules ──
        register(new Flight());
        register(new Speed());
        register(new Step());
        register(new NoFall());
        register(new Jesus());
        register(new Scaffold());

        // ── Player Modules ──
        register(new AutoEat());
        register(new AutoMine());
        register(new AutoFarm());
        register(new ChestAura());

        // ── Render Modules ──
        register(new ESP());
        register(new XRay());
        register(new Tracers());
        register(new Chams());
        register(new StorageESP());

        // ── World Modules ──
        register(new FreeCam());
        register(new Tunnel());

        // ── Utility Modules ──
        register(new AutoReconnect());
        register(new AntiAFK());
        register(new InventoryCleaner());

        // Register keybind check on tick
        ClientTickEvents.END_CLIENT_TICK.register(this::checkKeybinds);

        PulseMeteor.LOGGER.info("Registered {} modules", modules.size());
    }

    /**
     * Register a single module.
     */
    public void register(Module module) {
        modules.add(module);
    }

    /**
     * Get all registered modules.
     */
    public List<Module> getModules() {
        return modules;
    }

    /**
     * Get all modules in a specific category.
     */
    public List<Module> getModulesInCategory(Category category) {
        return modules.stream()
                .filter(module -> module.getCategory() == category)
                .collect(Collectors.toList());
    }

    /**
     * Get a module by its name (case-insensitive).
     */
    public Module getModule(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    /**
     * Get module by class type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Module> T getModule(Class<T> clazz) {
        return (T) modules.stream()
                .filter(module -> module.getClass() == clazz)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get all currently enabled (active) modules.
     */
    public List<Module> getActiveModules() {
        return modules.stream()
                .filter(Module::isEnabled)
                .collect(Collectors.toList());
    }

    /**
     * Get modules sorted by name for array list rendering.
     */
    public List<Module> getModulesForArrayList() {
        return modules.stream()
                .filter(Module::isVisible)
                .sorted(Comparator.comparing(Module::getName))
                .collect(Collectors.toList());
    }

    /**
     * Check keyboard keybinds each tick.
     */
    private void checkKeybinds(MinecraftClient client) {
        if (client.currentScreen != null) return;

        for (Module module : modules) {
            if (module.getKeyCode() != GLFW.GLFW_KEY_UNKNOWN) {
                if (InputUtil.isKeyPressed(mc.getWindow().getHandle(), module.getKeyCode())) {
                    // Use a toggle cooldown to prevent multiple toggles per press
                    module.toggle();
                }
            }
        }
    }
}

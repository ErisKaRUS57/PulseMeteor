package com.pulsemeteor.hud;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.hud.elements.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all HUD elements, including rendering and dragging.
 */
public class HUDManager {
    private final List<HUDElement> elements = new ArrayList<>();
    private final List<HUDElement> screenRelativeElements = new ArrayList<>();

    public void registerElements() {
        PulseMeteor.LOGGER.info("Registering HUD elements...");

        register(new WatermarkHUD());
        register(new ArmorHUD());
        register(new HealthHUD());
        register(new CombatInfoHUD());
        register(new CoordinatesHUD());
        register(new PotionEffectsHUD());

        PulseMeteor.LOGGER.info("Registered {} HUD elements", elements.size());
    }

    public void register(HUDElement element) {
        elements.add(element);
    }

    public void render(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        for (HUDElement element : elements) {
            if (element.isVisible()) {
                element.render(context, tickDelta);
            }
        }
    }

    public List<HUDElement> getElements() {
        return elements;
    }

    public HUDElement getElement(String name) {
        return elements.stream()
                .filter(e -> e.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}

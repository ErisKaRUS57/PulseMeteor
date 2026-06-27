package com.pulsemeteor.hud.elements;

import com.pulsemeteor.hud.HUDElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.Color;

/**
 * HealthHUD - displays player health, absorption, and hunger information.
 * Clean, minimalistic design with Pulse Visuals style.
 */
public class HealthHUD extends HUDElement {
    public HealthHUD() {
        super("Health", 4, 55, 100, 16);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        PlayerEntity player = mc.player;

        // Health
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        int healthColor;
        if (health > 15) healthColor = 0xFF00FF00;
        else if (health > 8) healthColor = 0xFFFFAA00;
        else healthColor = 0xFFFF0000;

        String healthText = String.format("❤ %.0f/%.0f", health, maxHealth);
        context.drawText(mc.textRenderer, healthText, x, y, healthColor, true);

        // Absorption
        float absorption = player.getAbsorptionAmount();
        if (absorption > 0) {
            String absText = String.format("⬡ %.0f", absorption);
            int absX = x + mc.textRenderer.getWidth(healthText) + 6;
            context.drawText(mc.textRenderer, absText, absX, y, 0xFFFFFF55, true);
        }

        // Food level
        int foodLevel = player.getHungerManager().getFoodLevel();
        int foodColor = foodLevel > 10 ? 0xFFCC8833 : 0xFFFF4444;
        String foodText = String.format("🍗 %d", foodLevel);
        context.drawText(mc.textRenderer, foodText, x, y + 10, foodColor, true);

        // Update width
        int textWidth = mc.textRenderer.getWidth(healthText) + 6;
        if (absorption > 0) {
            textWidth += mc.textRenderer.getWidth(String.format("⬡ %.0f", absorption)) + 6;
        }
        this.width = textWidth;
        this.height = 24;
    }
}

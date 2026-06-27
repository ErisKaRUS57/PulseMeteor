package com.pulsemeteor.hud.elements;

import com.pulsemeteor.hud.HUDElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;

import java.util.Collection;

/**
 * PotionEffectsHUD - displays active potion effects with duration.
 */
public class PotionEffectsHUD extends HUDElement {
    public PotionEffectsHUD() {
        super("Potion Effects", 4, 135, 100, 50);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        Collection<StatusEffectInstance> effects = mc.player.getStatusEffects();
        if (effects.isEmpty()) {
            this.height = 0;
            return;
        }

        int yOffset = y;

        for (StatusEffectInstance effect : effects) {
            String name = effect.getEffectType().getName().getString();
            String duration = StatusEffectUtil.getDurationText(effect, 1.0f).getString();
            int amplifier = effect.getAmplifier() + 1;

            String text = String.format("%s %d [%s]", name, amplifier, duration);
            int color = effect.getEffectType().getColor();

            context.drawText(mc.textRenderer, text, x, yOffset, color, true);

            // Duration bar
            float progress = 1.0f - (effect.getDuration() / (float) effect.getDuration());
            int barWidth = mc.textRenderer.getWidth(text);
            context.fill(x, yOffset + 10, x + barWidth, yOffset + 12, 0xFF333333);
            context.fill(x, yOffset + 10, x + (int) (barWidth * progress), yOffset + 12,
                    color | 0xFF000000);

            yOffset += 16;
        }

        this.width = 120;
        this.height = yOffset - y;
    }
}

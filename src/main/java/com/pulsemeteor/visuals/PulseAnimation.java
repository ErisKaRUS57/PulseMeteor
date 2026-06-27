package com.pulsemeteor.visuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * PulseAnimation - renders subtle pulsing glow effects on selected GUI elements
 * or around the crosshair for the Pulse Visuals aesthetic.
 */
public class PulseAnimation {
    private boolean enabled = true;
    private float time = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Update the animation time.
     */
    public void update(float tickDelta) {
        time += tickDelta * 0.05f;
    }

    /**
     * Get the current pulse value (oscillates between 0.3 and 1.0).
     */
    public float getPulseValue() {
        return (float) (Math.sin(time * 3.0f) * 0.35f + 0.65f);
    }

    /**
     * Get the current pulse alpha for rendering.
     */
    public int getPulseAlpha(int baseAlpha) {
        return (int) (baseAlpha * getPulseValue());
    }

    /**
     * Render a pulse glow effect around the crosshair.
     */
    public void renderCrosshairPulse(DrawContext context, int centerX, int centerY) {
        if (!enabled) return;

        float pulse = getPulseValue();
        int baseSize = 15;
        int size = (int) (baseSize + (1.0f - pulse) * 5);
        int alpha = (int) (pulse * 60);

        // Draw outer ring
        int color = 0xFFFF4444 | (alpha << 24);
        context.fill(centerX - size, centerY - 1, centerX + size, centerY + 1, color);
        context.fill(centerX - 1, centerY - size, centerX + 1, centerY + size, color);

        // Inner dot
        int dotSize = 2;
        context.fill(centerX - dotSize, centerY - dotSize, centerX + dotSize, centerY + dotSize,
                0xFFFF6666 | (alpha << 24));
    }
}

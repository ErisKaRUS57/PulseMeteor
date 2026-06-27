package com.pulsemeteor.hud.elements;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.hud.HUDElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Watermark HUD - displays the mod name and version in the top-left corner.
 * Styled with Pulse Visuals aesthetic.
 */
public class WatermarkHUD extends HUDElement {
    private static final int BG_COLOR = 0x80000000;
    private static final int TEXT_COLOR = 0xFF00FFAA;

    public WatermarkHUD() {
        super("Watermark", 4, 4, 120, 12);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        String text = String.format("%s v%s", PulseMeteor.MOD_NAME, PulseMeteor.MOD_VERSION);
        int textWidth = mc.textRenderer.getWidth(text);
        this.width = textWidth + 8;

        // Background
        context.fill(x, y, x + width, y + height, BG_COLOR);

        // Text with neon green accent
        context.drawText(mc.textRenderer, text, x + 4, y + 2, TEXT_COLOR, false);
    }
}

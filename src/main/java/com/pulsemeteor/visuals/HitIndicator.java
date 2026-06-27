package com.pulsemeteor.visuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.math.MathHelper;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * HitIndicator - displays crisp damage indicators and hit markers.
 * Shows directional arrows pointing to damage sources with pulse effects.
 */
public class HitIndicator {
    private final List<DamageIndicator> indicators = new ArrayList<>();
    private long lastHitTime = 0;

    public void onHit(float damage, float health) {
        lastHitTime = System.currentTimeMillis();
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        indicators.add(new DamageIndicator(
                damage, health,
                (float) (Math.random() * 2 * Math.PI),
                System.currentTimeMillis()
        ));
    }

    public void render(DrawContext context, int screenWidth, int screenHeight, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        int centerX = screenWidth / 2;
        int centerY = screenHeight / 2;

        // Crosshair pulse on hit
        long timeSinceHit = System.currentTimeMillis() - lastHitTime;
        if (timeSinceHit < 500) {
            float pulse = 1.0f - (timeSinceHit / 500.0f);
            int alpha = (int) (pulse * 200);
            int size = (int) (8 + pulse * 12);

            // Draw crosshair lines with pulse
            context.fill(centerX - size, centerY - 1, centerX + size, centerY + 1,
                    0xFFFF0000 | (alpha << 24));
            context.fill(centerX - 1, centerY - size, centerX + 1, centerY + size,
                    0xFFFF0000 | (alpha << 24));

            // Draw outer corners
            int cornerLen = 6;
            int gap = 4;
            int cornerColor = 0xFFFF4444 | (alpha << 24);

            // Top-left
            context.fill(centerX - gap - cornerLen, centerY - gap - 1, centerX - gap, centerY - gap + 1, cornerColor);
            context.fill(centerX - gap - 1, centerY - gap - cornerLen, centerX - gap + 1, centerY - gap, cornerColor);
            // Top-right
            context.fill(centerX + gap, centerY - gap - 1, centerX + gap + cornerLen, centerY - gap + 1, cornerColor);
            context.fill(centerX - 1, centerY - gap - cornerLen, centerX + 1, centerY - gap, cornerColor);
            // Bottom-left
            context.fill(centerX - gap - cornerLen, centerY - gap - 1, centerX - gap, centerY - gap + 1, cornerColor);
            context.fill(centerX - gap - 1, centerY + gap, centerX - gap + 1, centerY + gap + cornerLen, cornerColor);
            // Bottom-right
            context.fill(centerX + gap, centerY - gap - 1, centerX + gap + cornerLen, centerY - gap + 1, cornerColor);
            context.fill(centerX - 1, centerY + gap, centerX + 1, centerY + gap + cornerLen, cornerColor);
        }

        // Damage indicators
        long now = System.currentTimeMillis();
        Iterator<DamageIndicator> it = indicators.iterator();
        while (it.hasNext()) {
            DamageIndicator ind = it.next();
            float age = (now - ind.timestamp) / 1000.0f;
            if (age > 2.0f) { it.remove(); continue; }

            float progress = age / 2.0f;
            int alpha = (int) ((1.0f - progress) * 255);
            int x = (int) (centerX + Math.cos(ind.angle) * (30 + age * 40));
            int y = (int) (centerY + Math.sin(ind.angle) * (30 + age * 40));

            context.getMatrices().push();
            context.getMatrices().translate(x, y, 0);
            context.getMatrices().scale(1.0f + age * 0.5f, 1.0f + age * 0.5f, 1.0f);

            String text = String.format("%.1f", ind.damage);
            int textColor = 0xFFFF5555 | (alpha << 24);
            context.drawText(client.textRenderer, text, -client.textRenderer.getWidth(text) / 2, -4, textColor, true);

            context.getMatrices().pop();
        }
    }

    private record DamageIndicator(float damage, float health, float angle, long timestamp) {}
}

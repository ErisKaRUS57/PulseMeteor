package com.pulsemeteor.visuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * Nametags - renders colored nametags and health bars above players' heads
 * with a sleek, modern font and Pulse Visuals aesthetic.
 */
public class Nametags {
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Render custom nametags for players.
     * Called from a mixin in LivingEntityRenderer.
     */
    public void renderNametag(LivingEntity entity, float tickDelta, MatrixStack matrices,
                               RenderLayer renderLayer, int light, int overlay) {
        if (!enabled) return;
        if (!(entity instanceof PlayerEntity player)) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (player == client.player) return;

        TextRenderer textRenderer = client.textRenderer;
        double distance = client.player.distanceTo(player);
        double scale = Math.max(0.1, Math.min(1.0, 10.0 / distance));

        matrices.push();
        matrices.translate(0.0, entity.getHeight() + 0.5, 0.0);
        matrices.scale((float) scale * 0.025f, (float) (-scale) * 0.025f, (float) scale * 0.025f);
        matrices.multiply(client.gameRenderer.getCamera().getRotation());

        // Background
        String name = player.getDisplayName().getString();
        float nameWidth = textRenderer.getWidth(name);
        float bgPadding = 4.0f;
        float bgWidth = nameWidth + bgPadding * 2;
        float bgHeight = 22.0f;

        // Background rect
        fillRect(matrices, -bgWidth / 2, -10, bgWidth, bgHeight, new Color(0, 0, 0, 120));

        // Name text with team color
        int nameColor = 0xFFFFFFFF;
        if (player.getScoreboardTeam() != null) {
            var teamColor = player.getScoreboardTeam().getColor();
            if (teamColor != null) {
                nameColor = teamColor.getColorValue() != null ? teamColor.getColorValue() : 0xFFFFFFFF;
            }
        }

        textRenderer.draw(matrices, name, -nameWidth / 2, -8, nameColor, true);

        // Health bar
        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercent = Math.min(1.0f, health / maxHealth);

        int barWidth = (int) nameWidth;
        int barX = -barWidth / 2;
        int barY = 4;
        int barHeight = 3;

        // Background bar
        fillRect(matrices, barX, barY, barWidth, barHeight, new Color(60, 60, 60, 150));

        // Health bar fill
        Color healthColor;
        if (healthPercent > 0.5f) {
            healthColor = new Color(
                    (int) ((1.0f - healthPercent) * 2 * 255),
                    255,
                    50, 200);
        } else {
            healthColor = new Color(
                    255,
                    (int) (healthPercent * 2 * 255),
                    50, 200);
        }

        fillRect(matrices, barX, barY, (int) (barWidth * healthPercent), barHeight, healthColor);

        // Health text
        String healthText = String.format("%.0f", health);
        textRenderer.draw(matrices, healthText, barX + barWidth + 2, barY - 1, 0xFFFFFFFF, true);

        matrices.pop();
    }

    private void fillRect(MatrixStack matrices, int x, int y, int width, int height, Color color) {
        Matrix4f matrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);

        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        buffer.vertex(matrix, x, y, 0).color(r, g, b, a);
        buffer.vertex(matrix, x + width, y, 0).color(r, g, b, a);
        buffer.vertex(matrix, x + width, y + height, 0).color(r, g, b, a);
        buffer.vertex(matrix, x, y + height, 0).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
    }
}

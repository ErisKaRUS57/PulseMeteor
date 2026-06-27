package com.pulsemeteor.hud.elements;

import com.pulsemeteor.hud.HUDElement;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;

/**
 * CoordinatesHUD - displays the player's coordinates and facing direction.
 */
public class CoordinatesHUD extends HUDElement {
    public CoordinatesHUD() {
        super("Coordinates", 4, 100, 120, 30);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        BlockPos pos = mc.player.getBlockPos();
        String coords = String.format("XYZ: %d %d %d", pos.getX(), pos.getY(), pos.getZ());

        // Direction
        String direction = getFacingDirection();
        String speed = String.format("%.1f", mc.player.getVelocity().horizontalLength() * 20);

        context.drawText(mc.textRenderer, coords, x, y, 0xFFFFFFFF, true);
        context.drawText(mc.textRenderer, direction, x, y + 10, 0xFFAAAAAA, true);
        context.drawText(mc.textRenderer, speed + " bps", x, y + 20, 0xFF888888, true);

        // Update width
        int w1 = mc.textRenderer.getWidth(coords);
        int w2 = mc.textRenderer.getWidth(direction);
        int w3 = mc.textRenderer.getWidth(speed + " bps");
        this.width = Math.max(w1, Math.max(w2, w3));
        this.height = 30;
    }

    private String getFacingDirection() {
        if (mc.player == null) return "N/A";
        float yaw = mc.player.getYaw();
        if (yaw < 0) yaw += 360;

        if (yaw >= 315 || yaw < 45) return "South (Z+)";
        if (yaw >= 45 && yaw < 135) return "West (X-)";
        if (yaw >= 135 && yaw < 225) return "North (Z-)";
        return "East (X+)";
    }
}

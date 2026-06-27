package com.pulsemeteor.visuals;

import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * BlockOverlay - renders neon/pulse animations on target blocks
 * (obsidian, beds, end crystals) for PvP visibility.
 */
public class BlockOverlay {
    private boolean enabled = true;
    private float pulseTime = 0;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void render(float tickDelta) {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null || client.interactionManager == null) return;

        pulseTime += tickDelta * 0.05f;

        // Get target block the player is looking at
        if (client.crosshairTarget == null) return;
        if (client.crosshairTarget.getType() != net.minecraft.util.hit.HitResult.Type.BLOCK) return;

        BlockPos pos = ((net.minecraft.util.hit.BlockHitResult) client.crosshairTarget).getBlockPos();
        var state = client.world.getBlockState(pos);

        // Only overlay on PvP-relevant blocks
        boolean isPvPBlock = state.isOf(Blocks.OBSIDIAN)
                || state.isOf(Blocks.BEDROCK)
                || state.isOf(Blocks.RED_BED)
                || state.isOf(Blocks.END_STONE)
                || state.isOf(Blocks.CRYING_OBSIDIAN);

        if (!isPvPBlock) return;

        Vec3d camera = client.gameRenderer.getCamera().getPos();
        Box box = new Box(pos).offset(-camera.x, -camera.y, -camera.z);

        // Pulse animation
        float pulse = (float) (Math.sin(pulseTime * 3.0f) * 0.3f + 0.7f);
        Color color = new Color(
                (int) (255 * pulse),
                50,
                (int) (255 * (1.0f - pulse) * 0.5f),
                120
        );

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);

        // Draw the overlay box
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        // Bottom face
        buffer.vertex(box.minX, box.minY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.minY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.minY, box.maxZ).color(r, g, b, a);
        buffer.vertex(box.minX, box.minY, box.maxZ).color(r, g, b, a);

        // Top face
        buffer.vertex(box.minX, box.maxY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.maxY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.maxY, box.maxZ).color(r, g, b, a);
        buffer.vertex(box.minX, box.maxY, box.maxZ).color(r, g, b, a);

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }
}

package com.pulsemeteor.modules.render;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * Tracers - draws lines from player to entities.
 */
public class Tracers extends Module {
    private final BooleanSetting players;
    private final BooleanSetting mobs;
    private final NumberSetting lineWidth;

    public Tracers() {
        super("Tracers", "Draw lines to entities", Category.RENDER);
        this.players = createBoolean("Players", "Trace to players", true);
        this.mobs = createBoolean("Mobs", "Trace to mobs", false);
        this.lineWidth = createNumber("Line Width", "Width of tracer lines", 2.0, 0.5, 5.0, 0.5);
    }

    @Override
    public void onWorldRender(float tickDelta) {
        if (mc.world == null || mc.player == null) return;

        Vec3d cameraPos = mc.gameRenderer.getCamera().getPos();

        // Use tessellator for drawing lines
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINES,
                VertexFormats.POSITION_COLOR);

        for (Entity entity : mc.world.getEntities()) {
            if (!shouldTrace(entity)) continue;

            Vec3d entityPos = entity.getLerpedPos(tickDelta)
                    .add(0, entity.getHeight() / 2.0, 0)
                    .subtract(cameraPos);

            Vec3d playerPos = mc.player.getLerpedPos(tickDelta)
                    .add(0, mc.player.getEyeHeight(mc.player.getPose()), 0)
                    .subtract(cameraPos);

            Color color = getEntityColor(entity);

            buffer.vertex(playerPos.x, playerPos.y, playerPos.z)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            buffer.vertex(entityPos.x, entityPos.y, entityPos.z)
                    .color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());

        // Restore state
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    private boolean shouldTrace(Entity entity) {
        if (entity == mc.player) return false;
        if (entity instanceof PlayerEntity && players.isEnabled()) return true;
        if (entity instanceof Monster && mobs.isEnabled()) return true;
        return false;
    }

    private Color getEntityColor(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return new Color(255, 50, 50, 200);
        } else if (entity instanceof Monster) {
            return new Color(255, 100, 0, 200);
        }
        return new Color(255, 255, 255, 200);
    }
}

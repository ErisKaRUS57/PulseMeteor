package com.pulsemeteor.visuals;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * Trajectories - shows the predicted path of projectiles (pearls, arrows, eggs).
 * Draws a dotted line showing where the projectile will travel.
 */
public class Trajectories {
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void render(float tickDelta) {
        if (!enabled) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        ItemStack mainHand = client.player.getMainHandStack();
        if (!isThrowable(mainHand.getItem())) return;

        Vec3d camera = client.gameRenderer.getCamera().getPos();
        Vec3d start = client.player.getEyePos();
        Vec3d look = client.player.getRotationVec(tickDelta);

        // Simulate trajectory
        double velocity = 1.5;
        double gravity = 0.05;
        Vec3d vel = look.multiply(velocity);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.DEBUG_LINE_STRIP,
                VertexFormats.POSITION_COLOR);

        Vec3d current = start;
        Color trajColor = new Color(255, 255, 255, 150);

        for (int i = 0; i < 200; i++) {
            Vec3d next = current.add(vel);
            vel = vel.add(0, -gravity, 0);
            vel = vel.multiply(0.99);

            // Check collision
            HitResult hit = ProjectileUtil.getCollision(client.player, current, next,
                    new Box(current, next).expand(0.3), entity -> true, 0f);
            if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                next = hit.getPos();
                Vec3d drawPos = current.subtract(camera);
                buffer.vertex(drawPos.x, drawPos.y, drawPos.z)
                        .color(trajColor.getRed(), trajColor.getGreen(), trajColor.getBlue(), trajColor.getAlpha());

                Vec3d endPos = next.subtract(camera);
                buffer.vertex(endPos.x, endPos.y, endPos.z)
                        .color(trajColor.getRed(), trajColor.getGreen(), trajColor.getBlue(), 50);
                break;
            }

            // Draw point every few iterations
            if (i % 3 == 0) {
                Vec3d drawPos = current.subtract(camera);
                buffer.vertex(drawPos.x, drawPos.y, drawPos.z)
                        .color(trajColor.getRed(), trajColor.getGreen(), trajColor.getBlue(), trajColor.getAlpha());
            }

            current = next;
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    private boolean isThrowable(Item item) {
        return item instanceof EnderPearlItem
                || item instanceof BowItem
                || item instanceof CrossbowItem
                || item instanceof EggItem
                || item instanceof SnowballItem
                || item instanceof ExperienceBottleItem
                || item instanceof TridentItem;
    }
}

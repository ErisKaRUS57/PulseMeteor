package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.Vec3d;

/**
 * Speed - increase walking/sprinting speed with multiple modes.
 */
public class Speed extends Module {
    private final ModeSetting mode;
    private final NumberSetting speed;

    private int ticks = 0;
    private int stage = 0;

    public Speed() {
        super("Speed", "Increase movement speed", Category.MOVEMENT);
        this.mode = createMode("Mode", "Speed mode", "Strafe", "Strafe", "BHop", "YPort");
        this.speed = createNumber("Speed", "Speed multiplier", 1.5, 1.0, 5.0, 0.1);
    }

    @Override
    public void onEnable() {
        stage = 0;
        ticks = 0;
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (!client.player.isOnGround() && mode.isMode("BHop")) return;

        ticks++;
        double baseSpeed = 0.2873;
        double mult = speed.getValueDouble();

        if (mode.isMode("Strafe")) {
            // Strafe speed - preserve velocity direction
            Vec3d vel = client.player.getVelocity();
            float yaw = client.player.getYaw();

            if (client.options.forwardKey.isPressed() || client.options.backKey.isPressed()
                    || client.options.leftKey.isPressed() || client.options.rightKey.isPressed()) {
                float forward = client.options.forwardKey.isPressed() ? 1 : (client.options.backKey.isPressed() ? -1 : 0);
                float strafe = client.options.rightKey.isPressed() ? 1 : (client.options.leftKey.isPressed() ? -1 : 0);
                double speedVal = baseSpeed * mult;

                double motionX = (Math.cos(Math.toRadians(yaw + 90)) * strafe + Math.cos(Math.toRadians(yaw)) * forward) * speedVal
                        - Math.sin(Math.toRadians(yaw)) * forward * speedVal;
                double motionZ = (Math.sin(Math.toRadians(yaw + 90)) * strafe + Math.sin(Math.toRadians(yaw)) * forward) * speedVal
                        + Math.cos(Math.toRadians(yaw)) * forward * speedVal;

                // Apply speed effect boost
                if (client.player.hasStatusEffect(StatusEffects.SPEED)) {
                    int amp = client.player.getStatusEffect(StatusEffects.SPEED).getAmplifier();
                    motionX *= (1 + 0.2 * (amp + 1));
                    motionZ *= (1 + 0.2 * (amp + 1));
                }

                client.player.setVelocity(motionX, vel.y, motionZ);
            }
        } else if (mode.isMode("BHop")) {
            // BHop mode - bunny hop
            if (client.player.isOnGround() && (client.options.forwardKey.isPressed()
                    || client.options.backKey.isPressed()
                    || client.options.leftKey.isPressed()
                    || client.options.rightKey.isPressed())) {
                Vec3d vel = client.player.getVelocity();
                double speedVal = baseSpeed * mult * 1.3f;
                client.player.setVelocity(vel.x * speedVal, 0.42f, vel.z * speedVal);
                client.player.setSprinting(true);
            }
        } else if (mode.isMode("YPort")) {
            // YPort mode - vertical boost
            if (ticks % 4 == 0) {
                Vec3d vel = client.player.getVelocity();
                client.player.setVelocity(vel.x, 0.42, vel.z);
            }
        }
    }
}

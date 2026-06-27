package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;

/**
 * Jesus - walk on water/lava as if it were solid ground.
 */
public class Jesus extends Module {
    private final ModeSetting mode;

    public Jesus() {
        super("Jesus", "Walk on water and lava", Category.MOVEMENT);
        this.mode = createMode("Mode", "Jesus mode", "Solid", "Solid", "Velocity", "NCP");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (mode.isMode("Solid")) {
            // Check if player is in water/lava
            if (client.player.isTouchingWater() || client.player.isInLava()) {
                client.player.setVelocity(client.player.getVelocity().x, 0.0, client.player.getVelocity().z);

                // Bypass sinking
                if (client.player.isOnGround()) {
                    client.player.setVelocity(client.player.getVelocity().add(0, 0.08, 0));
                }

                // Prevent going down
                if (client.options.sneakKey.isPressed()) {
                    client.player.setVelocity(client.player.getVelocity().add(0, -0.1, 0));
                }
            }
        } else if (mode.isMode("NCP")) {
            if (client.player.isTouchingWater()) {
                client.player.setVelocity(client.player.getVelocity().x, 0.0, client.player.getVelocity().z);
                client.player.setOnGround(true);
            }
        } else if (mode.isMode("Velocity")) {
            if (client.player.isTouchingWater() || client.player.isInLava()) {
                if (client.player.getVelocity().y < 0) {
                    client.player.setVelocity(client.player.getVelocity().x, 0.0, client.player.getVelocity().z);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            // Reset water movement
        }
    }
}

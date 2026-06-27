package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;

/**
 * Step - automatically step up full blocks.
 */
public class Step extends Module {
    private final NumberSetting height;

    public Step() {
        super("Step", "Automatically step up full blocks", Category.MOVEMENT);
        this.height = createNumber("Height", "Max step height in blocks", 1.0, 0.5, 3.0, 0.5);
    }

    @Override
    public void onEnable() {
        if (mc.player != null) {
            mc.player.setNoGravity(false);
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (!client.player.isOnGround()) return;
        if (!client.player.horizontalCollision) return;

        float stepHeight = height.getValueFloat();
        client.player.stepHeight = stepHeight;

        // For higher steps (2+), use a velocity boost
        if (stepHeight >= 2.0f && client.player.horizontalCollision) {
            if (client.options.forwardKey.isPressed()) {
                client.player.setVelocity(client.player.getVelocity().x, 0.42, client.player.getVelocity().z);
            }
        }
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.stepHeight = 0.6f; // Reset to default
        }
    }
}

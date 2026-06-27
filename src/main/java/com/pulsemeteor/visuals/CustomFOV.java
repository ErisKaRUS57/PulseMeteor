package com.pulsemeteor.visuals;

import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;

/**
 * CustomFOV - dynamic field-of-view changes based on speed or combat state.
 */
public class CustomFOV {
    private boolean enabled = false;
    private int fov = 90;
    private boolean dynamic = true;
    private float maxDynamicFov = 110;

    private float currentFov;
    private float targetFov;

    public CustomFOV() {
        this.currentFov = 90;
        this.targetFov = 90;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getFov() {
        return fov;
    }

    public void setFov(int fov) {
        this.fov = Math.max(30, Math.min(120, fov));
    }

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    /**
     * Update the dynamic FOV based on player speed.
     * Called every render frame.
     */
    public void update(float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || !enabled) return;

        if (dynamic) {
            double velocity = client.player.getVelocity().horizontalLength();
            double target = fov + (velocity * 20.0);
            target = Math.min(target, maxDynamicFov);

            // Smooth transition
            currentFov += (float) (target - currentFov) * 0.1f;
        } else {
            currentFov = fov;
        }
    }

    /**
     * Get the current FOV value to apply.
     */
    public double getCurrentFov() {
        return currentFov;
    }
}

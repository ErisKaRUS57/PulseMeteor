package com.pulsemeteor.visuals;

import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

/**
 * ViewModelChanger - adjusts hand/item positions for better PvP visibility.
 * Allows customization of item display position and size.
 */
public class ViewModelChanger {
    private boolean enabled = false;

    // Position offsets
    private float posX = 0.0f;
    private float posY = 0.0f;
    private float posZ = 0.0f;

    // Scale
    private float scaleX = 1.0f;
    private float scaleY = 1.0f;
    private float scaleZ = 1.0f;

    // Rotation
    private float rotX = 0.0f;
    private float rotY = 0.0f;
    private float rotZ = 0.0f;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setPosition(float x, float y, float z) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
    }

    public void setScale(float x, float y, float z) {
        this.scaleX = x;
        this.scaleY = y;
        this.scaleZ = z;
    }

    public void setRotation(float x, float y, float z) {
        this.rotX = x;
        this.rotY = y;
        this.rotZ = z;
    }

    public float getPosX() { return posX; }
    public float getPosY() { return posY; }
    public float getPosZ() { return posZ; }

    public float getScaleX() { return scaleX; }
    public float getScaleY() { return scaleY; }
    public float getScaleZ() { return scaleZ; }

    public float getRotX() { return rotX; }
    public float getRotY() { return rotY; }
    public float getRotZ() { return rotZ; }
}

package com.pulsemeteor.visuals;

/**
 * NoHurtCam - removes the camera shake when the player is hit.
 * Works by overriding the hurt camera tilt via mixin.
 */
public class NoHurtCam {
    private boolean enabled = true;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}

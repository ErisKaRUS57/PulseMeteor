package com.pulsemeteor.visuals;

import com.pulsemeteor.PulseMeteor;
import net.minecraft.client.MinecraftClient;

/**
 * Central manager for all Pulse Visuals-style features.
 * Handles initialization of visual enhancement systems.
 */
public class VisualsManager {
    private HitIndicator hitIndicator;
    private BlockOverlay blockOverlay;
    private ViewModelChanger viewModelChanger;
    private NoHurtCam noHurtCam;
    private CustomFOV customFOV;
    private Trajectories trajectories;
    private Nametags nametags;
    private PulseAnimation pulseAnimation;

    private boolean initialized = false;

    public void initialize() {
        if (initialized) return;

        this.hitIndicator = new HitIndicator();
        this.blockOverlay = new BlockOverlay();
        this.viewModelChanger = new ViewModelChanger();
        this.noHurtCam = new NoHurtCam();
        this.customFOV = new CustomFOV();
        this.trajectories = new Trajectories();
        this.nametags = new Nametags();
        this.pulseAnimation = new PulseAnimation();

        PulseMeteor.LOGGER.info("Visuals system initialized.");
        initialized = true;
    }

    public HitIndicator getHitIndicator() {
        return hitIndicator;
    }

    public BlockOverlay getBlockOverlay() {
        return blockOverlay;
    }

    public ViewModelChanger getViewModelChanger() {
        return viewModelChanger;
    }

    public NoHurtCam getNoHurtCam() {
        return noHurtCam;
    }

    public CustomFOV getCustomFOV() {
        return customFOV;
    }

    public Trajectories getTrajectories() {
        return trajectories;
    }

    public Nametags getNametags() {
        return nametags;
    }

    public PulseAnimation getPulseAnimation() {
        return pulseAnimation;
    }

    public void onRender(float tickDelta) {
        if (!initialized) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        trajectories.render(tickDelta);
        pulseAnimation.update(tickDelta);
    }
}

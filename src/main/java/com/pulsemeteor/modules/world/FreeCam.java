package com.pulsemeteor.modules.world;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

/**
 * FreeCam - detaches camera from player and allows free flying.
 */
public class FreeCam extends Module {
    private final NumberSetting speed;
    private final BooleanSetting renderPlayer;

    private OtherClientPlayerEntity fakePlayer;
    private Vec3d originalPos;
    private float originalYaw;
    private float originalPitch;

    public FreeCam() {
        super("FreeCam", "Detach camera for free flight", Category.WORLD);
        this.speed = createNumber("Speed", "Camera movement speed", 1.0, 0.1, 5.0, 0.1);
        this.renderPlayer = createBoolean("Render Player", "Show the original player model", true);
    }

    @Override
    public void onEnable() {
        if (mc.player == null || mc.world == null) return;

        originalPos = mc.player.getPos();
        originalYaw = mc.player.getYaw();
        originalPitch = mc.player.getPitch();

        // Spawn a fake player at the original position
        if (renderPlayer.isEnabled()) {
            fakePlayer = new OtherClientPlayerEntity(mc.world, mc.player.getGameProfile());
            fakePlayer.copyFrom(mc.player);
            fakePlayer.setPosition(originalPos);
            mc.world.addEntity(fakePlayer.getId(), fakePlayer);
        }

        mc.player.setNoGravity(true);
        mc.player.setVelocity(0, 0, 0);
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;

        // Return player to original position
        mc.player.setPosition(originalPos);
        mc.player.setYaw(originalYaw);
        mc.player.setPitch(originalPitch);
        mc.player.setNoGravity(false);

        // Remove fake player
        if (fakePlayer != null) {
            mc.world.removeEntity(fakePlayer.getId(), Entity.RemovalReason.DISCARDED);
            fakePlayer = null;
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;

        // Disable movement processing for the real player
        client.player.input.movementForward = 0;
        client.player.input.movementSideways = 0;
        client.player.input.jump = false;
        client.player.input.sneak = false;

        client.player.setNoGravity(true);
        client.player.setVelocity(0, 0, 0);

        // Camera is controlled by spectator-like movement handled in MixinClientPlayerEntity
    }

    public double getSpeed() {
        return speed.getValueDouble();
    }
}

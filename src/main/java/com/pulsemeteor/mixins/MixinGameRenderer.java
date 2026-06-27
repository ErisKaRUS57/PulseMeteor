package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixes into GameRenderer for FOV modifications and NoHurtCam.
 */
@Mixin(GameRenderer.class)
public class MixinGameRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        // Update custom FOV
        PulseMeteorClient.getInstance().getVisualsManager().getCustomFOV().update(tickDelta);
    }

    @Inject(method = "getFov", at = @At("RETURN"), cancellable = true)
    private void onGetFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        var customFov = PulseMeteorClient.getInstance().getVisualsManager().getCustomFOV();
        if (customFov.isEnabled()) {
            cir.setReturnValue(customFov.getCurrentFov());
        }
    }

    @Inject(method = "tiltScreenWhenHurt", at = @At("HEAD"), cancellable = true)
    private void onTiltScreen(float tickDelta, CallbackInfo ci) {
        // NoHurtCam - cancel screen tilt when hurt
        if (PulseMeteorClient.getInstance().getVisualsManager().getNoHurtCam().isEnabled()) {
            ci.cancel();
        }
    }
}

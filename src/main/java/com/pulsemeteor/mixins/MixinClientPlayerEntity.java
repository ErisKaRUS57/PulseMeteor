package com.pulsemeteor.mixins;

import com.pulsemeteor.modules.Module;
import com.pulsemeteor.modules.ModuleManager;
import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.modules.world.FreeCam;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Mixes into ClientPlayerEntity for movement modifications.
 * Handles modules like FreeCam, Speed, Flight, Jesus, etc.
 */
@Mixin(ClientPlayerEntity.class)
public class MixinClientPlayerEntity {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        // Movement modules hook here
    }

    @Inject(method = "isCamera", at = @At("RETURN"), cancellable = true)
    private void onIsCamera(CallbackInfoReturnable<Boolean> cir) {
        // FreeCam makes the fake player the camera
        FreeCam freeCam = PulseMeteor.getInstance().getModuleManager().getModule(FreeCam.class);
        if (freeCam != null && freeCam.isEnabled()) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldAutoJump", at = @At("HEAD"), cancellable = true)
    private void onShouldAutoJump(CallbackInfoReturnable<Boolean> cir) {
        // Disable auto-jump when modules are active
        if (PulseMeteor.getInstance().getModuleManager().getActiveModules().size() > 0) {
            // cir.setReturnValue(false);
        }
    }
}

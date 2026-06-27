package com.pulsemeteor.mixins;

import com.pulsemeteor.modules.render.Chams;
import com.pulsemeteor.PulseMeteor;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into PlayerEntityRenderer for player-specific rendering (Chams, ESP).
 */
@Mixin(PlayerEntityRenderer.class)
public class MixinPlayerEntityRenderer {

    @Inject(method = "render(Lnet/minecraft/client/network/AbstractClientPlayerEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At("HEAD"))
    private void onRender(AbstractClientPlayerEntity player, float yaw, float tickDelta,
                          MatrixStack matrices,
                          net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                          int light, CallbackInfo ci) {
        // Player ESP rendering hook
    }
}

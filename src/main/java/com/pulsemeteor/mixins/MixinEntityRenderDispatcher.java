package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.modules.render.ESP;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into EntityRenderDispatcher for entity outline rendering (ESP).
 */
@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(Entity entity, double x, double y, double z, float yaw,
                          float tickDelta, net.minecraft.client.util.math.MatrixStack matrices,
                          VertexConsumer vertexConsumer, int light, CallbackInfo ci) {
        // ESP rendering hooks
    }
}

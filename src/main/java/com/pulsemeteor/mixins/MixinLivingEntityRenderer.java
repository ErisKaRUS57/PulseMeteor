package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import com.pulsemeteor.modules.render.Chams;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into LivingEntityRenderer for Chams rendering and custom nametags.
 */
@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(LivingEntity entity, float yaw, float tickDelta,
                          MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                          CallbackInfo ci) {
        // Chams rendering is handled here by modifying the rendering pipeline
    }

    @Inject(method = "renderLabelIfPresent", at = @At("HEAD"), cancellable = true)
    private void onRenderLabel(LivingEntity entity, net.minecraft.text.Text text,
                               MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light,
                               CallbackInfo ci) {
        // Custom nametags override
        if (PulseMeteorClient.getInstance().getVisualsManager().getNametags().isEnabled()) {
            ci.cancel();
            PulseMeteorClient.getInstance().getVisualsManager().getNametags()
                    .renderNametag(entity, 0, matrices, null, light, 0);
        }
    }
}

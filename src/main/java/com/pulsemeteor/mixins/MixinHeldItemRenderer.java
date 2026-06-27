package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into HeldItemRenderer for ViewModel transformation on held items.
 */
@Mixin(HeldItemRenderer.class)
public class MixinHeldItemRenderer {

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void onRenderFirstPersonItem(net.minecraft.client.network.AbstractClientPlayerEntity player,
                                          float tickDelta, float pitch, Hand hand,
                                          float swingProgress, ItemStack item, float equipProgress,
                                          MatrixStack matrices, net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                                          int light, CallbackInfo ci) {
        var viewModel = PulseMeteorClient.getInstance().getVisualsManager().getViewModelChanger();
        if (viewModel.isEnabled()) {
            matrices.translate(viewModel.getPosX() * 0.1f, viewModel.getPosY() * 0.1f, viewModel.getPosZ() * 0.1f);
            matrices.scale(viewModel.getScaleX(), viewModel.getScaleY(), viewModel.getScaleZ());
        }
    }
}

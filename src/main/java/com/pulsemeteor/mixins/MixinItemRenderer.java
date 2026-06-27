package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into ItemRenderer for ViewModelChanger modifications.
 * Adjusts item rendering position, scale, and rotation.
 */
@Mixin(ItemRenderer.class)
public class MixinItemRenderer {

    @Inject(method = "renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;IILnet/minecraft/client/render/model/BakedModel;)V",
            at = @At("HEAD"))
    private void onRenderItem(ItemStack stack, net.minecraft.client.render.model.json.ModelTransformationMode transformationMode,
                              boolean leftHanded, MatrixStack matrices,
                              net.minecraft.client.render.VertexConsumerProvider vertexConsumers,
                              int light, int overlay, net.minecraft.client.render.model.BakedModel model,
                              CallbackInfo ci) {
        // ViewModel changer - only modify first-person hand rendering
        if (transformationMode == net.minecraft.client.render.model.json.ModelTransformationMode.FIRST_PERSON_RIGHT_HAND
                || transformationMode == net.minecraft.client.render.model.json.ModelTransformationMode.FIRST_PERSON_LEFT_HAND) {

            var viewModel = PulseMeteorClient.getInstance().getVisualsManager().getViewModelChanger();
            if (viewModel.isEnabled()) {
                matrices.translate(viewModel.getPosX(), viewModel.getPosY(), viewModel.getPosZ());
                matrices.scale(viewModel.getScaleX(), viewModel.getScaleY(), viewModel.getScaleZ());
            }
        }
    }
}

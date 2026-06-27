package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into WorldRenderer for world-based rendering (Block overlays, ESP boxes, Tracers).
 */
@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(MatrixStack matrices, float tickDelta, long limitTime,
                          boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer,
                          LightmapTextureManager lightmapTextureManager, Matrix4f positionMatrix,
                          CallbackInfo ci) {
        // Render block overlays
        PulseMeteorClient.getInstance().getVisualsManager().getBlockOverlay().render(tickDelta);

        // Render visual trajectories
        PulseMeteorClient.getInstance().getVisualsManager().onRender(tickDelta);
    }
}

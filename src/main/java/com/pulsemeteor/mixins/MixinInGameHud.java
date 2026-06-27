package com.pulsemeteor.mixins;

import com.pulsemeteor.PulseMeteorClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixes into InGameHud to render our custom HUD elements and visual effects.
 */
@Mixin(InGameHud.class)
public class MixinInGameHud {

    @Inject(method = "render", at = @At("TAIL"))
    private void onRender(DrawContext context, float tickDelta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Render the Pulse Meteor HUD system
        PulseMeteorClient.getInstance().getHudManager().render(context, tickDelta);

        // Render hit indicators
        PulseMeteorClient.getInstance().getVisualsManager().getHitIndicator()
                .render(context, screenWidth, screenHeight, tickDelta);

        // Render pulse crosshair animation
        PulseMeteorClient.getInstance().getVisualsManager().getPulseAnimation()
                .renderCrosshairPulse(context, screenWidth / 2, screenHeight / 2);
    }
}

package com.pulsemeteor.modules.render;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.Color;

/**
 * Chams - renders entities through walls with a solid color.
 */
public class Chams extends Module {
    private final BooleanSetting players;
    private final BooleanSetting mobs;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;

    public Chams() {
        super("Chams", "Render entities through walls", Category.RENDER);
        this.players = createBoolean("Players", "Show players through walls", true);
        this.mobs = createBoolean("Mobs", "Show mobs through walls", false);
        this.red = createNumber("Red", "Red component", 0, 0, 255, 1);
        this.green = createNumber("Green", "Green component", 100, 0, 255, 1);
        this.blue = createNumber("Blue", "Blue component", 255, 0, 255, 1);
        this.alpha = createNumber("Alpha", "Opacity", 150, 0, 255, 1);
    }

    public boolean shouldRenderChams(Entity entity) {
        if (entity == mc.player) return false;
        if (entity instanceof PlayerEntity && players.isEnabled()) return true;
        if (entity instanceof Monster && mobs.isEnabled()) return true;
        return false;
    }

    public Color getChamsColor() {
        return new Color(red.getValueInt(), green.getValueInt(), blue.getValueInt(), alpha.getValueInt());
    }

    @Override
    public void onTick(MinecraftClient client) {
        // Chams is primarily handled through mixin in LivingEntityRenderer
    }
}

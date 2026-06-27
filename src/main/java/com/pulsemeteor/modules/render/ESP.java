package com.pulsemeteor.modules.render;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.awt.Color;

/**
 * ESP - highlights entities through walls with customizable colors.
 */
public class ESP extends Module {
    private final ModeSetting mode;
    private final BooleanSetting players;
    private final BooleanSetting mobs;
    private final BooleanSetting items;
    private final NumberSetting red;
    private final NumberSetting green;
    private final NumberSetting blue;
    private final NumberSetting alpha;

    public ESP() {
        super("ESP", "Highlight entities through walls", Category.RENDER);
        this.mode = createMode("Mode", "ESP rendering mode", "Box", "Box", "Glow", "Outline", "2D");
        this.players = createBoolean("Players", "Highlight players", true);
        this.mobs = createBoolean("Mobs", "Highlight mobs", false);
        this.items = createBoolean("Items", "Highlight items", false);
        this.red = createNumber("Red", "Red color component", 0, 0, 255, 1);
        this.green = createNumber("Green", "Green color component", 255, 0, 255, 1);
        this.blue = createNumber("Blue", "Blue color component", 0, 0, 255, 1);
        this.alpha = createNumber("Alpha", "ESP opacity", 128, 0, 255, 1);
    }

    @Override
    public void onWorldRender(float tickDelta) {
        if (mc.world == null || mc.player == null) return;

        Color espColor = new Color(red.getValueInt(), green.getValueInt(), blue.getValueInt(), alpha.getValueInt());

        for (Entity entity : mc.world.getEntities()) {
            if (!shouldRender(entity)) continue;
            // ESP rendering is handled in mixin/rendering hooks
            // Logic here stores the entities to render for the mixin callback
            // The actual GL rendering is in MixinWorldRenderer
            renderESPBox(entity, tickDelta, espColor);
        }
    }

    private boolean shouldRender(Entity entity) {
        if (entity == mc.player) return false;
        if (entity instanceof PlayerEntity && players.isEnabled()) return true;
        if (entity instanceof Monster && mobs.isEnabled()) return true;
        if (entity instanceof PassiveEntity && mobs.isEnabled()) return true;
        if (entity instanceof ItemEntity && items.isEnabled()) return true;
        return false;
    }

    private void renderESPBox(Entity entity, float tickDelta, Color color) {
        // Box rendering logic is injected at the mixin level
        // This just marks entities for rendering
        // In a real implementation, this would draw the box using WorldRenderer
    }
}

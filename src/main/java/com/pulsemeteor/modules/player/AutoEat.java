package com.pulsemeteor.modules.player;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

/**
 * AutoEat - automatically eats food when hunger is low.
 */
public class AutoEat extends Module {
    private final NumberSetting hungerThreshold;
    private final BooleanSetting preferGapples;

    public AutoEat() {
        super("AutoEat", "Automatically eat food when hungry", Category.PLAYER);
        this.hungerThreshold = createNumber("Hunger Threshold", "Hunger level to start eating", 10, 1, 19, 1);
        this.preferGapples = createBoolean("Prefer Gapples", "Eat golden apples first", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        // Don't interrupt mining or combat
        if (client.interactionManager.isBreakingBlock()) return;

        int foodLevel = client.player.getHungerManager().getFoodLevel();
        if (foodLevel > hungerThreshold.getValueInt()) return;
        if (client.player.isUsingItem()) return;

        // Find food in hotbar
        int foodSlot = -1;
        int bestScore = -1;

        for (int i = 0; i < 9; i++) {
            ItemStack stack = client.player.getInventory().getStack(i);
            FoodComponent food = stack.get(DataComponentTypes.FOOD);
            if (food == null) continue;

            if (!stack.get(DataComponentTypes.FOOD).isAlwaysEdible() && foodLevel >= 20) continue;

            int score = food.nutrition();
            if (preferGapples.isEnabled() && stack.getItem().getTranslationKey().contains("golden")) {
                score += 100;
            }

            if (score > bestScore) {
                bestScore = score;
                foodSlot = i;
            }
        }

        if (foodSlot < 0) return;

        // Switch to food and start eating
        int prevSlot = client.player.getInventory().selectedSlot;
        client.player.getInventory().selectedSlot = foodSlot;

        // Right-click to eat
        client.interactionManager.interactItem(client.player, Hand.MAIN_HAND);
        client.options.useKey.setPressed(true);
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.options.useKey.setPressed(false);
        }
    }
}

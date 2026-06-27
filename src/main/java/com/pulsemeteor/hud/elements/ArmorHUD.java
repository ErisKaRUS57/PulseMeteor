package com.pulsemeteor.hud.elements;

import com.pulsemeteor.hud.HUDElement;
import com.pulsemeteor.visuals.PulseAnimation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * ArmorHUD - displays equipped armor and hand items with durability bars.
 */
public class ArmorHUD extends HUDElement {
    private static final int SLOT_SIZE = 18;

    public ArmorHUD() {
        super("Armor", 4, 30, 90, 20);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        PlayerInventory inv = mc.player.getInventory();
        int startX = x;
        int startY = y;

        // Render armor (from boots to helmet - bottom to top)
        for (int i = 3; i >= 0; i--) {
            ItemStack armor = inv.armor.get(i);
            int slotX = startX + (3 - i) * (SLOT_SIZE + 2);
            renderItem(context, armor, slotX, startY);
        }

        // Render main hand
        ItemStack mainHand = inv.getMainHandStack();
        int handX = startX + 4 * (SLOT_SIZE + 2);
        renderItem(context, mainHand, handX, startY);

        // Render offhand
        int offhandX = handX + SLOT_SIZE + 2;
        renderItem(context, inv.offHand.get(0), offhandX, startY);

        this.width = (offhandX - x) + SLOT_SIZE;
        this.height = SLOT_SIZE;
    }

    private void renderItem(DrawContext context, ItemStack stack, int x, int y) {
        if (stack.isEmpty()) return;

        context.drawItem(stack, x, y);
        context.drawItemInSlot(mc.textRenderer, stack, x, y);

        // Render durability bar
        if (stack.isDamageable()) {
            float maxDamage = stack.getMaxDamage();
            float currentDamage = stack.getDamage();
            float durability = 1.0f - (currentDamage / maxDamage);

            int barWidth = 14;
            int barHeight = 2;
            int barX = x + 2;
            int barY = y + 14;

            int color;
            if (durability > 0.5f) {
                color = 0xFF00FF00;
            } else if (durability > 0.2f) {
                color = 0xFFFFAA00;
            } else {
                color = 0xFFFF0000;
            }

            context.fill(barX, barY, barX + barWidth, barY + barHeight, 0xFF000000);
            context.fill(barX, barY, barX + (int) (barWidth * durability), barY + barHeight, color);
        }
    }
}

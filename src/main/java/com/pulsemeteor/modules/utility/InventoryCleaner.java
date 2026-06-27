package com.pulsemeteor.modules.utility;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;

import java.util.HashSet;
import java.util.Set;

/**
 * InventoryCleaner - automatically sorts and throws away unwanted items.
 */
public class InventoryCleaner extends Module {
    private final NumberSetting delay;
    private final BooleanSetting trashTools;
    private final BooleanSetting trashArmor;
    private final BooleanSetting sortItems;

    private int tickDelay = 0;

    public InventoryCleaner() {
        super("InventoryCleaner", "Clean and sort inventory", Category.UTILITY);
        this.delay = createNumber("Delay", "Ticks between actions", 5, 1, 20, 1);
        this.trashTools = createBoolean("Trash Tools", "Throw away damaged tools", true);
        this.trashArmor = createBoolean("Trash Armor", "Throw away damaged armor", true);
        this.sortItems = createBoolean("Sort", "Sort and organize inventory", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;
        if (client.player.currentScreenHandler != client.player.getInventory()) return;

        tickDelay = Math.max(0, tickDelay - 1);
        if (tickDelay > 0) return;

        PlayerInventory inv = client.player.getInventory();

        // Clean damaged items
        for (int i = 9; i < inv.size(); i++) {
            ItemStack stack = inv.getStack(i);
            if (stack.isEmpty()) continue;

            if (shouldTrash(stack)) {
                // Drop the item
                client.interactionManager.clickSlot(
                        client.player.currentScreenHandler.syncId,
                        i, 0, SlotActionType.THROW, client.player
                );
                tickDelay = delay.getValueInt();
                return;
            }
        }
    }

    private boolean shouldTrash(ItemStack stack) {
        if (!stack.isDamageable()) return false;

        double durability = (double) (stack.getMaxDamage() - stack.getDamage()) / stack.getMaxDamage();

        if (stack.getItem().getTranslationKey().contains("sword")
                || stack.getItem().getTranslationKey().contains("pickaxe")
                || stack.getItem().getTranslationKey().contains("axe")
                || stack.getItem().getTranslationKey().contains("shovel")
                || stack.getItem().getTranslationKey().contains("hoe")) {
            return trashTools.isEnabled() && durability < 0.15;
        }

        if (stack.getItem().getTranslationKey().contains("helmet")
                || stack.getItem().getTranslationKey().contains("chestplate")
                || stack.getItem().getTranslationKey().contains("leggings")
                || stack.getItem().getTranslationKey().contains("boots")) {
            return trashArmor.isEnabled() && durability < 0.15;
        }

        return false;
    }
}

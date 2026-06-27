package com.pulsemeteor.modules.combat;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;

/**
 * AutoTotem - automatically equips a totem of undying in the off-hand.
 */
public class AutoTotem extends Module {
    private final BooleanSetting preferOffhand;
    private final NumberSetting healthThreshold;
    private final BooleanSetting checkHealth;

    public AutoTotem() {
        super("AutoTotem", "Automatically equips a totem of undying", Category.COMBAT);
        this.preferOffhand = createBoolean("Prefer Offhand", "Move totem to off-hand", true);
        this.healthThreshold = createNumber("Health Threshold", "Minimum health to equip totem", 20.0, 1.0, 20.0, 1.0);
        this.checkHealth = createBoolean("Check Health", "Only equip when health is low", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;

        // Check if we need a totem
        if (checkHealth.isEnabled() && client.player.getHealth() > healthThreshold.getValueFloat()) return;

        // Check if we already have a totem in off-hand
        if (client.player.getOffHandStack().getItem() == Items.TOTEM_OF_UNDYING) return;

        // Find totem in inventory
        PlayerInventory inv = client.player.getInventory();
        int totemSlot = -1;

        for (int i = 0; i < inv.size(); i++) {
            if (inv.getStack(i).getItem() == Items.TOTEM_OF_UNDYING) {
                totemSlot = i;
                break;
            }
        }

        if (totemSlot < 0) return;

        // Move totem to off-hand
        if (totemSlot < 9) {
            // Hotbar slot - swap with offhand
            client.interactionManager.clickSlot(
                    client.player.currentScreenHandler.syncId,
                    totemSlot < 9 ? totemSlot + 36 : totemSlot,
                    40, // Offhand slot
                    SlotActionType.SWAP,
                    client.player
            );
        } else {
            // Move to hotbar first then swap
            int emptyHotbarSlot = getEmptyHotbarSlot(inv);
            if (emptyHotbarSlot >= 0) {
                client.interactionManager.clickSlot(
                        client.player.currentScreenHandler.syncId,
                        totemSlot,
                        emptyHotbarSlot,
                        SlotActionType.QUICK_MOVE,
                        client.player
                );
                // Then swap with offhand
                client.interactionManager.clickSlot(
                        client.player.currentScreenHandler.syncId,
                        emptyHotbarSlot + 36,
                        40,
                        SlotActionType.SWAP,
                        client.player
                );
            }
        }
    }

    private int getEmptyHotbarSlot(PlayerInventory inv) {
        for (int i = 0; i < 9; i++) {
            if (inv.getStack(i).isEmpty()) return i;
        }
        return -1;
    }
}

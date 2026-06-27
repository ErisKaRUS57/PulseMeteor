package com.pulsemeteor.modules.player;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * AutoFarm - automatically harvests and replants crops.
 */
public class AutoFarm extends Module {
    private final NumberSetting range;
    private final BooleanSetting harvest;
    private final BooleanSetting replant;
    private final BooleanSetting autoTool;

    private int delay = 0;

    public AutoFarm() {
        super("AutoFarm", "Automatically harvest and replant crops", Category.PLAYER);
        this.range = createNumber("Range", "Farm interaction range", 4.0, 2.0, 6.0, 0.5);
        this.harvest = createBoolean("Harvest", "Harvest mature crops", true);
        this.replant = createBoolean("Replant", "Replant seeds after harvesting", true);
        this.autoTool = createBoolean("Auto Tool", "Switch to hoe when farming", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        delay = Math.max(0, delay - 1);
        if (delay > 0) return;

        int radius = (int) Math.ceil(range.getValueDouble());
        BlockPos playerPos = client.player.getBlockPos();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (client.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                            > range.getValueDouble() * range.getValueDouble()) continue;

                    BlockState state = client.world.getBlockState(pos);

                    // Harvest mature crops
                    if (harvest.isEnabled() && isMatureCrop(state)) {
                        client.interactionManager.attackBlock(pos, Direction.UP);
                        client.player.swingHand(Hand.MAIN_HAND);
                        delay = 2;
                        return;
                    }

                    // Replant farmland
                    if (replant.isEnabled() && state.getBlock() == Blocks.FARMLAND
                            && client.world.isAir(pos.up())) {
                        // Check for seeds in inventory
                        int seedSlot = -1;
                        for (int i = 0; i < 9; i++) {
                            ItemStack stack = client.player.getInventory().getStack(i);
                            if (stack.getItem() == Items.WHEAT_SEEDS
                                    || stack.getItem() == Items.BEETROOT_SEEDS
                                    || stack.getItem() == Items.CARROT
                                    || stack.getItem() == Items.POTATO) {
                                seedSlot = i;
                                break;
                            }
                        }
                        if (seedSlot >= 0) {
                            client.player.getInventory().selectedSlot = seedSlot;
                            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND,
                                    new BlockHitResult(Vec3d.ofCenter(pos.up()), Direction.UP, pos.up(), false));
                            client.player.swingHand(Hand.MAIN_HAND);
                            delay = 2;
                            return;
                        }
                    }
                }
            }
        }
    }

    private boolean isMatureCrop(BlockState state) {
        Block block = state.getBlock();
        if (block instanceof CropBlock crop) {
            return crop.isMature(state);
        }
        if (block instanceof NetherWartBlock) {
            return state.get(NetherWartBlock.AGE) >= 3;
        }
        if (block instanceof StemBlock) {
            return state.get(StemBlock.AGE) >= 7;
        }
        return false;
    }
}

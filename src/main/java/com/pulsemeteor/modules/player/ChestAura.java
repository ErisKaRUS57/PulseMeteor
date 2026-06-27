package com.pulsemeteor.modules.player;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.Optional;

/**
 * ChestAura - automatically opens and loots nearby chests.
 */
public class ChestAura extends Module {
    private final NumberSetting range;
    private final BooleanSetting autoSteal;
    private final BooleanSetting silentOpen;

    public ChestAura() {
        super("ChestAura", "Automatically interact with nearby chests", Category.PLAYER);
        this.range = createNumber("Range", "Chest search range", 4.0, 2.0, 6.0, 0.5);
        this.autoSteal = createBoolean("Auto Steal", "Automatically take all items", true);
        this.silentOpen = createBoolean("Silent Open", "Open chests without swinging", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (client.player.currentScreenHandler != client.player.getInventory()) return;

        // Find nearest unopened chest
        Optional<BlockPos> chest = findNearestChest(client);
        if (chest.isEmpty()) return;

        // Open the chest
        BlockPos pos = chest.get();
        BlockHitResult hit = new BlockHitResult(
                Vec3d.ofCenter(pos),
                Direction.UP,
                pos,
                false
        );

        // Face chest
        Vec3d diff = Vec3d.ofCenter(pos).subtract(client.player.getEyePos());
        client.player.setYaw((float) Math.toDegrees(Math.atan2(-diff.x, -diff.z)));

        client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        if (!silentOpen.isEnabled()) {
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private Optional<BlockPos> findNearestChest(MinecraftClient client) {
        int r = (int) Math.ceil(range.getValueDouble());
        BlockPos playerPos = client.player.getBlockPos();

        return BlockPos.streamOutward(playerPos, r, r, r)
                .map(BlockPos::toImmutable)
                .filter(pos -> {
                    BlockState state = client.world.getBlockState(pos);
                    return state.getBlock() instanceof ChestBlock
                            || state.getBlock() instanceof EnderChestBlock
                            || state.getBlock() instanceof BarrelBlock
                            || state.getBlock() instanceof ShulkerBoxBlock;
                })
                .filter(pos -> client.player.squaredDistanceTo(
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                        <= range.getValueDouble() * range.getValueDouble())
                .min(Comparator.comparingDouble(pos -> client.player.squaredDistanceTo(
                        pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)));
    }
}

package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Scaffold - automatically places blocks beneath you while walking.
 * Useful for bridging and building.
 */
public class Scaffold extends Module {
    private final BooleanSetting tower;
    private final BooleanSetting swing;
    private final NumberSetting placeDelay;
    private final ModeSetting blockMode;

    private int delayCounter = 0;

    public Scaffold() {
        super("Scaffold", "Automatically place blocks beneath you", Category.MOVEMENT);
        this.tower = createBoolean("Tower", "Hold jump to tower up", true);
        this.swing = createBoolean("Swing", "Swing hand when placing", true);
        this.placeDelay = createNumber("Place Delay", "Delay between placements", 0, 0, 5, 1);
        this.blockMode = createMode("Block Mode", "Block placement mode", "Normal", "Normal", "Silent", "Spoof");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        delayCounter = Math.max(0, delayCounter - 1);
        if (delayCounter > 0) return;

        // Tower mode
        if (tower.isEnabled() && client.options.jumpKey.isPressed()) {
            client.player.setVelocity(0, 0.42, 0);
            // Place block below
            BlockPos below = client.player.getBlockPos().down();
            placeBlock(client, below);
        }

        // Check if player is above air
        BlockPos blockBelow = client.player.getBlockPos().down();
        if (client.world.isAir(blockBelow) || !client.world.getBlockState(blockBelow).isFullCube(client.world, blockBelow)) {
            placeBlock(client, blockBelow);
        }

        // Extend placement to where the player is moving towards
        Vec3d vel = client.player.getVelocity();
        if (Math.abs(vel.x) > 0.1 || Math.abs(vel.z) > 0.1) {
            BlockPos ahead = client.player.getBlockPos().add(
                    (int) Math.signum(vel.x), -1, (int) Math.signum(vel.z));
            if (client.world.isAir(ahead)) {
                placeBlock(client, ahead);
            }
        }
    }

    private void placeBlock(MinecraftClient client, BlockPos pos) {
        // Find a block in inventory
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            if (client.player.getInventory().getStack(i).getItem() instanceof BlockItem) {
                slot = i;
                break;
            }
        }
        if (slot < 0) return;

        // Find a supporting block to place against
        Direction side = findPlaceSide(client, pos);
        if (side == null) return;

        // Switch to block slot
        int prevSlot = client.player.getInventory().selectedSlot;
        client.player.getInventory().selectedSlot = slot;

        // Place the block
        Vec3d hitPos = Vec3d.ofCenter(pos).add(Vec3d.of(side.getVector()).multiply(0.5));
        BlockHitResult hit = new BlockHitResult(hitPos, side.getOpposite(), pos.add(side.getVector()), false);

        if (blockMode.isMode("Silent")) {
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        } else {
            client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hit);
        }

        if (swing.isEnabled()) {
            client.player.swingHand(Hand.MAIN_HAND);
        }

        client.player.getInventory().selectedSlot = prevSlot;
        delayCounter = placeDelay.getValueInt();
    }

    private Direction findPlaceSide(MinecraftClient client, BlockPos target) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbor = target.add(dir.getVector());
            if (!client.world.isAir(neighbor) && client.world.getBlockState(neighbor).isFullCube(client.world, neighbor)) {
                return dir;
            }
        }
        return null;
    }
}

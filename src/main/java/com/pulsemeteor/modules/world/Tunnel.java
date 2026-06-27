package com.pulsemeteor.modules.world;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

/**
 * Tunnel - automatically mines a tunnel in a straight line.
 */
public class Tunnel extends Module {
    private final NumberSetting height;
    private final NumberSetting width;
    private final NumberSetting distance;
    private final BooleanSetting autoWalk;

    private int blocksMined = 0;
    private BlockPos targetBlock = null;

    public Tunnel() {
        super("Tunnel", "Automatically mine a tunnel", Category.WORLD);
        this.height = createNumber("Height", "Tunnel height", 2, 1, 3, 1);
        this.width = createNumber("Width", "Tunnel width", 1, 1, 3, 1);
        this.distance = createNumber("Distance", "Blocks to mine before stopping", 50, 10, 500, 10);
        this.autoWalk = createBoolean("Auto Walk", "Automatically walk forward", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (blocksMined >= distance.getValueInt()) {
            disable();
            return;
        }

        Direction facing = client.player.getHorizontalFacing();
        BlockPos front = client.player.getBlockPos().offset(facing);

        // Mine blocks in front
        int halfW = (width.getValueInt() - 1) / 2;
        int h = height.getValueInt();

        for (int y = 0; y < h; y++) {
            for (int x = -halfW; x <= halfW; x++) {
                Direction side = switch (facing) {
                    case NORTH, SOUTH -> Direction.EAST;
                    default -> Direction.NORTH;
                };
                BlockPos target = front.up(y).offset(side, x);

                if (!client.world.isAir(target)) {
                    client.interactionManager.attackBlock(target, facing.getOpposite());
                    client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
                    blocksMined++;
                }
            }
        }

        // Auto walk forward
        if (autoWalk.isEnabled()) {
            client.options.forwardKey.setPressed(true);
        }
    }

    @Override
    public void onDisable() {
        blocksMined = 0;
        if (mc.player != null) {
            mc.options.forwardKey.setPressed(false);
        }
    }
}

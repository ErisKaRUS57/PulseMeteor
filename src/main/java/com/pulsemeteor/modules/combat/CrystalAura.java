package com.pulsemeteor.modules.combat;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * CrystalAura - automatically places and breaks end crystals for PvP.
 */
public class CrystalAura extends Module {
    private final NumberSetting placeRange;
    private final NumberSetting breakRange;
    private final BooleanSetting autoPlace;
    private final BooleanSetting autoBreak;
    private final NumberSetting delay;

    private int tickCounter = 0;

    public CrystalAura() {
        super("CrystalAura", "Automatically place and break end crystals", Category.COMBAT);
        this.placeRange = createNumber("Place Range", "Max crystal placement range", 4.5, 1.0, 6.0, 0.1);
        this.breakRange = createNumber("Break Range", "Max crystal break range", 4.5, 1.0, 6.0, 0.1);
        this.autoPlace = createBoolean("Auto Place", "Automatically place crystals", true);
        this.autoBreak = createBoolean("Auto Break", "Automatically break crystals", true);
        this.delay = createNumber("Delay", "Ticks between actions", 2, 0, 10, 1);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        tickCounter++;

        if (autoBreak.isEnabled() && tickCounter % delay.getValueInt() == 0) {
            // Find and break nearest crystal
            client.world.getEntities().stream()
                    .filter(e -> e instanceof EndCrystalEntity)
                    .filter(e -> client.player.distanceTo(e) <= breakRange.getValueDouble())
                    .findFirst().ifPresent(crystal -> {
                        facePosition(crystal.getPos());
                        client.interactionManager.attackEntity(client.player, crystal);
                        client.player.swingHand(Hand.MAIN_HAND);
                    });
        }

        if (autoPlace.isEnabled() && tickCounter % (delay.getValueInt() + 1) == 0) {
            // Check if holding crystal
            if (client.player.getMainHandStack().getItem() != Items.END_CRYSTAL
                    && client.player.getOffHandStack().getItem() != Items.END_CRYSTAL) return;

            // Place crystal on nearby obsidian
            BlockPos place = findPlacePos(client);
            if (place != null) {
                facePosition(Vec3d.ofCenter(place));
                client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND,
                        new BlockHitResult(Vec3d.ofCenter(place), client.player.getHorizontalFacing(), place, false));
                client.player.swingHand(Hand.MAIN_HAND);
            }
        }
    }

    private BlockPos findPlacePos(MinecraftClient client) {
        double range = placeRange.getValueDouble();
        BlockPos playerPos = client.player.getBlockPos();

        for (int x = -(int)range; x <= (int)range; x++) {
            for (int y = -(int)range; y <= (int)range; y++) {
                for (int z = -(int)range; z <= (int)range; z++) {
                    BlockPos pos = playerPos.add(x, y, z);
                    if (pos.getSquaredDistance(playerPos) > range * range) continue;
                    if (client.world.getBlockState(pos).isOf(net.minecraft.block.Blocks.OBSIDIAN)
                            && client.world.isAir(pos.up())) {
                        return pos.up();
                    }
                }
            }
        }
        return null;
    }

    private void facePosition(Vec3d pos) {
        if (mc.player == null) return;
        Vec3d e = mc.player.getEyePos();
        Vec3d diff = pos.subtract(e);
        double dist = Math.sqrt(diff.x*diff.x + diff.z*diff.z);
        mc.player.setYaw((float)Math.toDegrees(Math.atan2(-diff.x, -diff.z)));
        mc.player.setPitch((float)Math.toDegrees(-Math.atan2(diff.y, dist)));
    }
}

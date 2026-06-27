package com.pulsemeteor.modules.player;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;

/**
 * AutoMine - automatically breaks blocks within range with swing.
 */
public class AutoMine extends Module {
    private final NumberSetting range;
    private final BooleanSetting autoSwing;
    private final BooleanSetting instant;

    private boolean mining = false;
    private BlockPos lastTarget = null;

    public AutoMine() {
        super("AutoMine", "Automatically mine targeted blocks", Category.PLAYER);
        this.range = createNumber("Range", "Mining range", 4.5, 1.0, 6.0, 0.5);
        this.autoSwing = createBoolean("Auto Swing", "Swing hand while mining", true);
        this.instant = createBoolean("Instant", "Break block instantly (creative)", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.interactionManager == null) return;

        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.BLOCK) {
            BlockHitResult hit = (BlockHitResult) client.crosshairTarget;
            BlockPos pos = hit.getBlockPos();

            if (client.player.squaredDistanceTo(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5)
                    > range.getValueDouble() * range.getValueDouble()) return;

            if (instant.isEnabled() && client.player.isCreative()) {
                client.interactionManager.breakBlock(pos);
            } else {
                // Start or continue mining
                if (!pos.equals(lastTarget)) {
                    client.interactionManager.attackBlock(pos, hit.getSide());
                    lastTarget = pos;
                    mining = true;
                } else {
                    client.interactionManager.updateBlockBreakingProgress(pos, hit.getSide());
                }
            }

            if (autoSwing.isEnabled()) {
                client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);
            }
        } else {
            // Reset mining if not targeting a block
            if (mining && lastTarget != null) {
                client.interactionManager.cancelBlockBreaking();
                mining = false;
                lastTarget = null;
            }
        }
    }
}

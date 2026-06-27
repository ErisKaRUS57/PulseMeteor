package com.pulsemeteor.modules.utility;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;

import java.util.Random;

/**
 * AntiAFK - sends random inputs to avoid being kicked for inactivity.
 */
public class AntiAFK extends Module {
    private final NumberSetting interval;
    private final BooleanSetting rotate;
    private final BooleanSetting jump;
    private final BooleanSetting move;

    private int tickCounter = 0;
    private final Random random = new Random();

    public AntiAFK() {
        super("AntiAFK", "Prevents being kicked for AFK", Category.UTILITY);
        this.interval = createNumber("Interval", "Ticks between actions", 100, 20, 600, 10);
        this.rotate = createBoolean("Rotate", "Randomly rotate view", true);
        this.jump = createBoolean("Jump", "Randomly jump", true);
        this.move = createBoolean("Move", "Randomly move", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        tickCounter++;

        if (tickCounter >= interval.getValueInt()) {
            tickCounter = 0;

            // Random rotation
            if (rotate.isEnabled()) {
                client.player.setYaw(client.player.getYaw() + (random.nextFloat() - 0.5f) * 90);
                client.player.setPitch((random.nextFloat() - 0.5f) * 40);
            }

            // Random jump
            if (jump.isEnabled() && client.player.isOnGround()) {
                client.player.jump();
            }

            // Random movement
            if (move.isEnabled()) {
                client.options.forwardKey.setPressed(random.nextBoolean());
                client.options.leftKey.setPressed(random.nextBoolean());
                client.options.rightKey.setPressed(random.nextBoolean());
                client.options.backKey.setPressed(random.nextBoolean());
            }
        }
    }
}

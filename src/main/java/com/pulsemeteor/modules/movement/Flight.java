package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * Flight - creative-style flight with speed control.
 */
public class Flight extends Module {
    private final ModeSetting mode;
    private final NumberSetting speed;

    public Flight() {
        super("Flight", "Enable creative-style flight", Category.MOVEMENT);
        this.mode = createMode("Mode", "Flight mode", "Creative", "Creative", "Vanilla", "Packet");
        this.speed = createNumber("Speed", "Flight speed", 1.0, 0.1, 5.0, 0.1);
    }

    @Override
    public void onEnable() {
        if (mc.player == null) return;
        if (mode.isMode("Creative")) {
            mc.player.getAbilities().flying = true;
            mc.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        if (mc.player == null) return;
        if (mode.isMode("Creative")) {
            if (!mc.player.isCreative()) {
                mc.player.getAbilities().flying = false;
                mc.player.getAbilities().allowFlying = false;
            }
        }
        mc.player.getAbilities().setFlySpeed(0.05f);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        float speedVal = speed.getValueFloat() * 0.05f;

        if (mode.isMode("Creative")) {
            client.player.getAbilities().setFlySpeed(speedVal);
            client.player.getAbilities().flying = true;
            client.player.getAbilities().allowFlying = true;
        } else if (mode.isMode("Vanilla")) {
            if (!client.player.getAbilities().flying) {
                client.player.getAbilities().allowFlying = true;
                client.player.getAbilities().flying = true;
            }
            client.player.getAbilities().setFlySpeed(speedVal);
        } else if (mode.isMode("Packet")) {
            // Packet-based flight - spoof movement packets
            double vel = speed.getValueDouble() * 0.2;
            client.player.setVelocity(0, 0, 0);
            if (client.options.jumpKey.isPressed()) {
                client.player.setVelocity(0, vel, 0);
            }
            if (client.options.sneakKey.isPressed()) {
                client.player.setVelocity(0, -vel, 0);
            }
            // Forward movement
            if (client.options.forwardKey.isPressed() || client.options.backKey.isPressed()
                    || client.options.leftKey.isPressed() || client.options.rightKey.isPressed()) {
                client.player.setVelocity(client.player.getVelocity().add(
                        -Math.sin(Math.toRadians(client.player.getYaw())) * vel * 0.5,
                        0,
                        Math.cos(Math.toRadians(client.player.getYaw())) * vel * 0.5
                ));
            }
        }
    }
}

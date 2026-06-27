package com.pulsemeteor.modules.movement;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;

/**
 * NoFall - negate fall damage using various methods.
 */
public class NoFall extends Module {
    private final ModeSetting mode;

    public NoFall() {
        super("NoFall", "Negate fall damage", Category.MOVEMENT);
        this.mode = createMode("Mode", "NoFall method", "Packet", "Packet", "GroundSpoof", "AirPlace");
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null) return;
        if (client.player.fallDistance <= 2.0f) return;

        if (mode.isMode("Packet")) {
            // Spoof on-ground to reset fall distance
            if (client.player.fallDistance > 2.0f) {
                client.player.networkHandler.sendPacket(
                        new PlayerMoveC2SPacket.OnGroundOnly(true, client.player.horizontalCollision)
                );
                client.player.fallDistance = 0;
            }
        } else if (mode.isMode("GroundSpoof")) {
            // Send ground position packet
            client.player.networkHandler.sendPacket(
                    new PlayerMoveC2SPacket.PositionAndOnGround(
                            client.player.getX(), client.player.getY(), client.player.getZ(), true,
                            client.player.horizontalCollision
                    )
            );
            client.player.fallDistance = 0;
        } else if (mode.isMode("AirPlace")) {
            // Place block below if close enough to ground
            if (client.player.fallDistance > 3.0f) {
                client.player.setVelocity(client.player.getVelocity().x, 0, client.player.getVelocity().z);
                client.player.fallDistance = 0;
            }
        }
    }
}

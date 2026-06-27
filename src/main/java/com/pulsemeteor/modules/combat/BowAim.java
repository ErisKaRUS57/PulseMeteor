package com.pulsemeteor.modules.combat;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;

/**
 * BowAim - automatically aims your bow at the nearest target when drawn.
 */
public class BowAim extends Module {
    private final NumberSetting range;
    private final BooleanSetting silentAim;
    private final BooleanSetting predictMovement;

    public BowAim() {
        super("BowAim", "Automatically aim bow at nearest target", Category.COMBAT);
        this.range = createNumber("Range", "Max aim range", 50.0, 10.0, 100.0, 5.0);
        this.silentAim = createBoolean("Silent Aim", "Aim without visibly rotating", true);
        this.predictMovement = createBoolean("Predict Movement", "Predict target movement for arrows", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (!client.player.isUsingItem() || client.player.getActiveItem().getItem() != Items.BOW) return;

        LivingEntity target = findTarget(client);
        if (target == null) return;

        Vec3d targetPos = predictMovement.isEnabled()
                ? target.getPos().add(target.getVelocity().multiply(client.player.getItemUseTime() / 20.0))
                : target.getEyePos();

        Vec3d playerPos = client.player.getEyePos();
        Vec3d diff = targetPos.subtract(playerPos);

        double horizontalDist = Math.sqrt(diff.x * diff.x + diff.z * diff.z);
        if (horizontalDist < 0.1) return;

        // Basic arrow trajectory calculation
        float velocity = client.player.getItemUseTime() / 20.0f * 3.0f;
        velocity = Math.min(velocity, client.player.getItemUseTime() > 5 ? 3.0f : velocity);
        double gravity = 0.05;
        double pitch = -Math.atan2(diff.y, horizontalDist);
        // Compensate for arrow drop at distance
        pitch -= Math.atan2(horizontalDist * gravity * 80.0 / (velocity * velocity), 1.0);

        float yaw = (float) Math.toDegrees(Math.atan2(-diff.x, -diff.z));
        float pitchDeg = (float) Math.toDegrees(pitch);

        client.player.setYaw(yaw);
        client.player.setPitch(MathHelper.clamp(pitchDeg, -90, 90));
    }

    private LivingEntity findTarget(MinecraftClient client) {
        double r = range.getValueDouble();
        return client.world.getEntities().stream()
                .filter(e -> e instanceof PlayerEntity && e != client.player)
                .map(e -> (LivingEntity) e)
                .filter(e -> !e.isDead() && e.getHealth() > 0)
                .filter(e -> client.player.distanceTo(e) <= r)
                .min(Comparator.comparingDouble(e -> client.player.distanceTo(e)))
                .orElse(null);
    }
}

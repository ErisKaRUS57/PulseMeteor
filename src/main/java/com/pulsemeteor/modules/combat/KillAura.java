package com.pulsemeteor.modules.combat;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.ModeSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;
import java.util.Optional;

/**
 * KillAura - automatically attacks nearest valid entity.
 * Configurable range, rotation smoothing, CPS, target filtering.
 */
public class KillAura extends Module {
    private final NumberSetting range;
    private final NumberSetting cps;
    private final ModeSetting targetMode;
    private final BooleanSetting onlyPlayers;
    private final BooleanSetting noFriends;

    private int attackCooldown = 0;

    public KillAura() {
        super("KillAura", "Automatically attacks nearest entity within range", Category.COMBAT);
        this.range = createNumber("Range", "Attack range in blocks", 4.2, 1.0, 6.0, 0.1);
        this.cps = createNumber("CPS", "Attacks per second", 8.0, 1.0, 20.0, 0.5);
        this.targetMode = createMode("Target Mode", "Target selection", "Single", "Single", "Multi", "Switch");
        this.onlyPlayers = createBoolean("Only Players", "Only attack players", false);
        this.noFriends = createBoolean("No Friends", "Skip friendly players", true);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        int ticks = Math.max(1, (int)(20.0 / cps.getValueDouble()));
        if (++attackCooldown < ticks) return;
        attackCooldown = 0;

        findTarget(client).ifPresent(target -> {
            faceEntity(target);
            client.player.swingHand(Hand.MAIN_HAND);
            client.interactionManager.attackEntity(client.player, target);
        });
    }

    private Optional<Entity> findTarget(MinecraftClient client) {
        double r = range.getValueDouble();
        return client.world.getEntities().stream()
                .filter(e -> e != client.player && e instanceof LivingEntity)
                .filter(e -> client.player.distanceTo(e) <= r && !e.isRemoved())
                .filter(e -> {
                    LivingEntity le = (LivingEntity) e;
                    if (le.isDead() || le.getHealth() <= 0) return false;
                    if (e instanceof PlayerEntity p && noFriends.isEnabled() && isFriend(p)) return false;
                    if (onlyPlayers.isEnabled() && !(e instanceof PlayerEntity)) return false;
                    return true;
                })
                .min(Comparator.comparingDouble(e -> client.player.distanceTo(e)));
    }

    private boolean isFriend(PlayerEntity p) {
        return mc.player.getScoreboardTeam() != null && p.getScoreboardTeam() == mc.player.getScoreboardTeam();
    }

    private void faceEntity(Entity target) {
        if (mc.player == null) return;
        Vec3d p = mc.player.getEyePos();
        Vec3d t = target.getBoundingBox().getCenter();
        double dx = t.x - p.x, dy = (t.y + target.getHeight() * 0.3) - p.y, dz = t.z - p.z;
        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float) Math.toDegrees(Math.atan2(-dx, -dz));
        float pitch = (float) Math.toDegrees(-Math.atan2(dy, dist));
        mc.player.setYaw(lerpAngle(mc.player.getYaw(), yaw, 0.3f));
        mc.player.setPitch(lerpAngle(mc.player.getPitch(), pitch, 0.3f));
    }

    private float lerpAngle(float from, float to, float speed) {
        float diff = MathHelper.wrapDegrees(to - from);
        if (Math.abs(diff) < 0.1f) return to;
        return from + diff * Math.min(speed, 1.0f);
    }
}

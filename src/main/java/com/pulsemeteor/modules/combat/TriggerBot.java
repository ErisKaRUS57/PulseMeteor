package com.pulsemeteor.modules.combat;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

/**
 * TriggerBot - automatically attacks when crosshair is over an entity.
 */
public class TriggerBot extends Module {
    private final NumberSetting delay;
    private final BooleanSetting onlyPlayers;
    private final BooleanSetting requireSword;

    private int clickDelay = 0;

    public TriggerBot() {
        super("TriggerBot", "Attacks when crosshair is over an entity", Category.COMBAT);
        this.delay = createNumber("Delay", "Delay between attacks in ticks", 2, 0, 10, 1);
        this.onlyPlayers = createBoolean("Only Players", "Only trigger on players", true);
        this.requireSword = createBoolean("Require Sword", "Only trigger holding a sword", false);
    }

    @Override
    public void onTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;

        if (clickDelay > 0) { clickDelay--; return; }

        if (requireSword.isEnabled()) {
            String itemId = client.player.getMainHandStack().getItem().getTranslationKey();
            if (!itemId.contains("sword")) return;
        }

        if (client.crosshairTarget != null && client.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            EntityHitResult hit = (EntityHitResult) client.crosshairTarget;
            Entity target = hit.getEntity();

            if (target instanceof LivingEntity living && !living.isDead() && living.getHealth() > 0) {
                if (target == client.player) return;
                if (onlyPlayers.isEnabled()) {
                    if (!(target instanceof net.minecraft.entity.player.PlayerEntity)) return;
                }

                client.player.swingHand(Hand.MAIN_HAND);
                client.interactionManager.attackEntity(client.player, target);
                clickDelay = delay.getValueInt();
            }
        }
    }
}

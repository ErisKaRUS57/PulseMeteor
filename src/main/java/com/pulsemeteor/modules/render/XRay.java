package com.pulsemeteor.modules.render;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;

import java.util.HashSet;
import java.util.Set;

/**
 * XRay - shows ores through stone by making other blocks transparent.
 */
public class XRay extends Module {
    private static final Set<Block> ORES = new HashSet<>();

    static {
        ORES.add(Blocks.COAL_ORE);
        ORES.add(Blocks.DEEPSLATE_COAL_ORE);
        ORES.add(Blocks.IRON_ORE);
        ORES.add(Blocks.DEEPSLATE_IRON_ORE);
        ORES.add(Blocks.GOLD_ORE);
        ORES.add(Blocks.DEEPSLATE_GOLD_ORE);
        ORES.add(Blocks.DIAMOND_ORE);
        ORES.add(Blocks.DEEPSLATE_DIAMOND_ORE);
        ORES.add(Blocks.EMERALD_ORE);
        ORES.add(Blocks.DEEPSLATE_EMERALD_ORE);
        ORES.add(Blocks.LAPIS_ORE);
        ORES.add(Blocks.DEEPSLATE_LAPIS_ORE);
        ORES.add(Blocks.REDSTONE_ORE);
        ORES.add(Blocks.DEEPSLATE_REDSTONE_ORE);
        ORES.add(Blocks.COPPER_ORE);
        ORES.add(Blocks.DEEPSLATE_COPPER_ORE);
        ORES.add(Blocks.NETHER_GOLD_ORE);
        ORES.add(Blocks.NETHER_QUARTZ_ORE);
        ORES.add(Blocks.ANCIENT_DEBRIS);
        ORES.add(Blocks.RAW_IRON_BLOCK);
        ORES.add(Blocks.RAW_GOLD_BLOCK);
        ORES.add(Blocks.RAW_COPPER_BLOCK);
    }

    private final BooleanSetting showStone;
    private final BooleanSetting showDirt;

    public XRay() {
        super("XRay", "Show ores through walls", Category.RENDER);
        this.showStone = createBoolean("Show Stone", "Show stone blocks", false);
        this.showDirt = createBoolean("Show Dirt", "Show dirt blocks", false);
    }

    public static boolean isOre(Block block) {
        return ORES.contains(block);
    }

    public boolean shouldShowBlock(Block block) {
        if (ORES.contains(block)) return true;
        if (showStone.isEnabled()) {
            if (block == Blocks.STONE || block == Blocks.DEEPSLATE || block == Blocks.TUFF
                    || block == Blocks.GRANITE || block == Blocks.DIORITE || block == Blocks.ANDESITE)
                return true;
        }
        if (showDirt.isEnabled()) {
            if (block == Blocks.DIRT || block == Blocks.GRASS_BLOCK || block == Blocks.GRAVEL
                    || block == Blocks.SAND || block == Blocks.SANDSTONE)
                return true;
        }
        return false;
    }

    @Override
    public void onEnable() {
        // Force world re-render to apply XRay
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        if (mc.worldRenderer != null) {
            mc.worldRenderer.reload();
        }
    }

    @Override
    public void onTick(MinecraftClient client) {
        // XRay is primarily handled via mixin in block rendering
    }
}

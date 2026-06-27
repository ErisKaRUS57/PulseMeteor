package com.pulsemeteor.modules.render;

import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.BooleanSetting;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.block.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.awt.Color;

/**
 * StorageESP - highlights chests, shulkers, and other storage blocks.
 */
public class StorageESP extends Module {
    private final BooleanSetting chests;
    private final BooleanSetting shulkers;
    private final BooleanSetting barrels;
    private final BooleanSetting enderChests;
    private final BooleanSetting furnaces;
    private final NumberSetting alpha;

    public StorageESP() {
        super("StorageESP", "Highlight storage containers", Category.RENDER);
        this.chests = createBoolean("Chests", "Highlight chests", true);
        this.shulkers = createBoolean("Shulkers", "Highlight shulker boxes", true);
        this.barrels = createBoolean("Barrels", "Highlight barrels", true);
        this.enderChests = createBoolean("Ender Chests", "Highlight ender chests", true);
        this.furnaces = createBoolean("Furnaces", "Highlight furnaces", false);
        this.alpha = createNumber("Alpha", "Highlight opacity", 100, 0, 255, 1);
    }

    @Override
    public void onWorldRender(float tickDelta) {
        if (mc.world == null || mc.player == null) return;

        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        int radius = 32;
        BlockPos center = mc.player.getBlockPos();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.begin(VertexFormat.DrawMode.QUADS,
                VertexFormats.POSITION_COLOR);

        for (BlockPos pos : BlockPos.iterateOutwards(center, radius, radius, radius)) {
            BlockState state = mc.world.getBlockState(pos);
            if (!isStorageBlock(state)) continue;

            Color color = getColorForBlock(state.getBlock());
            Box box = new Box(pos).offset(-camera.x, -camera.y, -camera.z);
            drawBox(buffer, box, color);
        }

        BufferRenderer.drawWithGlobalProgram(buffer.end());
        RenderSystem.setShader(GameRenderer::getPositionProgram);
    }

    private boolean isStorageBlock(BlockState state) {
        Block b = state.getBlock();
        if (b instanceof ChestBlock && chests.isEnabled()) return true;
        if (b instanceof ShulkerBoxBlock && shulkers.isEnabled()) return true;
        if (b instanceof BarrelBlock && barrels.isEnabled()) return true;
        if (b instanceof EnderChestBlock && enderChests.isEnabled()) return true;
        if (b instanceof AbstractFurnaceBlock && furnaces.isEnabled()) return true;
        return false;
    }

    private Color getColorForBlock(Block block) {
        if (block instanceof ChestBlock) return new Color(255, 255, 0, alpha.getValueInt());
        if (block instanceof EnderChestBlock) return new Color(128, 0, 255, alpha.getValueInt());
        if (block instanceof BarrelBlock) return new Color(139, 90, 43, alpha.getValueInt());
        if (block instanceof ShulkerBoxBlock) return new Color(255, 128, 255, alpha.getValueInt());
        if (block instanceof AbstractFurnaceBlock) return new Color(128, 128, 128, alpha.getValueInt());
        return new Color(255, 255, 255, alpha.getValueInt());
    }

    private void drawBox(BufferBuilder buffer, Box box, Color color) {
        float r = color.getRed() / 255f;
        float g = color.getGreen() / 255f;
        float b = color.getBlue() / 255f;
        float a = color.getAlpha() / 255f;

        // Simple box faces
        // Bottom
        buffer.vertex(box.minX, box.minY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.minY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.minY, box.maxZ).color(r, g, b, a);
        buffer.vertex(box.minX, box.minY, box.maxZ).color(r, g, b, a);
        // Top
        buffer.vertex(box.minX, box.maxY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.maxY, box.minZ).color(r, g, b, a);
        buffer.vertex(box.maxX, box.maxY, box.maxZ).color(r, g, b, a);
        buffer.vertex(box.minX, box.maxY, box.maxZ).color(r, g, b, a);
    }
}

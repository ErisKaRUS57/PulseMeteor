package com.pulsemeteor.hud.elements;

import com.pulsemeteor.hud.HUDElement;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.modules.ModuleManager;
import com.pulsemeteor.PulseMeteor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CombatInfoHUD / ArrayList - displays active modules in the top-right corner.
 * Shows the enabled modules with Pulse Visuals styling (neon colors, smooth).
 */
public class CombatInfoHUD extends HUDElement {
    private static final int TEXT_COLOR = 0xFF00FFAA;
    private static final int TEXT_COLOR_DISABLED = 0xFF888888;

    public CombatInfoHUD() {
        super("Module List", 4, 80, 100, 100);
    }

    @Override
    public void render(DrawContext context, float tickDelta) {
        if (mc.player == null) return;

        ModuleManager moduleManager = PulseMeteor.getInstance().getModuleManager();
        List<Module> activeModules = moduleManager.getModules().stream()
                .filter(Module::isEnabled)
                .filter(Module::isVisible)
                .sorted((a, b) -> {
                    int cmp = b.getName().length() - a.getName().length();
                    return cmp != 0 ? cmp : a.getName().compareTo(b.getName());
                })
                .collect(Collectors.toList());

        int yOffset = y;
        int maxWidth = 0;

        // Use right-aligned positioning if x is on the right side of screen
        int screenWidth = mc.getWindow().getScaledWidth();
        boolean rightAligned = x > screenWidth / 2;

        for (Module module : activeModules) {
            String text = module.getName();
            int textWidth = mc.textRenderer.getWidth(text);
            int displayX = rightAligned ? screenWidth - textWidth - 4 : x;
            int bgWidth = textWidth + 8;

            // Background pill
            context.fill(displayX, yOffset, displayX + bgWidth, yOffset + 12, 0x80000000);

            // Module name with category color
            int color = module.getCategory().getColor();
            context.drawText(mc.textRenderer, text, displayX + 4, yOffset + 2, color, false);

            yOffset += 14;
            maxWidth = Math.max(maxWidth, bgWidth);
        }

        this.width = maxWidth;
        this.height = yOffset - y;
    }
}

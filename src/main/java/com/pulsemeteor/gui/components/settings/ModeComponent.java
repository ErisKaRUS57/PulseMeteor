package com.pulsemeteor.gui.components.settings;

import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.settings.ModeSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * GUI component for mode/enum settings.
 * Clicking cycles through available modes.
 */
public class ModeComponent {
    private final ModeSetting setting;

    public ModeComponent(ModeSetting setting) {
        this.setting = setting;
    }

    public void render(DrawContext context, int x, int y, int width, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Background
        context.fill(x, y, x + width, y + 12, 0x80111111);

        // Cycle indicator (right side text)
        String value = setting.getValue();
        String displayText = setting.getName() + ": " + value;

        int textColor = 0xFFFFFFFF;
        context.drawText(mc.textRenderer, displayText, x + 2, y + 2, textColor, false);

        // Mode indicator dots
        int dotX = x + width - 6;
        int dotY = y + 4;
        int dotSize = 4;
        for (int i = 0; i < setting.getModes().size(); i++) {
            int color = i == setting.getIndex() ? ClickGUI.ACCENT_COLOR : ClickGUI.DISABLED_COLOR;
            context.fill(dotX, dotY, dotX + dotSize, dotY + dotSize, color);
            dotX -= 6;
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button, int x, int y, int width) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12) {
            if (button == 0) {
                setting.cycle();
                return true;
            } else if (button == 1) {
                setting.cycleBackward();
                return true;
            }
        }
        return false;
    }
}

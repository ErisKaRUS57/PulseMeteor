package com.pulsemeteor.gui.components.settings;

import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.settings.NumberSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * GUI component for numeric settings with a slider.
 */
public class NumberComponent {
    private final NumberSetting setting;
    private boolean sliding = false;

    public NumberComponent(NumberSetting setting) {
        this.setting = setting;
    }

    public void render(DrawContext context, int x, int y, int width, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();

        // Background
        context.fill(x, y, x + width, y + 12, 0x80111111);

        // Slider track
        int trackY = y + 4;
        int trackHeight = 4;
        context.fill(x + 2, trackY, x + width - 2, trackY + trackHeight, 0xFF333333);

        // Slider fill
        double range = setting.getMax().doubleValue() - setting.getMin().doubleValue();
        double progress = range > 0 ? (setting.getValueDouble() - setting.getMin().doubleValue()) / range : 0;
        int fillWidth = (int) ((width - 4) * progress);
        context.fill(x + 2, trackY, x + 2 + fillWidth, trackY + trackHeight, ClickGUI.ACCENT_COLOR);

        // Dragging
        if (sliding) {
            double newProgress = Math.max(0, Math.min(1,
                    (mouseX - (x + 2)) / (double) (width - 4)));
            double newValue = setting.getMin().doubleValue() + newProgress * range;

            if (setting.getValue() instanceof Integer) {
                setting.setValue((int) Math.round(newValue));
            } else {
                setting.setValue(newValue);
            }
        }

        // Name and value text
        String displayName = setting.getName() + ": " + String.format("%.1f", setting.getValueDouble());
        context.drawText(mc.textRenderer, displayName, x + 2, y - 1, 0xFFFFFFFF, false);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button, int x, int y, int width) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12) {
            if (button == 0) {
                sliding = true;
                return true;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        sliding = false;
    }
}

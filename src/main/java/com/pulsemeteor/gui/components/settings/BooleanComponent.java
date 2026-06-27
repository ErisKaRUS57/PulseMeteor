package com.pulsemeteor.gui.components.settings;

import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.settings.BooleanSetting;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * GUI component for boolean (toggle) settings.
 */
public class BooleanComponent {
    private final BooleanSetting setting;

    public BooleanComponent(BooleanSetting setting) {
        this.setting = setting;
    }

    public void render(DrawContext context, int x, int y, int width, int mouseX, int mouseY, float delta) {
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12;

        // Background
        context.fill(x, y, x + width, y + 12, 0x80111111);

        // Toggle indicator
        int toggleColor = setting.isEnabled() ? ClickGUI.ENABLED_COLOR : ClickGUI.DISABLED_COLOR;
        context.fill(x + width - 10, y + 2, x + width - 2, y + 10, toggleColor);

        // Name
        String displayName = setting.getName();
        context.drawText(MinecraftClient.getInstance().textRenderer, displayName,
                x + 2, y + 2, 0xFFFFFFFF, false);
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button, int x, int y, int width) {
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12) {
            if (button == 0) {
                setting.toggle();
                return true;
            }
        }
        return false;
    }
}

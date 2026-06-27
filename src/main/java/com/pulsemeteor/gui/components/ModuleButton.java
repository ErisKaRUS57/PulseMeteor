package com.pulsemeteor.gui.components;

import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.gui.components.settings.BooleanComponent;
import com.pulsemeteor.gui.components.settings.ModeComponent;
import com.pulsemeteor.gui.components.settings.NumberComponent;
import com.pulsemeteor.modules.Module;
import com.pulsemeteor.settings.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

/**
 * ModuleButton - represents a single module in the ClickGUI.
 * Can be toggled and expanded to reveal settings.
 */
public class ModuleButton {
    private final Module module;
    private final List<SettingComponent<?>> settingComponents = new ArrayList<>();
    private int x, y;
    private final int width;
    private int height = 12;
    private boolean expanded = false;
    private boolean listeningForKeybind = false;

    public ModuleButton(Module module, int x, int y, int width) {
        this.module = module;
        this.x = x;
        this.y = y;
        this.width = width;

        // Create setting components
        for (Setting<?> setting : module.getSettings()) {
            if (setting.getName().equalsIgnoreCase("Enabled")
                    || setting.getName().equalsIgnoreCase("Visible")) continue;

            if (setting instanceof BooleanSetting b) {
                settingComponents.add(new BooleanComponent(b));
            } else if (setting instanceof NumberSetting n) {
                settingComponents.add(new NumberComponent(n));
            } else if (setting instanceof ModeSetting m) {
                settingComponents.add(new ModeComponent(m));
            }
        }

        // Keybind display line
        this.height = 12 + settingComponents.size() * 16;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        MinecraftClient mc = MinecraftClient.getInstance();
        boolean hovered = mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        // Module button background
        int bgColor = module.isEnabled() ? ClickGUI.ACCENT_COLOR : ClickGUI.DISABLED_COLOR;
        if (hovered) {
            bgColor = module.isEnabled() ? 0xFF00CC88 : 0xFF555555;
        }
        context.fill(x, y, x + width, y + 12, bgColor);

        // Module name
        int textColor = module.isEnabled() ? 0xFFFFFFFF : ClickGUI.MUTED_TEXT_COLOR;
        context.drawText(mc.textRenderer, module.getName(), x + 2, y + 2, textColor, false);

        // Keybind hint
        String keybind = module.getKeyBindString();
        if (!keybind.equals("NONE")) {
            context.drawText(mc.textRenderer, keybind,
                    x + width - mc.textRenderer.getWidth(keybind) - 2, y + 2,
                    0xFFAAAAAA, false);
        }

        // Listening indicator
        if (listeningForKeybind) {
            context.fill(x + width + 2, y, x + width + 80, y + 12, 0x80000000);
            context.drawText(mc.textRenderer, "Press a key...", x + width + 4, y + 2, 0xFFFFAA00, false);
        }

        // Settings
        if (expanded) {
            int settingY = y + 14;
            for (SettingComponent<?> component : settingComponents) {
                component.render(context, x + 2, settingY, width - 4, mouseX, mouseY, delta);
                settingY += 14;
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        // Main button click
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 12) {
            if (button == 0) {
                module.toggle();
                return true;
            } else if (button == 1) {
                expanded = !expanded;
                return true;
            } else if (button == 2) {
                listeningForKeybind = true;
                return true;
            }
        }

        // Settings interaction
        if (expanded) {
            int settingY = y + 14;
            for (SettingComponent<?> component : settingComponents) {
                if (component.mouseClicked(mouseX, mouseY, button, x + 2, settingY, width - 4)) {
                    return true;
                }
                settingY += 14;
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (expanded) {
            for (SettingComponent<?> component : settingComponents) {
                component.mouseReleased(mouseX, mouseY, button);
            }
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (listeningForKeybind) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE || keyCode == GLFW.GLFW_KEY_DELETE) {
                module.setKeyCode(GLFW.GLFW_KEY_UNKNOWN);
            } else {
                module.setKeyCode(keyCode);
            }
            listeningForKeybind = false;
            return true;
        }
        return false;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        if (expanded) {
            return 14 + settingComponents.size() * 14;
        }
        return 12;
    }

    /**
     * Abstract base for setting GUI components.
     */
    private abstract static class SettingComponent<T extends Setting<?>> {
        protected final T setting;

        public SettingComponent(T setting) {
            this.setting = setting;
        }

        public abstract void render(DrawContext context, int x, int y, int width, int mouseX, int mouseY, float delta);
        public abstract boolean mouseClicked(int mouseX, int mouseY, int button, int x, int y, int width);
        public void mouseReleased(int mouseX, int mouseY, int button) {}
    }
}

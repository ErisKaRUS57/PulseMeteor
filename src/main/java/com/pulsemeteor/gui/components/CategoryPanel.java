package com.pulsemeteor.gui.components;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.gui.ClickGUI;
import com.pulsemeteor.modules.Category;
import com.pulsemeteor.modules.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

import java.util.ArrayList;
import java.util.List;

/**
 * CategoryPanel - a collapsible panel in the ClickGUI for each module category.
 * Contains module buttons that can be toggled and configured.
 */
public class CategoryPanel {
    private final Category category;
    private final List<ModuleButton> moduleButtons = new ArrayList<>();
    private int x, y;
    private int width;
    private int height;
    private boolean expanded = true;
    private boolean dragging = false;
    private int dragOffsetX, dragOffsetY;

    public CategoryPanel(Category category, int x, int y, int width) {
        this.category = category;
        this.x = x;
        this.y = y;
        this.width = width;

        // Create module buttons
        int buttonY = y + 22;
        for (Module module : PulseMeteor.getInstance().getModuleManager().getModulesInCategory(category)) {
            moduleButtons.add(new ModuleButton(module, x + 2, buttonY, width - 4));
            buttonY += 14;
        }
        this.height = buttonY - y;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Category header
        context.fill(x, y, x + width, y + 20, ClickGUI.SECONDARY_COLOR);

        // Accent line
        context.fill(x, y + 18, x + width, y + 20, category.getColor());

        // Category name
        String name = category.getName();
        context.drawText(MinecraftClient.getInstance().textRenderer, name,
                x + 4, y + 4, category.getColor(), false);

        // Module buttons
        if (expanded) {
            int buttonY = y + 22;
            for (ModuleButton button : moduleButtons) {
                button.setY(buttonY);
                button.render(context, mouseX, mouseY, delta);
                buttonY += button.getHeight() + 1;
            }
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int button) {
        // Header drag and expand
        if (mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + 20) {
            if (button == 0) {
                dragging = true;
                dragOffsetX = mouseX - x;
                dragOffsetY = mouseY - y;
                return true;
            } else if (button == 1) {
                expanded = !expanded;
                return true;
            }
        }

        // Module buttons
        if (expanded) {
            for (ModuleButton moduleButton : moduleButtons) {
                if (moduleButton.mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void mouseReleased(int mouseX, int mouseY, int button) {
        if (button == 0) {
            dragging = false;
        }
        for (ModuleButton moduleButton : moduleButtons) {
            moduleButton.mouseReleased(mouseX, mouseY, button);
        }
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (ModuleButton moduleButton : moduleButtons) {
            if (moduleButton.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return false;
    }
}

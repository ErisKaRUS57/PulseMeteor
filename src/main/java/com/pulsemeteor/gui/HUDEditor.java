package com.pulsemeteor.gui;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.hud.HUDElement;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.List;

/**
 * HUD Editor - allows users to drag and position HUD elements.
 */
public class HUDEditor extends Screen {
    private HUDElement selectedElement = null;
    private int dragOffsetX, dragOffsetY;
    private boolean dragging = false;

    private static HUDEditor INSTANCE;

    public HUDEditor() {
        super(Text.literal("Pulse Meteor HUD Editor"));
        INSTANCE = this;
    }

    public static HUDEditor getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new HUDEditor();
        }
        return INSTANCE;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Dim background
        context.fillGradient(0, 0, this.width, this.height, 0x80000000, 0x40000000);

        // Render all HUD elements with edit overlays
        List<HUDElement> elements = PulseMeteorClient.getInstance().getHudManager().getElements();

        for (HUDElement element : elements) {
            if (!element.isVisible()) continue;

            // Draw element background
            context.fill(element.getX(), element.getY(),
                    element.getX() + element.getWidth(), element.getY() + element.getHeight(),
                    0x40FFFFFF);

            // Draw element border
            boolean hovered = element.isMouseOver(mouseX, mouseY);
            int borderColor = hovered ? 0xFF00FFAA : 0x60888888;
            context.fill(element.getX(), element.getY(),
                    element.getX() + element.getWidth(), element.getY() + 1, borderColor);
            context.fill(element.getX(), element.getY() + element.getHeight() - 1,
                    element.getX() + element.getWidth(), element.getY() + element.getHeight(), borderColor);
            context.fill(element.getX(), element.getY(),
                    element.getX() + 1, element.getY() + element.getHeight(), borderColor);
            context.fill(element.getX() + element.getWidth() - 1, element.getY(),
                    element.getX() + element.getWidth(), element.getY() + element.getHeight(), borderColor);

            // Draw element name
            context.drawText(textRenderer, element.getName(),
                    element.getX() + 2, element.getY() + 2, 0xFFFFFFFF, true);
        }

        // Drag info
        if (selectedElement != null) {
            context.drawText(textRenderer,
                    "Dragging: " + selectedElement.getName(),
                    10, 10, 0xFF00FFAA, true);
        }

        // Instructions
        context.drawText(textRenderer,
                "Drag elements to reposition • Right-click to toggle visibility",
                10, this.height - 20, 0xFF888888, true);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        List<HUDElement> elements = PulseMeteorClient.getInstance().getHudManager().getElements();

        // Find clicked element (reverse order for correct z-index)
        for (int i = elements.size() - 1; i >= 0; i--) {
            HUDElement element = elements.get(i);
            if (!element.isVisible() || !element.isDraggable()) continue;

            if (element.isMouseOver((int) mouseX, (int) mouseY)) {
                if (button == 0) {
                    selectedElement = element;
                    dragOffsetX = (int) mouseX - element.getX();
                    dragOffsetY = (int) mouseY - element.getY();
                    dragging = true;
                    return true;
                } else if (button == 1) {
                    element.setVisible(!element.isVisible());
                    return true;
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && dragging) {
            dragging = false;
            selectedElement = null;
            PulseMeteor.getInstance().getConfigManager().save();
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (button == 0 && dragging && selectedElement != null) {
            int newX = (int) mouseX - dragOffsetX;
            int newY = (int) mouseY - dragOffsetY;

            // Clamp to screen bounds
            newX = Math.max(0, Math.min(newX, width - selectedElement.getWidth()));
            newY = Math.max(0, Math.min(newY, height - selectedElement.getHeight()));

            selectedElement.setPosition(newX, newY);
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void close() {
        PulseMeteor.getInstance().getConfigManager().save();
        super.close();
    }
}

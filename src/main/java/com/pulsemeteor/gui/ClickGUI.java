package com.pulsemeteor.gui;

import com.pulsemeteor.PulseMeteor;
import com.pulsemeteor.gui.components.CategoryPanel;
import com.pulsemeteor.modules.Category;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Pulse Meteor ClickGUI - a modern, sleek GUI for toggling modules and adjusting settings.
 * Visually consistent with Pulse Visuals aesthetic (neon accents, dark background).
 */
public class ClickGUI extends Screen {
    private final List<CategoryPanel> panels = new ArrayList<>();
    private boolean dragging = false;
    private int dragX, dragY;

    // Theme colors
    public static final int BACKGROUND_COLOR = 0xCC0A0A0A;
    public static final int ACCENT_COLOR = 0xFF00FFAA;
    public static final int SECONDARY_COLOR = 0xFF1A1A2E;
    public static final int TEXT_COLOR = 0xFFFFFFFF;
    public static final int MUTED_TEXT_COLOR = 0xFF888888;
    public static final int ENABLED_COLOR = 0xFF00FFAA;
    public static final int DISABLED_COLOR = 0xFF444444;

    private static ClickGUI INSTANCE;

    public ClickGUI() {
        super(Text.literal("Pulse Meteor ClickGUI"));
        INSTANCE = this;
    }

    public static ClickGUI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ClickGUI();
        }
        return INSTANCE;
    }

    @Override
    protected void init() {
        super.init();
        panels.clear();

        int panelX = 10;
        int panelY = 10;
        int panelWidth = 110;

        for (Category category : Category.values()) {
            panels.add(new CategoryPanel(category, panelX, panelY, panelWidth));
            panelX += panelWidth + 5;

            // Wrap to next row if needed
            if (panelX + panelWidth > this.width) {
                panelX = 10;
                panelY += 200;
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Background overlay
        context.fillGradient(0, 0, this.width, this.height, 0x60000000, 0x40000000);

        // Render panels
        for (CategoryPanel panel : panels) {
            panel.render(context, mouseX, mouseY, delta);
        }

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            if (panel.mouseClicked((int) mouseX, (int) mouseY, button)) {
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        for (CategoryPanel panel : panels) {
            panel.mouseReleased((int) mouseX, (int) mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (CategoryPanel panel : panels) {
            if (panel.keyPressed(keyCode, scanCode, modifiers)) return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
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

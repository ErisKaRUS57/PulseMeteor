package com.pulsemeteor.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;

/**
 * Base class for all HUD elements in the Pulse Meteor HUD system.
 * Each element has a position, size, visibility toggle, and render method.
 */
public abstract class HUDElement {
    protected static final MinecraftClient mc = MinecraftClient.getInstance();

    protected final String name;
    protected int x, y;
    protected int width, height;
    protected boolean visible = true;
    protected boolean draggable = true;

    public HUDElement(String name, int defaultX, int defaultY, int defaultWidth, int defaultHeight) {
        this.name = name;
        this.x = defaultX;
        this.y = defaultY;
        this.width = defaultWidth;
        this.height = defaultHeight;
    }

    public abstract void render(DrawContext context, float tickDelta);

    public String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isDraggable() {
        return draggable;
    }

    public boolean isMouseOver(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

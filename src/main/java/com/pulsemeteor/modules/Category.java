package com.pulsemeteor.modules;

/**
 * Defines the categories for module organization in the ClickGUI.
 * Matches the Meteor Client style categories.
 */
public enum Category {
    COMBAT("Combat", 0xFFE74C3C),
    MOVEMENT("Movement", 0xFF3498DB),
    PLAYER("Player", 0xFF2ECC71),
    RENDER("Render", 0xFF9B59B6),
    WORLD("World", 0xFFE67E22),
    UTILITY("Utility", 0xFF95A5A6);

    private final String name;
    private final int color;

    Category(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getColor() {
        return color;
    }
}

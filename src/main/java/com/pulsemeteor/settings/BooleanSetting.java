package com.pulsemeteor.settings;

/**
 * Boolean setting with a toggle state.
 */
public class BooleanSetting extends Setting<Boolean> {

    public BooleanSetting(String name, String description, boolean defaultValue) {
        super(name, description, defaultValue);
    }

    public void toggle() {
        setValue(!getValue());
    }

    public boolean isEnabled() {
        return getValue();
    }
}

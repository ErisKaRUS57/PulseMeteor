package com.pulsemeteor.settings;

import java.util.Arrays;
import java.util.List;

/**
 * Mode/Enum setting for selecting between predefined options.
 */
public class ModeSetting extends Setting<String> {
    private final List<String> modes;
    private int index;

    public ModeSetting(String name, String description, String defaultValue, String... modes) {
        super(name, description, defaultValue);
        this.modes = Arrays.asList(modes);
        this.index = this.modes.indexOf(defaultValue);
        if (this.index < 0) this.index = 0;
    }

    public List<String> getModes() {
        return modes;
    }

    public int getIndex() {
        return index;
    }

    public void cycle() {
        index = (index + 1) % modes.size();
        setValue(modes.get(index));
    }

    public void cycleBackward() {
        index = (index - 1 + modes.size()) % modes.size();
        setValue(modes.get(index));
    }

    public boolean isMode(String mode) {
        return getValue().equalsIgnoreCase(mode);
    }

    @Override
    public void setValue(String value) {
        int newIndex = modes.indexOf(value);
        if (newIndex >= 0) {
            this.index = newIndex;
            super.setValue(value);
        }
    }
}

package com.pulsemeteor.settings;

/**
 * Numeric setting with min, max, and step values.
 * Supports integers and doubles for slider-based configuration.
 */
public class NumberSetting extends Setting<Number> {
    private final Number min;
    private final Number max;
    private final Number step;

    public NumberSetting(String name, String description, Number defaultValue, Number min, Number max, Number step) {
        super(name, description, defaultValue);
        this.min = min;
        this.max = max;
        this.step = step;
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public Number getStep() {
        return step;
    }

    public double getValueDouble() {
        return getValue().doubleValue();
    }

    public int getValueInt() {
        return getValue().intValue();
    }

    public float getValueFloat() {
        return getValue().floatValue();
    }

    @Override
    public void setValue(Number value) {
        double clamped = Math.max(min.doubleValue(), Math.min(max.doubleValue(), value.doubleValue()));
        if (step.doubleValue() > 0) {
            clamped = Math.round(clamped / step.doubleValue()) * step.doubleValue();
        }
        if (value instanceof Integer) {
            super.setValue((int) clamped);
        } else if (value instanceof Float) {
            super.setValue((float) clamped);
        } else {
            super.setValue(clamped);
        }
    }
}

package dev.tonimatas.config;

public class ConfigValue {
    private final String value;

    protected ConfigValue(String value) {
        this.value = value;
    }

    public String get() {
        return value;
    }

    public boolean toBoolean() {
        return Boolean.getBoolean(value);
    }

    public int toInt() {
        return Integer.parseInt(value);
    }

    public long toLong() {
        return Long.parseLong(value);
    }
}

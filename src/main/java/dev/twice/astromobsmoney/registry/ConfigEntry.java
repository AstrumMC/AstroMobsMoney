package dev.twice.astromobsmoney.registry;

import lombok.Getter;

@Getter
public class ConfigEntry<T> {
    private final String path;
    private final T value;
    private final boolean persistent;

    public ConfigEntry(String path, T value, boolean persistent) {
        this.path = path;
        this.value = value;
        this.persistent = persistent;
    }

    public ConfigEntry(String path, T value) {
        this(path, value, false);
    }
}
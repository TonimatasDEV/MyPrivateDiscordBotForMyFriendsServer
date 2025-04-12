package dev.tonimatas.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

public final class ConfigFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigFile.class);
    private final Properties properties;
    private final String name;
    private final Map<String, String> defaultValues;
    private final Path path;

    public ConfigFile(String name) {
        this(name, Map.of());
    }

    public ConfigFile(String name, Map<String, String> defaultValues) {
        this.properties = new Properties();
        this.name = name;
        this.defaultValues = defaultValues;
        this.path = Paths.get(name + ".properties");

        create();
        load();
        save();
    }

    public ConfigValue getValue(String key) {
        return new ConfigValue(properties.getProperty(key));
    }

    public void setValue(String key, Object value) {
        properties.setProperty(key, value.toString());
        save();
    }

    private void create() {
        if (Files.exists(path)) return;

        try {
            Files.createFile(path);

            for (String key : defaultValues.keySet()) {
                properties.setProperty(key, defaultValues.get(key));
            }
        } catch (IOException e) {
            LOGGER.error("Error creating the config: {}", name);
            throw new RuntimeException(e);
        }
    }

    private void load() {
        try {
            properties.load(Files.newInputStream(path));
        } catch (IOException e) {
            LOGGER.error("Error loading the config: {}", name);
            throw new RuntimeException(e);
        }
    }

    public void save() {
        try {
            properties.store(Files.newOutputStream(path), "Config file: " + name);
        } catch (IOException e) {
            LOGGER.error("Error saving the config: {}", name);
        }
    }
}

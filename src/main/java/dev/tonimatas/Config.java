package dev.tonimatas;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Properties;

public class Config {
    private static Properties properties = null;

    public static void runProperties() {
        try {
            File propertiesFile = new File("bot.properties");
            properties = new Properties();

            if (!propertiesFile.exists()) {
                Files.createFile(propertiesFile.toPath());

                properties.setProperty("token", "");
                properties.setProperty("count", "0");

                save();
            }

            properties.load(Files.newInputStream(propertiesFile.toPath()));

            checkVariable("token", "");
            save();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getToken() {
        if (properties == null) runProperties();
        return properties.getProperty("token");
    }

    public static long getCount() {
        if (properties == null) runProperties();
        return Long.parseLong(properties.getProperty("count"));
    }
    
    public static void setCount(long count) {
        if (properties == null) runProperties();
        properties.setProperty("count", String.valueOf(count));
        save();
    }

    @SuppressWarnings("SameParameterValue")
    private static void checkVariable(String variable, String defaultValue) {
        if (properties.getProperty(variable) == null) {
            properties.setProperty(variable, defaultValue);
        }
    }

    private static void save() {
        try {
            properties.store(Files.newOutputStream(new File("bot.properties").toPath()),
                    "El Resistente config file.");
        } catch (IOException e) {
            System.out.println("Error saving config.");
        }
    }
}

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

                save(propertiesFile);
            }

            properties.load(Files.newInputStream(propertiesFile.toPath()));

            checkVariable("token", "");
            save(propertiesFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getToken() {
        if (properties == null) runProperties();
        return properties.getProperty("token");
    }

    @SuppressWarnings("SameParameterValue")
    private static void checkVariable(String variable, String defaultValue) {
        if (properties.getProperty(variable) == null) {
            properties.setProperty(variable, defaultValue);
        }
    }

    private static void save(File propertiesFile) throws IOException {
        properties.store(Files.newOutputStream(propertiesFile.toPath()),
                "El Resistente config file.");
    }
}

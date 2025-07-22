package dev.tonimatas.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public abstract class JsonFile {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonFile.class);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final Object saveLock = new Object();

    private static <T extends JsonFile> T load(Class<T> clazz, String path) {
        try (FileReader reader = new FileReader(path)) {
            return GSON.fromJson(reader, clazz);
        } catch (IOException e) {
            LOGGER.error("Error loading {} config file. {}", path, e.getMessage());
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static <T extends JsonFile> T loadOrCreate(Class<T> clazz, String path) {
        File file = new File(path);

        if (!file.exists()) {
            if (path.split("/").length > 1) {
                boolean folderCreated = Paths.get(path).getParent().toFile().mkdirs();

                if (!folderCreated) {
                    LOGGER.debug("Error creating {} folder.", path);
                }
            }

            try {
                T instance = clazz.getDeclaredConstructor().newInstance();
                instance.save();
                return instance;
            } catch (Exception e) {
                LOGGER.error("Error creating {} config file. {}", path, e.getMessage());
                Runtime.getRuntime().exit(-1);
            }
        }

        return load(clazz, path);
    }

    protected abstract String getFilePath();

    public void save() {
        synchronized (saveLock) {
            Path tempPath = Paths.get(getFilePath() + ".tmp");
            Path finalPath = Paths.get(getFilePath());

            try (BufferedWriter writer = Files.newBufferedWriter(tempPath, StandardCharsets.UTF_8)) {
                GSON.toJson(this, writer);
            } catch (IOException e) {
                LOGGER.error("Error writing temporary config file {}. {}", tempPath, e.getMessage());
                return;
            }

            try {
                Files.move(tempPath, finalPath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                LOGGER.error("Error replacing config file {}. {}", finalPath, e.getMessage());
            }
        }
    }
}

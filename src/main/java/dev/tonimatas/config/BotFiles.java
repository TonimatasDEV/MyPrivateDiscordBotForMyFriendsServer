package dev.tonimatas.config;

import dev.tonimatas.config.config.BotConfig;
import dev.tonimatas.config.data.ExtraData;
import dev.tonimatas.config.data.UserData;
import dev.tonimatas.systems.executors.ExecutorManager;

import java.util.concurrent.TimeUnit;

public class BotFiles {
    public static final BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static final UserData USER = JsonFile.loadOrCreate(UserData.class, "data/user.json");
    public static final ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");

    public static void autosave() {
        ExecutorManager.addRunnableAtFixedRate(BotFiles::save, 5, TimeUnit.SECONDS);
    }

    public static void save() {
        USER.save();
        EXTRA.save();
    }
}

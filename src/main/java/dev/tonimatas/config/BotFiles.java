package dev.tonimatas.config;

import dev.tonimatas.config.config.BotConfig;
import dev.tonimatas.config.data.ExtraData;
import dev.tonimatas.config.data.UserData;

public class BotFiles {
    public static final BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static final UserData USER = JsonFile.loadOrCreate(UserData.class, "data/user.json");
    public static final ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");

    private BotFiles() {
        // We don't need a constructor
    }
    
    public static void save() {
        USER.save();
        EXTRA.save();
    }
}

package dev.tonimatas.config;

public class BotFiles {
    public static final BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static final UserData USER = JsonFile.loadOrCreate(UserData.class, "data/user.json");
    public static final ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");

    private BotFiles() {
        // We don't need a constructor
    }
}

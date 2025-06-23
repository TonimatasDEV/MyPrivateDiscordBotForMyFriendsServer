package dev.tonimatas.config;

public class BotFiles {
    private BotFiles() {
        // We don't need a constructor
    }

    public static final BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static final BankData BANK = JsonFile.loadOrCreate(BankData.class, "data/bank.json");
    public static final ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");
    public static final PointsData POINTS = JsonFile.loadOrCreate(PointsData.class, "data/points.json");
    public static final UserSettingsData SETTINGS = JsonFile.loadOrCreate(UserSettingsData.class, "data/settings.json");
}

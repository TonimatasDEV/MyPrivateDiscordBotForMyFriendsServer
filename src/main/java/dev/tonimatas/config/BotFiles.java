package dev.tonimatas.config;

import dev.tonimatas.systems.bank.UserSettings;

public class BotFiles {
    public static BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static BankData BANK = JsonFile.loadOrCreate(BankData.class, "data/bank.json");
    public static ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");
    public static PaymentsData PAYMENTS = JsonFile.loadOrCreate(PaymentsData.class, "data/payments.json");
    public static PointsData POINTS = JsonFile.loadOrCreate(PointsData.class, "data/points.json");
    public static UserSettingsData SETTINGS = JsonFile.loadOrCreate(UserSettingsData.class, "data/settings.json");
}

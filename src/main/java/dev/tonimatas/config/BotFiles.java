package dev.tonimatas.config;

public class BotFiles {
    public static BotConfig CONFIG = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
    public static BankData BANK = JsonFile.loadOrCreate(BankData.class, "data/bank.json");
    public static ExtraData EXTRA = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");
    public static PaymentsData PAYMENTS = JsonFile.loadOrCreate(PaymentsData.class, "data/payments.json");
}

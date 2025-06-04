package dev.tonimatas.config;

public class BotConfig extends JsonConfig {
    public String token = "default-token";
    public long count = 0;

    @Override
    protected String getFilePath() {
        return "bot.json";
    }
}

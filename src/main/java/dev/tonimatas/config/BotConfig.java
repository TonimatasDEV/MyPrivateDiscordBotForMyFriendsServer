package dev.tonimatas.config;

public class BotConfig extends JsonFile {
    public String token = "default-token";

    @Override
    protected String getFilePath() {
        return "bot.json";
    }
}

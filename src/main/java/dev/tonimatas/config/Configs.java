package dev.tonimatas.config;

import java.util.Map;

public class Configs {
    public static ConfigFile BOT;
    public static ConfigFile XP;

    static {
        BOT = new ConfigFile("bot", Map.of("token", "", "count", "0"));
        XP = new ConfigFile("xp");
    }
}

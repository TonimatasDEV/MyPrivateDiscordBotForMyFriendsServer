package dev.tonimatas.config;

import dev.tonimatas.systems.bank.UserSettings;

import java.util.HashMap;
import java.util.Map;

public class UserSettingsData extends JsonFile {
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, UserSettings> settings = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/settings.json";
    }

    public UserSettings getSettings(String userId) {
        return settings.computeIfAbsent(userId, id -> new UserSettings());
    }

    public void setNotifyDaily(String userId, boolean value) {
        getSettings(userId).setNotifyDaily(value);
        save();
    }
}

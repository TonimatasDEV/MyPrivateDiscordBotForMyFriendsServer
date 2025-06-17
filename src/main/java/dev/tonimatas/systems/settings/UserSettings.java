package dev.tonimatas.systems.settings;

import dev.tonimatas.config.BotFiles;

public class UserSettings {
    private boolean notifyDaily = false;

    public boolean isNotifyDaily() {
        return notifyDaily;
    }

    public void setNotifyDaily(boolean notifyDaily) {
        this.notifyDaily = notifyDaily;
        BotFiles.SETTINGS.save();
    }
}

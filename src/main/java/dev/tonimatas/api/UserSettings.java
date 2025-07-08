package dev.tonimatas.api;

public class UserSettings {
    private boolean notifyDaily = false;

    public boolean isNotifyDaily() {
        return notifyDaily;
    }

    public void setNotifyDaily(boolean notifyDaily) {
        this.notifyDaily = notifyDaily;
        UserInfo.save();
    }
}

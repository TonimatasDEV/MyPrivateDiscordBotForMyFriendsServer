package dev.tonimatas.api.user;

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

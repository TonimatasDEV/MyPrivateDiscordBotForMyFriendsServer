package dev.tonimatas.systems.bank;

import java.time.LocalDateTime;

public class UserSettings {
    private boolean notifyDaily = false;
    private boolean notifiedDaily = false;
    private LocalDateTime lastDailyPing = LocalDateTime.MIN;

    public boolean isNotifyDaily() {
        return notifyDaily;
    }

    public void setNotifyDaily(boolean notifyDaily) {
        this.notifyDaily = notifyDaily;
    }

    public LocalDateTime getLastDailyPing() {
        return lastDailyPing;
    }

    public void setLastDailyPing(LocalDateTime lastDailyPing) {
        this.lastDailyPing = lastDailyPing;
    }

    public boolean isNotifiedDaily() {
        return notifiedDaily;
    }

    public void setNotifiedDaily(boolean notifiedDaily) {
        this.notifiedDaily = notifiedDaily;
    }
}

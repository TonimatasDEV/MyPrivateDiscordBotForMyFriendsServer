package dev.tonimatas.systems.bank;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.TimeUtils;

import java.time.LocalDateTime;

public class DailyInfo {
    private String lastDaily;
    private boolean notified;

    public DailyInfo(String lastDaily, boolean notified) {
        this.lastDaily = lastDaily;
        this.notified = notified;
    }

    public LocalDateTime getLast() {
        return TimeUtils.getLocalDateTime(lastDaily);
    }

    public void setLast(LocalDateTime last) {
        this.lastDaily = TimeUtils.getStr(last);
        BotFiles.BANK.save();
    }

    public boolean isNotified() {
        return notified;
    }

    public void setNotified(boolean notified) {
        this.notified = notified;
        BotFiles.BANK.save();
    }

    public LocalDateTime getNext() {
        return getLast().plusHours(24);
    }

    public String getNextFormatted() {
        return TimeUtils.getStr(getNext());
    }
}

package dev.tonimatas.systems.bank;

import dev.tonimatas.util.TimeUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

public class Transaction implements Comparable<Transaction> {
    private final String userId;
    private final long amount;
    private final String time;
    private final String reason;

    public Transaction(String userId, long amount, String reason) {
        this.userId = userId;
        this.amount = amount;
        this.time = TimeUtils.getNowStr();
        this.reason = reason;
    }

    public String getUserId() {
        return userId;
    }

    public long getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return TimeUtils.getLocalDateTime(time);
    }

    public String getReason() {
        return reason;
    }

    @Override
    public int compareTo(@NotNull Transaction o) {
        if (this.getTime().isAfter(o.getTime())) return -1;
        if (this.getTime().isBefore(o.getTime())) return 1;
        return 0;
    }
}

package dev.tonimatas.api.user;

import dev.tonimatas.api.bank.DailyInfo;
import dev.tonimatas.util.TimeUtils;

import java.time.LocalDateTime;

public class UserInfo {
    private final UserStats stats;
    private DailyInfo daily;
    private UserSettings settings;
    private long money;

    public UserInfo() {
        this(new DailyInfo(TimeUtils.getStr(LocalDateTime.now().minusHours(25)), false), new UserSettings(), 0, new UserStats());
    }

    public UserInfo(DailyInfo daily, UserSettings settings, long money, UserStats stats) {
        this.daily = daily;
        this.settings = settings;
        this.money = money;
        this.stats = stats;
    }

    public DailyInfo getDaily() {
        if (daily == null) {
            daily = new DailyInfo(TimeUtils.getStr(LocalDateTime.now().minusHours(25)), false);
        }

        return daily;
    }

    public long getMoney() {
        return money;
    }

    private void setMoney(long money) {
        this.money = money;
    }

    public void addMoney(long money) {
        stats.increaseMoneyWon(money);

        if (money != 0) {
            setMoney(getMoney() + money);
        }
    }

    public void removeMoney(long money) {
        stats.increaseMoneySpent(money);

        long nonNegativeMoney = Math.min(this.money, money);
        setMoney(getMoney() - nonNegativeMoney);
    }

    public UserSettings getSettings() {
        if (settings == null) {
            settings = new UserSettings();
        }

        return settings;
    }

    public UserStats getStats() {
        return stats;
    }
}

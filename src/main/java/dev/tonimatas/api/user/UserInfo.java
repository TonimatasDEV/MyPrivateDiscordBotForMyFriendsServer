package dev.tonimatas.api.user;

import dev.tonimatas.api.bank.DailyInfo;
import dev.tonimatas.api.bank.Transaction;
import dev.tonimatas.util.TimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserInfo {
    private final String userId;
    private final DailyInfo daily;
    private final ArrayList<Transaction> transactions;
    private final UserSettings settings;
    private long money;
    private long points;
    private UserStats stats;

    public UserInfo(String userId) {
        this(userId, new DailyInfo(TimeUtils.getStr(LocalDateTime.now().minusHours(25)), false), new ArrayList<>(), new UserSettings(), 0, 0, new UserStats());
    }

    public UserInfo(String userId, DailyInfo daily, ArrayList<Transaction> transactions, UserSettings settings, long money, long points, UserStats stats) {
        this.userId = userId;
        this.daily = daily;
        this.transactions = transactions;
        this.settings = settings;
        this.money = money;
        this.points = points;
        this.stats = stats;
    }

    public DailyInfo getDaily() {
        return daily;
    }

    public long getMoney() {
        return money;
    }

    private void setMoney(long money) {
        this.money = money;
    }

    public void addMoney(long money, String reason) {
        stats.increaseMoneyWon(money);

        if (money != 0) {
            addTransaction(new Transaction(userId, money, reason));
            setMoney(getMoney() + money);
        }
    }

    public void removeMoney(long money, String reason) {
        stats.increaseMoneySpent(money);

        long nonNegativeMoney = Math.min(this.money, money);
        addTransaction(new Transaction(userId, -nonNegativeMoney, reason));
        setMoney(getMoney() - nonNegativeMoney);
    }

    public void addTransaction(Transaction transaction) {
        stats.increaseTransactions();
        transactions.sort(null);

        Transaction first = transactions.getFirst();
        if (first.getReason().equals(transaction.getReason())) {
            transactions.removeFirst();
            transaction = new Transaction(userId, transaction.getAmount() + first.getAmount(), transaction.getReason());
        }

        transactions.add(transaction);
        transactions.sort(null);

        int deleteCount = transactions.size() - 10;

        if (deleteCount > 0) {
            for (int i = 0; i < deleteCount; i++) {
                transactions.removeLast();
            }
        }
    }

    public UserSettings getSettings() {
        return settings;
    }

    public List<Transaction> getTransactions() {
        transactions.sort(null);
        return transactions;
    }

    public UserStats getStats() {
        return stats;
    }
}

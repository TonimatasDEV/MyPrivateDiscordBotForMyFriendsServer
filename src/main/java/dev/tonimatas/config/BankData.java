package dev.tonimatas.config;

import dev.tonimatas.systems.bank.DailyInfo;
import dev.tonimatas.systems.bank.Transaction;
import dev.tonimatas.util.TimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankData extends JsonFile {
    public Map<String, Long> bank = new HashMap<>();
    public Map<String, DailyInfo> daily = new HashMap<>();
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, ArrayList<Transaction>> transactions = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/bank.json";
    }

    public DailyInfo getDaily(String userId) {
        daily.computeIfAbsent(userId, id -> new DailyInfo(TimeUtils.getStr(LocalDateTime.now().minusHours(25)), false));
        return daily.get(userId);
    }

    public long getMoney(String userId) {
        if (!bank.containsKey(userId)) {
            setMoney(userId, 0);
        }

        return bank.get(userId);
    }

    private void setMoney(String userId, long money) {
        bank.put(userId, money);
        save();
    }

    public void addMoney(String userId, long money, String reason) {
        if (money != 0) {
            addTransaction(new Transaction(userId, money, reason));
            setMoney(userId, getMoney(userId) + money);
        }
    }

    public void removeMoney(String userId, long money, String reason) {
        addTransaction(new Transaction(userId, -money, reason));
        setMoney(userId, getMoney(userId) - money);
    }

    public void addTransaction(Transaction transaction) {
        String userId = transaction.getUserId();
        ArrayList<Transaction> userTransactions = transactions.get(userId);

        if (userTransactions == null) {
            userTransactions = new ArrayList<>();
        }

        userTransactions.add(transaction);
        userTransactions.sort(null);

        int deleteCount = userTransactions.size() - 10;

        if (deleteCount > 0) {
            for (int i = 0; i < deleteCount; i++) {
                userTransactions.removeLast();
            }
        }

        transactions.put(userId, userTransactions);
        save();
    }

    public List<Transaction> getTransactions(String userId) {
        ArrayList<Transaction> userTransactions = transactions.get(userId);

        if (userTransactions == null) {
            userTransactions = new ArrayList<>();
        }

        userTransactions.sort(null);

        return userTransactions;
    }
}

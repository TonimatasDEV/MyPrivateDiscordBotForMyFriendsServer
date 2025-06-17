package dev.tonimatas.config;

import dev.tonimatas.systems.bank.Transaction;
import dev.tonimatas.util.TimeUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankData extends JsonFile {
    public Map<String, Long> bank = new HashMap<>();
    public Map<String, String> daily = new HashMap<>();
    @SuppressWarnings("FieldMayBeFinal")
    private Map<String, ArrayList<Transaction>> transactions = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/bank.json";
    }

    public LocalDateTime getDaily(String id) {
        if (!daily.containsKey(id)) {
            setDaily(id, LocalDateTime.now().minusHours(25));
        }

        return TimeUtils.getLocalDateTime(daily.get(id));
    }

    public void setDaily(String id, LocalDateTime time) {
        daily.put(id, TimeUtils.getStr(time));
        save();
    }

    public String getNextFormattedDaily(String id) {
        return TimeUtils.getStr(getDaily(id).plusHours(24));
    }

    public long getMoney(String memberID) {
        if (!bank.containsKey(memberID)) {
            setMoney(memberID, 0);
        }

        return bank.get(memberID);
    }

    private void setMoney(String memberID, long money) {
        bank.put(memberID, money);
        save();
    }

    public void addMoney(String memberID, long money, String reason) {
        addTransaction(new Transaction(memberID, money, reason));
        setMoney(memberID, getMoney(memberID) + money);
    }

    public void removeMoney(String memberID, long money, String reason) {
        addTransaction(new Transaction(memberID, -money, reason));
        setMoney(memberID, getMoney(memberID) - money);
    }

    public void addTransaction(Transaction transaction) {
        String userId = transaction.getUserId();
        ArrayList<Transaction> userTransactions = transactions.get(userId);

        if (userTransactions == null) {
            userTransactions = new ArrayList<>();
        }

        if (!userTransactions.isEmpty()) {
            userTransactions.sort(null);

            int deleteCount = transactions.get(userId).size() - 10;

            if (deleteCount > 1) {
                for (int i = 0; i < deleteCount; i++) {
                    userTransactions.removeFirst();
                }
            }
        }

        userTransactions.add(transaction);
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

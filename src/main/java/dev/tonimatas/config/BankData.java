package dev.tonimatas.config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BankData extends JsonFile {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    public Map<String, Long> bank = new HashMap<>();
    public Map<String, String> daily = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/bank.json";
    }

    public LocalDateTime getDaily(String id) {
        if (!daily.containsKey(id)) {
            setDaily(id, LocalDateTime.now().minusHours(25));
        }

        return FORMATTER.parse(daily.get(id), LocalDateTime::from);
    }

    public void setDaily(String id, LocalDateTime time) {
        daily.put(id, FORMATTER.format(time));
        save();
    }

    public String getNextFormattedDaily(String id) {
        return FORMATTER.format(getDaily(id).plusHours(24));
    }

    public long getMoney(String memberID) {
        if (!bank.containsKey(memberID)) {
            setMoney(memberID, 0);
        }

        return bank.get(memberID);
    }

    public void setMoney(String memberID, long money) {
        bank.put(memberID, money);
        save();
    }

    public void addMoney(String memberID, long money) {
        setMoney(memberID, getMoney(memberID) + money);
    }

    public void removeMoney(String memberID, long money) {
        setMoney(memberID, getMoney(memberID) - money);
    }
}

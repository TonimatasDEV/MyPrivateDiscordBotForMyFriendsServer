package dev.tonimatas.config;

import java.util.HashMap;
import java.util.Map;

public class BankData extends JsonFile {
    public Map<String, Long> bank = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/bank.json";
    }

    public long getMoney(String memberID) {
        if (!bank.containsKey(memberID)) {
            setMoney(memberID, 0);
        }

        return bank.get(memberID);
    }

    public void setMoney(String memberID, long money) {
        bank.put(memberID, money);
    }
}

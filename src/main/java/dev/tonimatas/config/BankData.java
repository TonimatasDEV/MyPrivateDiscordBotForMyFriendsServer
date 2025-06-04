package dev.tonimatas.config;

import java.util.HashMap;
import java.util.Map;

public class BankData extends JsonFile {
    public Map<String, Long> bank = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/bank.json";
    }
}

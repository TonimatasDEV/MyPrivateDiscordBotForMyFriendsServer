package dev.tonimatas.roulette;

import dev.tonimatas.schedules.RouletteManager;

public record Bet(String id, long money, BetType type, int value) {
    public boolean isValid() {
        return type.isCorrect(value);
    }
    
    public void giveReward(int winNumber) {
        int multiplier = type.getMultiplier(value, winNumber);

        // TODO: Send message to ID
        RouletteManager.bankAccounts.put(id, money*multiplier);
    }
}

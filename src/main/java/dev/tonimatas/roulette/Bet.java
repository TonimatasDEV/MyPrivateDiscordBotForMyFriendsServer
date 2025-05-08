package dev.tonimatas.roulette;

public record Bet(String id, long money, BetType type, int value) {
    public boolean isValid() {
        return type.isCorrect(value);
    }

    public long getProfit(int winner) {
        return type.getMultiplier(value, winner) * money;
    }
}

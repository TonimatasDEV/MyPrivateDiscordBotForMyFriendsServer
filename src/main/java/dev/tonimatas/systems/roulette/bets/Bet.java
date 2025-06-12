package dev.tonimatas.systems.roulette.bets;

public abstract class Bet {
    private final String id;
    private final long money;

    public Bet(String id, long money) {
        this.id = id;
        this.money = money;
    }

    public String getRewardMessage(int winnerNumber) {
        if (isWinner(winnerNumber)) {
            return "bet on " + getTypePart() + " and won " + getReward(winnerNumber) + "€.";
        } else {
            return "bet on " + getTypePart() + " and lost " + getMoney() + "€.";
        }
    }

    public String getBetMessage() {
        return "bet " + money + "€ on " + getTypePart() + ".";
    }

    public abstract String getTypePart();

    abstract int getMultiplier();

    abstract boolean isWinner(int winnerNumber);

    public abstract boolean isValid();

    public String getId() {
        return id;
    }

    public long getMoney() {
        return money;
    }

    public long getReward(int winnerNumber) {
        if (!isWinner(winnerNumber)) return 0;

        return getMultiplier() * money;
    }
}

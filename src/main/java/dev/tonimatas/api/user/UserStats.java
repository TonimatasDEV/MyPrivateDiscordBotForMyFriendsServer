package dev.tonimatas.api.user;

public class UserStats {
    private long countCorrectly;
    private long countIncorrectly;
    private long moneyWon;
    private long moneySpent;
    private long transactions;

    public UserStats() {
        this(0, 0);
    }

    public UserStats(long countCorrectly, long countIncorrectly) {
        this.countCorrectly = countCorrectly;
        this.countIncorrectly = countIncorrectly;
    }

    public long getCountCorrectly() {
        return countCorrectly;
    }

    public long getCountIncorrectly() {
        return countIncorrectly;
    }

    public long getMoneyWon() {
        return moneyWon;
    }

    public long getMoneySpent() {
        return moneySpent;
    }

    public long getTransactions() {
        return transactions;
    }

    public void increaseCountCorrectly() {
        countCorrectly++;
    }

    public void increaseCountIncorrectly() {
        countIncorrectly++;
    }
    
    public void increaseMoneyWon(long amount) {
        moneyWon += amount;
    }
    
    public void increaseMoneySpent(long amount) {
        moneySpent += amount;
    }
    
    public void increaseTransactions() {
        transactions++;
    }
}

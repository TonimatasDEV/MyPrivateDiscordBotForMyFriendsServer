package dev.tonimatas.api.user;

public class UserStats {
    private long countCorrectly;
    private long countIncorrectly;
    private long moneyWon;
    private long moneySpent;
    private long messagesSent;
    private long commandsExecuted;

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

    public long getMessagesSent() {
        return messagesSent;
    }

    public long getCommandsExecuted() {
        return commandsExecuted;
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

    public void increaseMessagesSent() {
        messagesSent++;
    }

    public void increaseCommandsExecuted() {
        commandsExecuted++;
    }
}

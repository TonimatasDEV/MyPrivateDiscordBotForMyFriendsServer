package dev.tonimatas.api.user;

import java.time.Duration;
import java.time.LocalDateTime;

public class UserStats {
    private long countCorrectly;
    private long countIncorrectly;
    private long moneyWon;
    private long moneySpent;
    private long transactions;
    private long messagesSent;
    private long commandsExecuted;
    private String timeInVoice = Duration.ZERO.toString();

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

    public long getMessagesSent() {
        return messagesSent;
    }

    public long getCommandsExecuted() {
        return commandsExecuted;
    }

    public Duration getTimeInVoice() {
        return Duration.parse(timeInVoice);
    }
    
    public long getTimeInVoiceLong() {
        return getTimeInVoice().getSeconds();
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

    public void increaseMessagesSent() {
        messagesSent++;
    }

    public void increaseCommandsExecuted() {
        commandsExecuted++;
    }

    public void increaseTimeInVoice(LocalDateTime time) {
        if (time == null) {
            return;
        }

        Duration actualTime = Duration.parse(timeInVoice);
        timeInVoice = actualTime.plus(Duration.between(time, LocalDateTime.now())).toString();
    }
}

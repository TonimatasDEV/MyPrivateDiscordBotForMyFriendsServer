package dev.tonimatas.api.user;

public class UserStats {
    private long countCorrectly;
    private long countIncorrectly;

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

    public void increaseCountCorrectly() {
        countCorrectly++;
        UserInfo.save();
    }

    public void increaseCountIncorrectly() {
        countIncorrectly++;
        UserInfo.save();
    }
}

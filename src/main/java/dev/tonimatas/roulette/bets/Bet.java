package dev.tonimatas.roulette.bets;

public abstract class Bet {
    private final String id;
    protected final int input;
    private final long money;
    
    public Bet(String id, int input, int money) {
        this.id = id;
        this.input = input;
        this.money = money;
    }
    
    abstract int getMultiplier();
    
    abstract boolean isWinner(int winnerNumber);
    
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

package dev.tonimatas.roulette.bets;

public class NumberBet extends Bet {
    public NumberBet(String id, int input, int money) {
        super(id, input, money);
    }

    @Override
    int getMultiplier() {
        return 36;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return winnerNumber == input;
    }
}

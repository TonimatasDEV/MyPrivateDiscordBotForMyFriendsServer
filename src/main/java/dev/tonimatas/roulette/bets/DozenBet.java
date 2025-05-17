package dev.tonimatas.roulette.bets;

public class DozenBet extends Bet {
    public DozenBet(String id, int input, int money) {
        super(id, input, money);
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getDozen(winnerNumber) == input;
    }

    private static int getDozen(int number) {
        if (number > 0 && number < 13) {
            return 1;
        } else if (number >= 13 && number <=24) {
            return 2;
        } else if (number >= 25 && number <= 36) {
            return 3;
        } else {
            return -1;
        }
    }
}

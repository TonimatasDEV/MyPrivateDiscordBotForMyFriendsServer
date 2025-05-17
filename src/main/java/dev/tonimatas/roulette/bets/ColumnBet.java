package dev.tonimatas.roulette.bets;

public class ColumnBet extends Bet {
    public ColumnBet(String id, int input, int money) {
        super(id, input, money);
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getColumn(winnerNumber) == input;
    }

    private static int getColumn(int number) {
        if (number == 0) {
            return -1;
        }

        if (number % 3 == 0) {
            return 3;
        } else if ( (number + 1) % 3 == 0) {
            return 2;
        } else {
            return 1;
        }
    }
}

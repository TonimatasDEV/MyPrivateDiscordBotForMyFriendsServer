package dev.tonimatas.roulette.bets;

public class ColorBet extends Bet {
    public ColorBet(String id, int input, int money) {
        super(id, input, money);
    }

    @Override
    int getMultiplier() {
        return 0;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getColor(winnerNumber) == input;
    }

    private static int getColor(int number) {
        return switch (number) {
            case 0 -> 1;
            case 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> 2;
            case 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 -> 3;
            default -> -1;
        };
    }
}

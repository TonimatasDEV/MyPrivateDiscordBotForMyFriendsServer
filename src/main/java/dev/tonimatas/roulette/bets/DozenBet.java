package dev.tonimatas.roulette.bets;

public class DozenBet extends Bet {
    private final String input;

    public DozenBet(String id, String dozen, long money) {
        super(id, money);
        this.input = dozen;
    }

    private static String getDozen(int number) {
        if (number > 0 && number < 13) {
            return "first";
        } else if (number >= 13 && number <= 24) {
            return "second";
        } else if (number >= 25 && number <= 36) {
            return "third";
        } else {
            return "invalid";
        }
    }

    @Override
    public String getTypePart() {
        return "the " + input + " dozen";
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getDozen(winnerNumber).equalsIgnoreCase(input);
    }

    @Override
    public boolean isValid() {
        return input.equalsIgnoreCase("first") || input.equalsIgnoreCase("second") || input.equalsIgnoreCase("third");
    }
}

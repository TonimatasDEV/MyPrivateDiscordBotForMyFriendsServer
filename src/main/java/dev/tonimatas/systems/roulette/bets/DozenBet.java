package dev.tonimatas.systems.roulette.bets;

public class DozenBet extends Bet {
    private final String dozen;

    public DozenBet(String id, String dozen, long money) {
        super(id, money);
        this.dozen = dozen;
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
        return "the " + dozen + " dozen";
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int winner) {
        return getDozen(winner).equalsIgnoreCase(dozen);
    }

    @Override
    public boolean isValid() {
        return dozen.equalsIgnoreCase("first") || dozen.equalsIgnoreCase("second") || dozen.equalsIgnoreCase("third");
    }
}

package dev.tonimatas.roulette.bets;

public class ColumnBet extends Bet {
    private final String input;
    
    public ColumnBet(String id, String column, long money) {
        super(id, money);
        this.input = column;
    }

    @Override
    public String getTypePart() {
        return "the " + input + " column";
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getColumn(winnerNumber).equalsIgnoreCase(input);
    }

    @Override
    public boolean isValid() {
        return input.equalsIgnoreCase("first") || input.equalsIgnoreCase("second") || input.equalsIgnoreCase("third");
    }

    private static String getColumn(int number) {
        if (number == 0) {
            return "invalid";
        }

        if (number % 3 == 0) {
            return "third";
        } else if ( (number + 1) % 3 == 0) {
            return "second";
        } else {
            return "first";
        }
    }
}

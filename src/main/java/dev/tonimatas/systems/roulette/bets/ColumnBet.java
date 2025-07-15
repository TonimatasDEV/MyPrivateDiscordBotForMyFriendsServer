package dev.tonimatas.systems.roulette.bets;

public class ColumnBet extends Bet {
    private final String column;

    public ColumnBet(String id, String column, long money) {
        super(id, money);
        this.column = column;
    }

    private static String getColumn(int number) {
        if (number == 0) {
            return "invalid";
        }

        if (number % 3 == 0) {
            return "third";
        } else if ((number + 1) % 3 == 0) {
            return "second";
        } else {
            return "first";
        }
    }

    @Override
    public String getTypePart() {
        return "the " + column + " column";
    }

    @Override
    int getMultiplier() {
        return 3;
    }

    @Override
    boolean isWinner(int number) {
        return getColumn(number).equalsIgnoreCase(column);
    }

    @Override
    public boolean isValid() {
        return column.equalsIgnoreCase("first") || column.equalsIgnoreCase("second") || column.equalsIgnoreCase("third");
    }

    @Override
    public boolean canMerge(Bet bet) {
        return bet instanceof ColumnBet columnBet && columnBet.column.equals(column);
    }
}

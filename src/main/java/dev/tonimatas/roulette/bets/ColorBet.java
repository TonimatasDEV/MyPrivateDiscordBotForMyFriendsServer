package dev.tonimatas.roulette.bets;

public class ColorBet extends Bet {
    private final String input;
    
    public ColorBet(String id, String color, long money) {
        super(id, money);
        this.input = color;
    }

    @Override
    public String getTypePart() {
        return input;
    }

    @Override
    int getMultiplier() {
        return switch (input) {
            case "green" -> 36;
            case "red", "black" -> 2;
            default -> 0;
        };
    }

    @Override
    boolean isWinner(int winnerNumber) {
        return getColor(winnerNumber).equalsIgnoreCase(input);
    }

    @Override
    public boolean isValid() {
        return input.equalsIgnoreCase("red") || input.equalsIgnoreCase("green") || input.equalsIgnoreCase("black");
    }

    private static String getColor(int number) {
        return switch (number) {
            case 0 -> "green";
            case 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> "red";
            case 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 -> "black";
            default -> "invalid";
        };
    }
}

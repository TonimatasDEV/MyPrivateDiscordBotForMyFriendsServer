package dev.tonimatas.systems.roulette.bets;

public class ColorBet extends Bet {
    private final String input;

    public ColorBet(String id, String color, long money) {
        super(id, money);
        this.input = color;
    }

    private static String getColor(int number) {
        if (number >= 0 && number <= 36) {
            if (number == 0) return "green";

            if (number <= 10 || number >= 19 && number <= 28) {
                return number % 2 == 0 ? "black" : "red";
            }

            return number % 2 == 0 ? "red" : "black";
        }

        return "invalid";
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
}

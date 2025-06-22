package dev.tonimatas.systems.roulette.bets;

public class ColorBet extends Bet {
    private final String color;

    public ColorBet(String id, String color, long money) {
        super(id, money);
        this.color = color;
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
        return color;
    }

    @Override
    int getMultiplier() {
        return switch (color) {
            case "green" -> 36;
            case "red", "black" -> 2;
            default -> 0;
        };
    }

    @Override
    boolean isWinner(int number) {
        return getColor(number).equalsIgnoreCase(color);
    }

    @Override
    public boolean isValid() {
        return color.equalsIgnoreCase("red") || color.equalsIgnoreCase("green") || color.equalsIgnoreCase("black");
    }
}

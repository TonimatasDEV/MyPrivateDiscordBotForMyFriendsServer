package dev.tonimatas.systems.roulette.bets;

import java.util.Arrays;
import java.util.Locale;

public class ColorBet extends Bet {
    private final Color color;

    public ColorBet(String id, String color, long money) {
        super(id, money);
        this.color = getColor(color);
    }

    private static Color getColor(String color) {
        return switch (color) {
            case "green" -> Color.GREEN;
            case "red" -> Color.RED;
            case "black" -> Color.BLACK;
            default -> null;
        };
    }
    
    private static Color getColor(int number) {
        if (number >= 0 && number <= 36) {
            if (number == 0) return Color.GREEN;

            if (number <= 10 || number >= 19 && number <= 28) {
                return number % 2 == 0 ? Color.BLACK : Color.RED;
            }

            return number % 2 == 0 ? Color.RED : Color.BLACK;
        }

        return null;
    }

    @Override
    public String getTypePart() {
        return color.toString().toLowerCase(Locale.ENGLISH);
    }

    @Override
    int getMultiplier() {
        return switch (color) {
            case GREEN -> 36;
            case RED, BLACK -> 2;
        };
    }

    @Override
    boolean isWinner(int number) {
        return getColor(number) == color;
    }

    @Override
    public boolean isValid() {
        return color != null && Arrays.asList(Color.values()).contains(color);
    }
    
    private enum Color {
        GREEN,
        RED,
        BLACK
    }
}

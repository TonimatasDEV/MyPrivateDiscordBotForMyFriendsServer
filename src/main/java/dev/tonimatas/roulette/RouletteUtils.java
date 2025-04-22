package dev.tonimatas.roulette;

public class RouletteUtils {
    public static int getDozen(int number) {
        if (number > 0 && number < 13) {
            return 1;
        } else if (number >= 13 && number <=24) {
            return 2;
        } else if (number >= 25 && number <= 36) {
            return 3;
        } else {
            return -1;
        }
    }

    public static int getColumn(int number) {
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

    public static int getColor(int number) {
        return switch (number) {
            case 0 -> 1;
            case 1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36 -> 2;
            case 2, 4, 6, 8, 10, 11, 13, 15, 17, 20, 22, 24, 26, 28, 29, 31, 33, 35 -> 3;
            default -> -1;
        };
    }
}

package dev.tonimatas.util;

import java.time.Duration;

public class TimeUtils {
    public static String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        if (minutes > 0 && seconds > 0) {
            return "Remaining " + minutes + " minute" + (minutes != 1 ? "s" : "") +
                    " and " + seconds + " second" + (seconds != 1 ? "s" : "");
        } else if (minutes > 0) {
            return "Remaining " + minutes + " minute" + (minutes != 1 ? "s" : "");
        } else if (seconds > 0) {
            return "Remaining " + seconds + " second" + (seconds != 1 ? "s" : "");
        } else {
            return "Time's up!";
        }
    }
}

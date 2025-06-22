package dev.tonimatas.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class TimeUtils {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    public static String getNowStr() {
        return getStr(LocalDateTime.now());
    }

    public static String getStr(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }

    public static LocalDateTime getLocalDateTime(String timeStr) {
        return LocalDateTime.parse(timeStr, FORMATTER);
    }

    public static String formatDuration(Duration duration) {
        long minutes = duration.toMinutes();
        long seconds = duration.minusMinutes(minutes).getSeconds();

        if (minutes > 0 && seconds > 0) {
            return "Remaining " + minutes + " minute" + (minutes != 1 ? "s" : "") + " and " + seconds + " second" + (seconds != 1 ? "s" : "");
        } else if (minutes > 0) {
            return "Remaining " + minutes + " minute" + (minutes != 1 ? "s" : "");
        } else if (seconds > 0) {
            return "Remaining " + seconds + " second" + (seconds != 1 ? "s" : "");
        } else {
            return "Time's up!";
        }
    }

    public static boolean isBetween(LocalTime time, int startHour, int startMinute, int endHour, int endMinute) {
        LocalTime start = LocalTime.of(startHour, startMinute);
        LocalTime end = LocalTime.of(endHour, endMinute);

        if (start.isBefore(end)) {
            return !time.isBefore(start) && !time.isAfter(end);
        } else {
            return !time.isBefore(start) || time.isBefore(end);
        }
    }
}

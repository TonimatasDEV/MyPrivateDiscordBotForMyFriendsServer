package dev.tonimatas.commands;

import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.entities.User;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;

public class HiCommand {
    @Command("hi")
    @Description("Receive a greeting from our friendly bot.")
    public void execute(SlashCommandActor actor) {
        User user = actor.user();
        LocalTime nowTime = LocalTime.now();
        LocalDate nowDate = LocalDate.now();

        String greeting;
        String userName = user.getEffectiveName();
        int startHourNight = isSummer(LocalDateTime.of(nowDate, nowTime)) ? 22 : 18;

        if (TimeUtils.isBetween(nowTime, 6, 0, 12, 30)) {
            greeting = "☀️ Good morning, " + userName + "! 😊";
        } else if (TimeUtils.isBetween(nowTime, 12, 31, startHourNight, 0)) {
            greeting = "🌤️ Good afternoon, " + userName + "! 😄";
        } else if (TimeUtils.isBetween(nowTime, startHourNight, 1, 23, 59) || TimeUtils.isBetween(nowTime, 0, 0, 2, 0)) {
            greeting = "🌙 Good night, " + userName + "! 😴";
        } else {
            greeting = "😠 Go to sleep, bot! Stop greeting at this hours...";
        }

        actor.replyToInteraction(greeting).queue();
    }

    private boolean isSummer(LocalDateTime date) {
        LocalDateTime summerStart = LocalDateTime.of(date.getYear(), 5, 15, 0, 0);
        LocalDateTime summerEnd = LocalDateTime.of(date.getYear(), 10, 15, 23, 59);
        return date.isAfter(ChronoLocalDateTime.from(summerStart)) && date.isBefore(ChronoLocalDateTime.from(summerEnd));
    }
}

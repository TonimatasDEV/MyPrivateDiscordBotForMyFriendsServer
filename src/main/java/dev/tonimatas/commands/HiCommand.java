package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.List;

public class HiCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        User user = interaction.getUser();
        LocalTime nowTime = LocalTime.now();
        LocalDate nowDate = LocalDate.now();

        String greeting;
        String userName = user.getEffectiveName();
        int startHourNight = isSummer(LocalDateTime.of(nowDate, nowTime)) ? 22 : 18;

        if (TimeUtils.isBetween(nowTime, 6, 0, 12, 30)) {
            greeting = "☀️ ¡Buenos días, " + userName + "! 😊";
        } else if (TimeUtils.isBetween(nowTime, 12, 31, startHourNight, 0)) {
            greeting = "🌤️ ¡Buenas tardes, " + userName + "! 😄";
        } else if (TimeUtils.isBetween(nowTime, startHourNight, 1, 23, 59) || TimeUtils.isBetween(nowTime, 0, 0, 2, 0)) {
            greeting = "🌙 ¡Buenas noches, " + userName + "! 😴";
        } else {
            greeting = "😠 ¡Duérmete, bot! Deja de saludar a estas horas...";
        }

        interaction.reply(greeting).queue();
    }

    @Override
    public String getCommandName() {
        return "hi";
    }

    @Override
    public String getDescription() {
        return "Receive a greeting from our friendly bot.";
    }

    @Override
    public List<InteractionContextType> getContexts() {
        return InteractionContextType.ALL.stream().toList();
    }

    private boolean isSummer(LocalDateTime date) {
        LocalDateTime summerStart = LocalDateTime.of(date.getYear(), 5, 15, 0, 0);
        LocalDateTime summerEnd = LocalDateTime.of(date.getYear(), 10, 15, 23, 59);
        return date.isAfter(ChronoLocalDateTime.from(summerStart)) && date.isBefore(ChronoLocalDateTime.from(summerEnd));
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.api.bank.DailyInfo;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

import java.time.LocalDateTime;

public class DailyCommand {
    @Command("daily")
    @Description("Daily money!")
    @GuildOnly
    public void execute(SlashCommandActor actor) {
        if (CommandUtils.isNotCommandsChannel(actor)) return;

        JDA jda = actor.jda();
        User user = actor.user();
        LocalDateTime now = LocalDateTime.now();
        DailyInfo dailyInfo = BotFiles.USER.get(user.getId()).getDaily();

        if (now.isAfter(dailyInfo.getNext())) {
            BotFiles.USER.get(user.getId()).addMoney(100);
            MessageCreateData embed = Messages.getDefaultEmbed_Lamp(jda, "Daily", "Yeah! You claimed 100€.");
            actor.replyToInteraction(embed).queue();
            dailyInfo.setLast(now);
            dailyInfo.setNotified(false);
        } else {
            String formattedDate = BotFiles.USER.get(user.getId()).getDaily().getNextFormatted();
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "You need to wait more. Your next daily will be available at " + formattedDate);
            actor.replyToInteraction(embed).queue();
        }
    }
}

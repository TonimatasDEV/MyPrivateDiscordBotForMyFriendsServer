package dev.tonimatas.commands;

import dev.tonimatas.api.bank.DailyInfo;
import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.time.LocalDateTime;
import java.util.Set;

public class DailyCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        JDA jda = interaction.getJDA();
        User user = interaction.getUser();
        LocalDateTime now = LocalDateTime.now();
        DailyInfo dailyInfo = BotFiles.USER.get(user.getId()).getDaily();

        if (now.isAfter(dailyInfo.getNext())) {
            BotFiles.USER.get(user.getId()).addMoney(100, "Daily money!");
            MessageEmbed embed = Messages.getDefaultEmbed(jda, "Daily", "Yeah! You claimed 100€.");
            interaction.replyEmbeds(embed).queue();
            dailyInfo.setLast(now);
            dailyInfo.setNotified(false);
        } else {
            String formattedDate = BotFiles.USER.get(user.getId()).getDaily().getNextFormatted();
            MessageEmbed embed = Messages.getErrorEmbed(jda, "You need to wait more. Your next daily will be available at " + formattedDate);
            interaction.replyEmbeds(embed).queue();
        }
    }

    @Override
    public String getName() {
        return "daily";
    }

    @Override
    public String getDescription() {
        return "Daily money!";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

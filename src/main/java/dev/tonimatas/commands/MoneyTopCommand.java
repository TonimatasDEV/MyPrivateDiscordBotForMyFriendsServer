package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class MoneyTopCommand implements SlashCommand {
    private static String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, UserInfo>> sortedList = BotFiles.USER.getUsers().entrySet()
                .stream()
                .sorted((a, b) -> Long.compare(b.getValue().getMoney(), a.getValue().getMoney()))
                .toList();

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            String name = "None";
            long money = 0;

            if (sortedList.size() > i) {
                Member user = guild.getMemberById(sortedList.get(i).getKey());

                name = (user != null) ? user.getEffectiveName() : "Unknown";
                money = sortedList.get(i).getValue().getMoney();
            }

            text.append(i).append(". ").append(name).append(" - ").append(money).append("€\n");
        }

        return text.toString();
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        Guild guild = interaction.getGuild();

        if (guild == null) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "You need to be in a guild!");
            interaction.replyEmbeds(embed).setEphemeral(true).queue();
            return;
        }

        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Money Top", getMoneyTopString(guild));
        interaction.replyEmbeds(embed).queue();
    }

    @Override
    public String getName() {
        return "money-top";
    }

    @Override
    public String getDescription() {
        return "The top 10 richest people among us!";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Set;

public class StatsCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;


        User user = interaction.getUser();
        OptionMapping option = interaction.getOption("user");

        if (option != null) {
            user = option.getAsUser();
        }

        if (user.isBot()) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Bots don't take part on any statistics.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        UserInfo userInfo = BotFiles.USER.get(user.getId());
        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Stats",
                "**" + user.getEffectiveName() + ":**" + "\n" +
                        "Times counted correctly: " + userInfo.getStats().getCountCorrectly() + ".\n" +
                        "Times counted incorrectly: " + userInfo.getStats().getCountIncorrectly() + ".\n" +
                        "Money won: " + userInfo.getStats().getMoneyWon() + "€.\n" +
                        "Money spent: " + userInfo.getStats().getMoneySpent() + "€.\n" +
                        "Total transactions: " + userInfo.getStats().getTransactions() + ".\n" +
                        "Messages sent: " + userInfo.getStats().getMessagesSent() + ".\n" +
                        "Commands Executed: " + userInfo.getStats().getCommandsExecuted() + "."
        );

        interaction.replyEmbeds(embed).queue();
    }

    @Override
    public SlashCommandData init(SlashCommandData slashCommandData) {
        return slashCommandData.addOption(OptionType.USER, "user", "The user that you want to check their stats.");
    }

    @Override
    public String getName() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "Show user statistics.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

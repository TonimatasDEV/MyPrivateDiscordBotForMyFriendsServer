package dev.tonimatas.commands;

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

public class MoneyCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        User user = interaction.getUser();
        OptionMapping option = interaction.getOption("user");

        if (option != null) {
            user = option.getAsUser();
        }

        if (user.isBot()) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Bots cannot storage money.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        long money = BotFiles.USER.get(user.getId()).getMoney();
        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Money", user.getEffectiveName() + " has " + money + "€.");
        interaction.replyEmbeds(embed).queue();
    }

    @Override
    public SlashCommandData init(SlashCommandData slashCommandData) {
        return slashCommandData.addOption(OptionType.USER, "user", "The user that you want to check their amount of money.");
    }

    @Override
    public String getName() {
        return "money";
    }

    @Override
    public String getDescription() {
        return "The user that you want to check their amount of money.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

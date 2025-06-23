package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.systems.bank.Bank;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Set;

public class MoneyTopCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        Guild guild = interaction.getGuild();

        if (guild == null) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "You need to be in a guild!");
            interaction.replyEmbeds(embed).setEphemeral(true).queue();
            return;
        }

        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Money Top", Bank.getMoneyTopString(guild));
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

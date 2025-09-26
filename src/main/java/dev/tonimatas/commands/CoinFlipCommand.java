package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Set;

public class CoinFlipCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        User user = interaction.getUser();
        OptionMapping typeOption = interaction.getOption("type");
        OptionMapping moneyOption = interaction.getOption("money");
        
        if (typeOption == null || moneyOption == null) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Invalid options. Please recheck your command options.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String type = typeOption.getAsString();
        long money = moneyOption.getAsLong();
        boolean typeBoolean = type.equalsIgnoreCase("heads");

        if (money > BotFiles.USER.get(user.getId()).getMoney()) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Insufficient funds.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (typeBoolean || type.equalsIgnoreCase("tails")) {
            MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Coinflip", "User: " + user.getEffectiveName() + "." +
                    "\nOption: " + type + "." +
                    "\nMoney: " + money + "€.");
            String buttonId = "coinflip-" + user.getId() + "-" + typeBoolean + "-" + money;
            interaction.replyEmbeds(embed).addComponents(
                    ActionRow.of(
                            Button.of(ButtonStyle.PRIMARY, buttonId, "Flip", Emoji.fromUnicode("🪙"))
                    )
            ).queue();
        } else {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Invalid options. Please recheck your command options.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOptions(
                new OptionData(OptionType.STRING, "type", "Select what you want to play. Heads or tails.", true)
                        .addChoice("heads", "heads")
                        .addChoice("tails", "tails"),
                new OptionData(OptionType.STRING, "money", "Money amount", true)
        );
    }

    @Override
    public String getName() {
        return "coinflip";
    }

    @Override
    public String getDescription() {
        return "Flip the coin and steal money from the others with your luck.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

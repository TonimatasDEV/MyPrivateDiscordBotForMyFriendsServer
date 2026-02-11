package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.roulette.Roulette;
import dev.tonimatas.systems.roulette.bets.Bet;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Set;

public class BetCommand {
    /*
    @Override
    public void execute(SlashCommandInteraction interaction) {
        JDA jda = interaction.getJDA();
        String id = interaction.getUser().getId();

        if (!interaction.getChannel().getId().equals(BotFiles.CONFIG.getRouletteChannel(jda).getId())) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "This command can only be run in the Roulette channel.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String type = interaction.getSubcommandName();
        OptionMapping betOption = interaction.getOption("option");
        OptionMapping betMoney = interaction.getOption("money");

        if (type == null || betOption == null || betMoney == null) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "Invalid bet type, option or money.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String option = betOption.getAsString();
        long money = betMoney.getAsLong();

        Bet bet = Roulette.getBet(type, id, option, money);

        if (bet == null) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "This bet type \"" + type + "\" doesn't exist.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (bet.isValid()) {
            if (bet.getMoney() <= BotFiles.USER.get(id).getMoney()) {
                Roulette.getRoulette(jda).addBet(bet);
                MessageEmbed embed = Messages.getDefaultEmbed(jda, "Bet", "Your " + type + " bet has been added to the Roulette.");
                interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            } else {
                MessageEmbed embed = Messages.getErrorEmbed(jda, "You don't have enough money.");
                interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }
        } else {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "Invalid bet option \"" + option + "\" for \"" + type + "\".");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }

    @Override
    public String getName() {
        return "bet";
    }

    @Override
    public String getDescription() {
        return "Make a bet on the roulette!";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addSubcommands(new SubcommandData("color", "Bet to color")
                        .addOptions(createOption()
                                        .addChoice("green", "green")
                                        .addChoice("red", "red")
                                        .addChoice("black", "black"),
                                createMoneyOption()))
                .addSubcommands(new SubcommandData("column", "Bet to column")
                        .addOptions(createOption()
                                        .addChoice("first", "first")
                                        .addChoice("second", "second")
                                        .addChoice("third", "third"),
                                createMoneyOption()))
                .addSubcommands(new SubcommandData("dozen", "Bet to dozen")
                        .addOptions(createOption()
                                        .addChoice("first", "first")
                                        .addChoice("second", "second")
                                        .addChoice("third", "third"),
                                createMoneyOption()))
                .addSubcommands(new SubcommandData("number", "Bet to number")
                        .addOptions(createOption(),
                                createMoneyOption()));
    }
    
    private static OptionData createOption() {
        return new OptionData(OptionType.STRING, "option", "Select the option.", true);
    }
    
    private static OptionData createMoneyOption() {
        return new OptionData(OptionType.STRING, "money", "Money for the bet", true);
    }*/
}

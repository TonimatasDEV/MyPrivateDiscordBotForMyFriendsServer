package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.roulette.Roulette;
import dev.tonimatas.systems.roulette.bets.Bet;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.*;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.Choices;
import revxrsal.commands.jda.annotation.GuildOnly;

public class BetCommand {
    @Command("bet")
    @Description("Bet to color!")
    @GuildOnly
    @Subcommand("color")
    public void executeBetColor(SlashCommandActor actor, @Named("option") @Description("Select the option.") @Choices({"green", "red", "black"}) String option, @Named("money") @Description("Money for the bet") @Range(min = 1) long money) {
        execute(actor, option, money);
    }

    @Command("bet")
    @Description("Bet to column!")
    @GuildOnly
    @Subcommand("column")
    public void executeBetColumn(SlashCommandActor actor, @Named("option") @Description("Select the option.") @Choices({"first", "second", "third"}) String option, @Named("money") @Description("Money for the bet") @Range(min = 1) long money) {
        execute(actor, option, money);
    }

    @Command("bet")
    @Description("Bet to dozen!")
    @GuildOnly
    @Subcommand("dozen")
    public void executeBetDozen(SlashCommandActor actor, @Named("option") @Description("Select the option.") @Choices({"first", "second", "third"}) String option, @Named("money") @Description("Money for the bet") @Range(min = 1) long money) {
        execute(actor, option, money);
    }

    @Command("bet")
    @Description("Bet to number!")
    @GuildOnly
    @Subcommand("number")
    public void executeBetNumber(SlashCommandActor actor, @Named("option") @Description("Select the option.") @Range(min = 0, max = 36) Integer option, @Named("money") @Description("Money for the bet") @Range(min = 1) long money) {
        execute(actor, String.valueOf(option), money);
    }

    public void execute(SlashCommandActor actor, String option, long money) {
        JDA jda = actor.jda();
        String id = actor.user().getId();

        if (!actor.channel().getId().equals(BotFiles.CONFIG.getRouletteChannel(jda).getId())) {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "This command can only be run in the Roulette channel.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String type = actor.commandEvent().getSubcommandName();

        if (type == null) {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "Invalid bet type, option or money.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        Bet bet = Roulette.getBet(type, id, option, money);

        if (bet == null) {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "This bet type \"" + type + "\" doesn't exist.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (bet.isValid()) {
            if (bet.getMoney() <= BotFiles.USER.get(id).getMoney()) {
                Roulette.getRoulette(jda).addBet(bet);
                MessageCreateData embed = Messages.getDefaultEmbed_Lamp(jda, "Bet", "Your " + type + " bet has been added to the Roulette.");
                actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            } else {
                MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "You don't have enough money.");
                actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }
        } else {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "Invalid bet option \"" + option + "\" for \"" + type + "\".");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }
}

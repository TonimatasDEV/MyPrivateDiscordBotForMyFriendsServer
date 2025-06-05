package dev.tonimatas.listeners;

import dev.tonimatas.config.BankData;
import dev.tonimatas.roulette.Roulette;
import dev.tonimatas.roulette.bets.*;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.Strings;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Collections;

public class RouletteListener extends ListenerAdapter {
    private final Roulette roulette;
    private final BankData bankData;

    public RouletteListener(JDA jda, BankData bankData) {
        this.roulette = new Roulette(jda, bankData);
        this.bankData = bankData;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member == null || guild == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String id = member.getId();

        if (event.getFullCommandName().equalsIgnoreCase("bet")) {
            OptionMapping betType = event.getOption("bet-type");
            OptionMapping betOption = event.getOption("bet-option");
            OptionMapping betMoney = event.getOption("bet-money");

            if (betType == null || betOption == null || betMoney == null) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Invalid bet type or option.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            String type = betType.getAsString();
            String option = betOption.getAsString();
            long money = betMoney.getAsLong();

            Bet bet = getBet(type, id, option, money);

            if (bet == null) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This bet type \"" + type + "\" doesn't exist.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (bet.isValid()) {
                if (bet.getMoney() <= bankData.getMoney(id)) {
                    roulette.addBet(bet);
                    MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Bet", "Your " + type + " bet has been added to the Roulette.");
                    event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                } else {
                    MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "You don't have enough money.");
                    event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                }
            } else {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Invalid bet option \"" + option + "\" for \"" + type + "\".");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }
        }
    }

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (event.getName().equalsIgnoreCase("bet")) {
            String option = event.getFocusedOption().getName();
            String focusedValue = event.getFocusedOption().getValue();

            if (option.equalsIgnoreCase("bet-type")) {
                String[] options = new String[]{"color", "column", "dozen", "number"};
                event.replyChoices(Strings.getStartWithValues(options, focusedValue)).queue();
            } else if (option.equalsIgnoreCase("bet-option")) {
                OptionMapping betType = event.getOption("bet-type");

                if (betType == null) return;
                String[] options;

                switch (betType.getAsString()) {
                    case "color" -> options = new String[]{"green", "red", "black"};
                    case "column", "dozen" -> options = new String[]{"first", "second", "third"};
                    default -> options = new String[]{};
                }

                event.replyChoices(Strings.getStartWithValues(options, focusedValue)).queue();
            } else {
                event.replyChoices(Collections.emptyList()).queue();
            }
        }
    }

    private Bet getBet(String type, String id, String option, long money) {
        return switch (type) {
            case "color" -> new ColorBet(id, option, money);
            case "column" -> new ColumnBet(id, option, money);
            case "dozen" -> new DozenBet(id, option, money);
            case "number" -> new NumberBet(id, option, money);
            default -> null;
        };
    }
}

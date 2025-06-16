package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.bank.Bank;
import dev.tonimatas.systems.bank.Payment;
import dev.tonimatas.systems.bank.UserSettings;
import dev.tonimatas.systems.roulette.Roulette;
import dev.tonimatas.systems.roulette.bets.Bet;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.Strings;
import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;

public class SlashCommandListener extends ListenerAdapter {
    private static final String COMMANDS_CHANNEL = "1380277341405581443";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getFullCommandName();
        Member member = event.getMember();

        if (member == null || event.getGuild() == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }
        
        switch (command) {
            case "ping" -> executePing(event);
            case "bet" -> executeBet(event);
            case "money" -> executeMoney(event);
            case "money-top" -> executeMoneyTop(event);
            case "daily" -> executeDaily(event);
            case "pay" -> executePay(event);
            case "hi" -> executeHi(event);
            case "options" -> executeOptions(event);
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

    private void executePing(SlashCommandInteractionEvent event) {
        if (checkTheUseOfCommandsInTheCommandChannel(event)) return;

        long startTime = System.currentTimeMillis();

        event.reply("Pong!").queue(response ->
                response.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - startTime).queue());
    }
    
    private void executeBet(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        
        if (member == null || event.getGuild() == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }
        
        String id = member.getId();

        if (!event.getChannel().getId().equals(Roulette.getRoulette(event.getJDA()).getRouletteChannel().getId())) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Roulette channel.");
            event.replyEmbeds(embed).setEphemeral(true).queue();
        }

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

        Bet bet = Roulette.getBet(type, id, option, money);

        if (bet == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This bet type \"" + type + "\" doesn't exist.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (bet.isValid()) {
            if (bet.getMoney() <= BotFiles.BANK.getMoney(id)) {
                Roulette.getRoulette(event.getJDA()).addBet(bet);
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
    
    private void executeMoney(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if (member == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (checkTheUseOfCommandsInTheCommandChannel(event)) return;

        long money = BotFiles.BANK.getMoney(member.getId());
        MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money", "You have " + money + "€.");
        event.replyEmbeds(embed).queue();
    }
    
    private void executeMoneyTop(SlashCommandInteractionEvent event) {
        if (checkTheUseOfCommandsInTheCommandChannel(event)) return;

        MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money Top", Bank.getMoneyTopString(event.getGuild()));
        event.replyEmbeds(embed).queue();
    }
    
    private void executeDaily(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        if (member == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (checkTheUseOfCommandsInTheCommandChannel(event)) return;

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime claimedTime = BotFiles.BANK.getDaily(member.getId());

        if (now.isAfter(claimedTime.plusHours(24))) {
            BotFiles.BANK.addMoney(member.getId(), 100);
            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Daily", "Yeah! You claimed 100€.");
            event.replyEmbeds(embed).queue();
            BotFiles.BANK.setDaily(member.getId(), now);
            UserSettings settings = BotFiles.SETTINGS.getSettings(event.getUser().getId());
            settings.setNotifiedDaily(false);
            BotFiles.SETTINGS.save();
        } else {
            String formattedDate = BotFiles.BANK.getNextFormattedDaily(member.getId());
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "You need to wait more. Your next daily will be available at " + formattedDate);
            event.replyEmbeds(embed).queue();
        }
    }

    private void executePay(SlashCommandInteractionEvent event) {
        if (checkTheUseOfCommandsInTheCommandChannel(event)) return;

        Member sender = event.getMember();
        if (sender == null) {
            MessageEmbed err = Messages.getErrorEmbed(event.getJDA(), "Internal error. Sender not found, please try again later.");
            event.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        Member receiver = event.getOption("user").getAsMember();
        long amount = event.getOption("amount").getAsLong();
        String reason = event.getOption("reason") != null ? event.getOption("reason").getAsString() : null;

        Payment payment = new Payment(sender, receiver, amount, reason);

        if (!payment.isValid()) {
            event.replyEmbeds(Messages.getErrorEmbed(event.getJDA(), "Invalid data.")).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (!payment.canAfford()) {
            event.replyEmbeds(Messages.getErrorEmbed(event.getJDA(), "Insufficient funds.")).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        MessageEmbed confirmation = Messages.getDefaultEmbed(event.getJDA(), "Confirm Payment",
                String.format("Send **%d€** to **%s**?\\nFee: **%d€**\\nTotal: **%d€**\\nReason: %s",
                    payment.getAmount(),
                    receiver.getEffectiveName(),
                    payment.getFee(),
                    payment.getTotalCost(),
                    payment.getReason()));

        String confirmId = "pay:confirm" + sender.getId() + ":" + receiver.getId() + ":" + amount + ":" + payment.getReason().replaceAll(":", "||");
        String cancelId = "pay:cancel" + sender.getId();

        event.replyEmbeds(confirmation)
                .addActionRow(
                        Button.success(confirmId, "✅"),
                        Button.danger(cancelId, "❌")
                )
                .setEphemeral(true)
                .queue();
    }

    private void executeHi(SlashCommandInteractionEvent event) {
        checkTheUseOfCommandsInTheCommandChannel(event);

        LocalTime now = LocalTime.now();

        String greeting;
        String userName = event.getMember().getEffectiveName();

        if (TimeUtils.isBetween(now, 6, 0, 12, 30)) {
            greeting = "☀️ ¡Buenos días, " + userName + "! 😊";
        } else if (TimeUtils.isBetween(now, 12, 31, 18, 0)) {
            greeting = "🌤️ ¡Buenas tardes, " + userName + "! 😄";
        } else if (TimeUtils.isBetween(now, 18, 1, 23, 59) || TimeUtils.isBetween(now, 0, 0, 2, 0)) {
            greeting = "🌙 ¡Buenas noches, " + userName + "! 😴";
        } else {
            greeting = "😠 ¡Duérmete, bot! Deja de saludar a estas horas...";
        }

        event.reply(greeting).queue();
    }

    private boolean checkTheUseOfCommandsInTheCommandChannel(SlashCommandInteractionEvent event) {
        if (!event.getChannel().getId().equals(COMMANDS_CHANNEL)) {
            MessageEmbed err = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Commands channel.");
            event.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return true;
        }

        return false;
    }

    private void executeOptions(SlashCommandInteractionEvent event) {
        checkTheUseOfCommandsInTheCommandChannel(event);
        boolean notify = event.getOption("daily_notify").getAsBoolean();
        UserSettings settings = BotFiles.SETTINGS.getSettings(event.getUser().getId());
        settings.setNotifyDaily(notify);
        settings.setNotifiedDaily(false);
        BotFiles.SETTINGS.save();

        MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Settings changed",
                "Daily notifier " + (notify ? "enabled." : "disabled."));

        event.replyEmbeds(embed).setEphemeral(true).queue();
    }

}
package dev.tonimatas.listeners;

import dev.tonimatas.roulette.bets.*;
import dev.tonimatas.tasks.RouletteTask;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class RouletteListener extends ListenerAdapter {
    private final RouletteTask rouletteTask;
    
    public RouletteListener(RouletteTask rouletteTask) {
        this.rouletteTask = rouletteTask;
    }
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member == null || guild == null) {
            event.reply("Internal error. Please try again later.").setEphemeral(true).queue(deleteBefore());
            return;
        }

        String id = member.getId();

        switch (event.getFullCommandName()) {
            case "bet" -> {
                OptionMapping betType = event.getOption("bet-type");
                OptionMapping betOption = event.getOption("bet-option");
                OptionMapping betMoney = event.getOption("bet-money");
                
                if (betType == null || betOption == null || betMoney == null) {
                    event.reply("Invalid bet type or option.").setEphemeral(true).queue(deleteBefore());
                    return;
                }
                
                String option = betOption.getAsString();
                long money = betType.getAsLong();

                Bet bet = switch (betType.getAsString()) {
                    case "color" -> new ColorBet(id, option, money);
                    case "column" -> new ColumnBet(id, option, money);
                    case "dozen" -> new DozenBet(id, option, money);
                    case "number" -> new NumberBet(id, option, money);
                    default -> null;
                };

                if (bet == null) {
                    event.reply("This bet type \"" + betType.getAsString() + "\" doesn't exist.").setEphemeral(true).queue(deleteBefore());
                    return;
                }
                
                if (bet.isValid()) {
                    rouletteTask.get().addBet(bet);
                    event.reply("Your " + betType.getAsString() + " bet has been added to the Roulette.").setEphemeral(true).queue(deleteBefore());
                } else {
                    event.reply("Invalid bet option \"" + option + "\" for \"" + betType.getAsString() + "\".").setEphemeral(true).queue(deleteBefore());
                }
            }

            case "money" -> {
                long money = rouletteTask.getMoney(member.getId());
                event.reply("Tienes " + money + "€.").queue();
            }

            case "moneytop" -> {
                List<Map.Entry<String, Long>> sortedList = rouletteTask.getBank().entrySet()
                        .stream()
                        .sorted((a, b) ->
                                b.getValue().compareTo(a.getValue()))
                        .toList();
                StringBuilder text = new StringBuilder("**Top 5 más ricos:**\n\n");
                int counter = 0;
                for (Map.Entry<String, Long> entry : sortedList) {
                    if (counter >= 5) break;
                    Member m = guild.getMemberById(entry.getKey());
                    String name = (m != null) ? m.getEffectiveName() : "Usuario Desconocido";
                    text.append((counter + 1))
                            .append(". ")
                            .append(name)
                            .append(" ")
                            .append(entry.getValue())
                            .append("€\n");
                    counter++;
                }

                if (counter == 0) {
                    event.reply("No hay datos suficientes para mostrar el ranking.").queue();
                } else {
                    event.reply(text.toString()).queue();
                }
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
                event.replyChoices(getStartWithValues(options, focusedValue)).queue();
            } else if (option.equalsIgnoreCase("bet-option")) {
                OptionMapping betType = event.getOption("bet-type");
                
                if (betType == null) return;
                String[] options;
                
                switch (betType.getAsString()) {
                    case "color" -> options = new String[]{"green", "red", "black"};
                    case "column", "dozen" -> options = new String[]{"first", "second", "third"};
                    default -> options = new String[]{};
                }

                event.replyChoices(getStartWithValues(options, focusedValue)).queue();
            }
        }
    }
    
    
    
    private List<Command.Choice> getStartWithValues(String[] values, String focusedValue) {
        return Stream.of(values)
                .filter(value -> value.startsWith(focusedValue))
                .map(value -> new Command.Choice(value, value))
                .toList();
    }
    
    private Consumer<InteractionHook> deleteBefore() {
        return hook -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            hook.deleteOriginal().queue();
        };
    }
}

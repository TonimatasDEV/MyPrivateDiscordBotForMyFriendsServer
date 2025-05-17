package dev.tonimatas.listeners;

import dev.tonimatas.tasks.RouletteTask;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RouletteListener extends ListenerAdapter {
    private final RouletteTask rouletteTask;
    
    public RouletteListener(RouletteTask rouletteTask) {
        this.rouletteTask = rouletteTask;
    }
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        switch (event.getFullCommandName()) {
            case "bet" -> {
                event.reply("Choose your bet").setEphemeral(true).addActionRow(getBetTypeSelection()).queue();
            }

            case "money" -> {
                if (member != null) {
                    long money = rouletteTask.getMoney(member.getId());
                    event.reply("Tienes " + money + "€.").queue();
                } else {
                    event.reply("Error obteniendo tú dinero.").setEphemeral(true).queue();
                }
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
                    Member m = event.getGuild().getMemberById(entry.getKey());
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
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("roulette-bet-type-menu")) {
            String type = event.getId();
            event.replyModal(getOptions(type)).queue();
            event.getMessage().delete().queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().equals("roulette-bet-modal")) {
            for (ModalMapping modalMapping : event.getValues()) {
                event.reply(modalMapping.getAsString()).setEphemeral(true).queue();
            }
        }
    }

    private Modal getOptions(String type) {
        TextInput option = TextInput.create("roulette-bet-" + type, "What " + type + " do you want to bet?", TextInputStyle.SHORT)
                .setPlaceholder("1,2,3,4,5...")
                .setMinLength(1)
                .setMaxLength(2)
                .build();
        
        return Modal.create("roulette-bet-modal", "Bet").addActionRow(option).build();
    }
    
    private StringSelectMenu getBetTypeSelection() {
        return StringSelectMenu.create("roulette-bet-type-menu")
                .addOption("Color", "color")
                .addOption("Column", "column")
                .addOption("Dozen", "dozen")
                .addOption("Number", "number")
                .build();
    }

    private StringSelectMenu getOptionSelection(String type) {
        StringSelectMenu.Builder builder = StringSelectMenu.create("roulette-bet-option-menu");
        
        switch (type) {
            case "color" -> {
                
            }
            
            case "column" -> {
                
            }
            
            case "dozen" -> {
                
            }
            
            case "number" -> {
                for (int i = 0; i < 37; i++) {
                    String value = String.valueOf(i);
                    builder.addOption(value, value);
                }
            }
        }
        
        return builder.build();
    }
}

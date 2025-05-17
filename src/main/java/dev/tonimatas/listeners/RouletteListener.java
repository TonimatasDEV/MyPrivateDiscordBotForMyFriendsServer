package dev.tonimatas.listeners;

import dev.tonimatas.tasks.RouletteTask;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
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
                // TODO
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
}

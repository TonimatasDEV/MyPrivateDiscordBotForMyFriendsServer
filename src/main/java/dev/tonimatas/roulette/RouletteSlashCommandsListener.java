package dev.tonimatas.roulette;

import dev.tonimatas.schedules.RouletteManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class RouletteSlashCommandsListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        switch (event.getFullCommandName()) {
            case "bet" -> {
                OptionMapping type = event.getOption("type");
                OptionMapping money = event.getOption("money");
                OptionMapping value = event.getOption("value");

                if (member != null && type != null && money != null && value != null) {
                    BetType betType = BetType.valueOf(type.getAsString());

                    Bet bet = new Bet(member.getId(), money.getAsLong(), betType, value.getAsInt());

                    if (RouletteManager.roulette.addBet(bet)) {
                        event.reply("Apuesta aceptada.\nHas apostado **" + money.getAsLong() + "€** al valor **" + value.getAsInt() + "** de tipo **" + betType.name() + "**.").queue();
                    } else {
                        event.reply("Apuesta rechazada.").setEphemeral(true).queue();
                    }
                }
            }

            case "money" -> {
                if (member != null) {
                    long money = RouletteManager.bankAccounts.get(member.getId());
                    event.reply("Tienes " + money + "€.").queue();
                } else {
                    event.reply("Error obteniendo tú dinero.").setEphemeral(true).queue();
                }
            }

            case "moneytop" -> {
                List<Map.Entry<String, Long>> sortedList = RouletteManager.bankAccounts.entrySet()
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

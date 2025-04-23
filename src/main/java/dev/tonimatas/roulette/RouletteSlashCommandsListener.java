package dev.tonimatas.roulette;

import dev.tonimatas.schedules.RouletteManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

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
                // TODO: Implement me!



                // Ixgal 7658924376856234853245€
                // DaniPraivet 10000000000€
                // Dona 1000€
                // Érika -120€
                // Nerea -17298364€
            }
        }
    }
}

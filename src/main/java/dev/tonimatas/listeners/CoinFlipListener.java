package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.security.SecureRandom;
import java.util.Random;

public class CoinFlipListener extends ListenerAdapter {
    private static final Random RANDOM = new SecureRandom();
    
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String clickerId = event.getUser().getId();
        String id = event.getButton().getCustomId();
        
        if (id != null && id.startsWith("coinflip")) {
            String[] idSplit = id.split("-");
            String ownerId = idSplit[1];
            boolean ownerOption = Boolean.parseBoolean(idSplit[2]);
            long money = Long.parseLong(idSplit[3]);

            if (ownerId.equalsIgnoreCase(clickerId)) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "You can't flip your own coinflips.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (money > BotFiles.USER.get(clickerId).getMoney()) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Insufficient funds.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }
            
            String winnerName;
            if (RANDOM.nextBoolean() == ownerOption) {
                User owner = event.getJDA().getUserById(ownerId);
                winnerName = owner == null ? "Unknown" : owner.getEffectiveName();

                BotFiles.USER.get(ownerId).addMoney(money);
                BotFiles.USER.get(clickerId).removeMoney(money);
            } else {
                winnerName = event.getUser().getEffectiveName();

                BotFiles.USER.get(clickerId).addMoney(money);
                BotFiles.USER.get(ownerId).removeMoney(money);
            }


            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Coinflip", winnerName + " won " + money + ".");
            event.editMessageEmbeds(embed).setComponents().queue();
        }
    }
}

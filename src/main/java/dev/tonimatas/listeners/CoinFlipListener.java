package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.Utils;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CoinFlipListener extends ListenerAdapter {

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

            User owner = event.getJDA().getUserById(ownerId);
            boolean result = Utils.RANDOM.nextBoolean();

            String winnerName;
            String lostName;
            if (result == ownerOption) {
                winnerName = owner == null ? "Unknown" : owner.getEffectiveName();
                lostName = event.getUser().getEffectiveName();

                BotFiles.USER.get(ownerId).addMoney(money);
                BotFiles.USER.get(clickerId).removeMoney(money);
            } else {
                winnerName = event.getUser().getEffectiveName();
                lostName = owner == null ? "Unknown" : owner.getEffectiveName();

                BotFiles.USER.get(clickerId).addMoney(money);
                BotFiles.USER.get(ownerId).removeMoney(money);
            }

            String landed = result ? "heads" : "tails";
            
            ;
            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Coinflip",
                    String.format("%s won %d€ and %s lost %d€ because it landed %s.", winnerName, money * 2, lostName, money, landed));
            event.editMessageEmbeds(embed).setComponents().queue();
        }
    }
}

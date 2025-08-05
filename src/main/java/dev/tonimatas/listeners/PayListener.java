package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

public class PayListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");

        if (!parts[0].equals("pay")) return;

        String action = parts[1];

        if (action.equals("cancel")) {
            event.editMessageEmbeds(Messages.getErrorEmbed(event.getJDA(), "Payment cancelled."))
                    .setComponents()
                    .queue(Messages.deleteBeforeX(10));
            return;
        }

        if (action.equals("confirm")) {
            confirm(event.getJDA(), event.getInteraction(), parts);
        }
    }

    public void confirm(JDA jda, ButtonInteraction interaction, String[] parts) {
        String receiverId = parts[2];
        long amount = Long.parseLong(parts[3]);
        String reason = parts[4].replace("‖", ":");

        User sender = interaction.getUser();
        User receiver = jda.getUserById(receiverId);

        if (receiver == null) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "One of the users is no longer available.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (BotFiles.USER.get(sender.getId()).getMoney() < amount) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "You no longer have enough money.");
            interaction.editMessageEmbeds(embed).setComponents().queue(Messages.deleteBeforeX(10));
            return;
        }

        long fee = (long) (amount * 0.05);

        if (!reason.isEmpty()) {
            reason = (reason.endsWith(".") ? reason : reason + ".");
        }

        BotFiles.USER.get(sender.getId()).removeMoney(amount, "Sent to " + receiver.getEffectiveName() + reason);
        BotFiles.USER.get(receiver.getId()).addMoney(amount - fee, "Received by " + sender.getEffectiveName() + " because: " + reason);

        MessageEmbed success = Messages.getDefaultEmbed(jda, "Payment",
                String.format("%s sent **%d€** to %s (Fee: %d€)%nReason: %s",
                        sender.getEffectiveName(),
                        amount - fee,
                        receiver.getEffectiveName(),
                        fee,
                        reason
                )
        );

        interaction.deferEdit().complete();
        interaction.getMessage().delete().complete();
        interaction.getChannel().sendMessageEmbeds(success).queue();
    }
}

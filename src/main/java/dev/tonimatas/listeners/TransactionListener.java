package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

public class TransactionListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        Guild guild = event.getGuild();

        if (guild == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String[] parts = event.getComponentId().split(":");

        if (!parts[0].equals("pay")) return;

        String action = parts[1];
        String userId = parts[2];

        if (!event.getUser().getId().equals(userId)) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "You can't interact with this payment.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (action.equals("cancel")) {
            event.editMessageEmbeds(Messages.getErrorEmbed(event.getJDA(), "Transaction cancelled."))
                    .setComponents()
                    .queue(Messages.deleteBeforeX(10));
            return;
        }

        if (action.equals("confirm")) {
            confirm(event.getJDA(), event.getInteraction(), guild, parts);
        }
    }
    
    public void confirm(JDA jda, ButtonInteraction interaction, Guild guild, String[] parts) {
        String userId = parts[2];
        String receiverId = parts[3];
        long amount = Long.parseLong(parts[4]);
        String reason = parts.length >= 6 ? parts[5].replace("‖", ":") : "";

        Member sender = guild.getMemberById(userId);
        Member receiver = guild.getMemberById(receiverId);

        if (sender == null || receiver == null) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "One of the users is no longer available.");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (BotFiles.BANK.getMoney(sender.getId()) < amount) {
            MessageEmbed embed = Messages.getErrorEmbed(jda, "You no longer have enough money.");
            interaction.editMessageEmbeds(embed).setComponents().queue(Messages.deleteBeforeX(10));
            return;
        }

        long fee = (long) (amount * 0.05);

        if (!reason.isEmpty()) {
            reason = " because: " + (reason.endsWith(".") ? reason : reason + ".");
        }

        BotFiles.BANK.removeMoney(sender.getId(), amount, "Sent to " + receiver.getEffectiveName() + reason);
        BotFiles.BANK.addMoney(receiver.getId(), amount - fee, "Received by " + sender.getEffectiveName() + " because: " + reason);

        MessageEmbed success = Messages.getDefaultEmbed(jda, "Transaction Successful",
                String.format("%s sent **%d€** to %s (Fee: %d€)%nReason: %s",
                        sender.getEffectiveName(),
                        amount - fee,
                        receiver.getEffectiveName(),
                        fee,
                        reason
                )
        );

        interaction.editMessageEmbeds(success).setComponents().queue(Messages.deleteBeforeX(15));
    }
}

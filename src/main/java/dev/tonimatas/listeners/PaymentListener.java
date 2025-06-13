package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.bank.Payment;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PaymentListener extends ListenerAdapter {
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String[] parts = event.getComponentId().split(":");

        if (!parts[0].equals("pay")) return;

        String action = parts[1];
        String userId = parts[2];

        if (!event.getUser().getId().equals(userId)) {
            event.reply("You can't interact with this payment.").setEphemeral(true).queue();
            return;
        }

        if (action.equals("cancel")) {
            event.editMessageEmbeds(Messages.getErrorEmbed(event.getJDA(), "Payment cancelled."))
                    .setComponents()
                    .queue();
            return;
        }

        if (action.equals("confirm")) {
            String receiverId = parts[3];
            long amount = Long.parseLong(parts[4]);
            String reason = parts.length >= 6 ? parts[5].replaceAll("‖", ":") : null;

            Member sender = event.getGuild().getMemberById(userId);
            Member receiver = event.getGuild().getMemberById(receiverId);

            if (sender == null || receiver == null) {
                event.reply("One of the users is no longer available.").setEphemeral(true).queue();
                return;
            }

            Payment payment = new Payment(sender, receiver, amount, reason);

            if (!payment.canAfford()) {
                event.editMessageEmbeds(Messages.getErrorEmbed(event.getJDA(), "You no longer have enough money."))
                        .setComponents()
                        .queue();
                return;
            }

            payment.execute();
            BotFiles.PAYMENTS.addPayment(payment);

            MessageEmbed success = Messages.getDefaultEmbed(event.getJDA(), "Payment Successful",
                    String.format("%s sent **%d€** to %s (Fee: %d€)\nReason: %s",
                            sender.getEffectiveName(),
                            payment.getAmount(),
                            receiver.getEffectiveName(),
                            payment.getFee(),
                            payment.getReason()));

            event.editMessageEmbeds(success).setComponents().queue();
        }
    }
}

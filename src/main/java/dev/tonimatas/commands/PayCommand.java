package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.*;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

public class PayCommand {
    @Command("pay")
    @Description("Send an amount of money to a member.")
    @GuildOnly
    public void execute(SlashCommandActor actor, 
                        @Named("user") @Description("The member who is gonna receive your money.") User user,
                        @Named("amount") @Description("The quantity of money you are gonna loose.") @Range(min = 1) long amount,
                        @Named("reason") @Description("If you want to say why are you paying.") @Optional String reason)
    {
        if (CommandUtils.isNotCommandsChannel(actor)) return;

        JDA jda = actor.jda();
        User sender = actor.user();
        
        reason = reason != null ? reason : "No reason provided";

        if (user != null) {
            if (amount <= 0) {
                MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "You can't sent 0€.");
                actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (amount > BotFiles.USER.get(sender.getId()).getMoney()) {
                MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "Insufficient funds.");
                actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (user.isBot()) {
                MessageCreateData embed = Messages.getErrorEmbed_Lamp(jda, "Invalid receiver. Please try again later.");
                actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            long fee = (long) (amount * 0.05);

            MessageCreateData confirmation = Messages.getDefaultEmbed_Lamp(jda, "Confirm payment",
                    String.format("""
                                    Send **%d€** to **%s**? Fee: **%d€**
                                    Total: **%d€**
                                    Reason: %s
                                    """,
                            amount - fee,
                            user.getEffectiveName(),
                            fee,
                            amount,
                            reason)
            );

            String confirmId = "pay:confirm:" + user.getId() + ":" + amount + ":" + reason.replace(":", "‖");
            String cancelId = "pay:cancel";

            actor.replyToInteraction(confirmation)
                    .addComponents(
                            ActionRow.of(
                                    Button.success(confirmId, "✅"),
                                    Button.danger(cancelId, "❌")
                            )
                    ).setEphemeral(true).queue();
        }
    }
}

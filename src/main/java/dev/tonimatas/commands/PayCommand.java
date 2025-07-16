package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.Set;

public class PayCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        JDA jda = interaction.getJDA();
        User sender = interaction.getUser();

        OptionMapping userOption = interaction.getOption("user");
        OptionMapping amountOption = interaction.getOption("amount");
        OptionMapping reasonOption = interaction.getOption("reason");
        String reason = reasonOption != null ? reasonOption.getAsString() : "No reason provided";

        if (userOption != null && amountOption != null) {
            Member receiver = userOption.getAsMember();
            long amount = amountOption.getAsLong();

            if (amount <= 0) {
                MessageEmbed embed = Messages.getErrorEmbed(jda, "You can't sent 0€.");
                interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (amount > BotFiles.USER.get(sender.getId()).getMoney()) {
                MessageEmbed embed = Messages.getErrorEmbed(jda, "Insufficient funds.");
                interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            if (receiver == null) {
                MessageEmbed embed = Messages.getErrorEmbed(jda, "Invalid receiver. Please try again later.");
                interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            long fee = (long) (amount * 0.05);

            MessageEmbed confirmation = Messages.getDefaultEmbed(jda, "Confirm Transaction",
                    String.format("Send **%d€** to **%s**? \nFee: **%d€** \nTotal: **%d€** \nReason: %s",
                            amount - fee,
                            receiver.getEffectiveName(),
                            fee,
                            amount,
                            reason));

            String confirmId = "pay:confirm:" + sender.getId() + ":" + receiver.getId() + ":" + amount + ":" + reason.replace(":", "||");
            String cancelId = "pay:cancel:" + sender.getId();

            interaction.replyEmbeds(confirmation)
                    .addComponents(
                            ActionRow.of(
                                    Button.success(confirmId, "✅"),
                                    Button.danger(cancelId, "❌")
                            )
                    )
                    .setEphemeral(true)
                    .queue();
        }
    }

    @Override
    public String getName() {
        return "pay";
    }

    @Override
    public String getDescription() {
        return "Send an amount of money to a member.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOption(OptionType.USER, "user", "The member who is gonna receive your money.", true)
                .addOption(OptionType.STRING, "amount", "The quantity of money you are gonna loose.", true)
                .addOption(OptionType.STRING, "reason", "If you want to say why are you paying.");
    }
}

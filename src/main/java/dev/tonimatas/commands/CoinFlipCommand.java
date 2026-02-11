package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.Choices;
import revxrsal.commands.jda.annotation.GuildOnly;

public class CoinFlipCommand {
    @Command("coinflip")
    @Description("Flip the coin and steal money from the others with your luck.")
    @GuildOnly
    public void execute(SlashCommandActor actor,
                        @Description("Select what you want to play. Heads or tails.") @Choices(value = {"heads", "tails"}) String type,
                        @Description("Money amount") Long money
    ) {
        User user = actor.user();

        boolean typeBoolean = type.equalsIgnoreCase("heads");

        if (money > BotFiles.USER.get(user.getId()).getMoney()) {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(actor.jda(), "Insufficient funds.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        if (typeBoolean || type.equalsIgnoreCase("tails")) {
            MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Coinflip", "**User:** " + user.getEffectiveName() + "." +
                    "\n**Option:** " + type + "." +
                    "\n**Money:** " + money + "€.");
            String buttonId = "coinflip-" + user.getId() + "-" + typeBoolean + "-" + money;
            actor.replyToInteraction(embed).addComponents(
                    ActionRow.of(
                            Button.of(ButtonStyle.PRIMARY, buttonId, "Flip", Emoji.fromUnicode("🪙"))
                    )
            ).queue();
        } else {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(actor.jda(), "Invalid options. Please recheck your command options.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.jda.actor.SlashCommandActor;

import java.util.Set;

public class OptionsCommand  {
    @Command("options")
    @Description("Configure your preferences")
    public void execute(SlashCommandActor actor, @Named("daily_notify") @Description("Do you prefer if the bot remembers when your daily reward is up?") Boolean dailyNotify) {
        if (dailyNotify != null) {
            String userId = actor.user().getId();

            BotFiles.USER.get(userId).getSettings().setNotifyDaily(dailyNotify);
            BotFiles.USER.get(userId).getDaily().setNotified(false);

            String description = "Daily notifier " + (dailyNotify ? "enabled." : "disabled.");
            MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Settings changed", description);
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }
}

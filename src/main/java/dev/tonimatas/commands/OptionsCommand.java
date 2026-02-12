package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.jda.actor.SlashCommandActor;

public class OptionsCommand {
    @Command("options")
    @Description("Configure your preferences")
    @Subcommand("daily_notify")
    public void execute(SlashCommandActor actor, @Named("enabled") @Description("Do you prefer if the bot remembers when your daily reward is up?") boolean enable) {
        String userId = actor.user().getId();

        BotFiles.USER.get(userId).getSettings().setNotifyDaily(enable);
        BotFiles.USER.get(userId).getDaily().setNotified(false);

        String description = "Daily notifier " + (enable ? "enabled." : "disabled.");
        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Settings changed", description);
        actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
    }
}

package dev.tonimatas.util;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import revxrsal.commands.jda.actor.SlashCommandActor;

public class CommandUtils {
    private CommandUtils() {
        // We don't need a constructor
    }

    public static boolean isNotCommandsChannel(SlashCommandInteraction interaction) {
        if (!interaction.getChannel().getId().equals(BotFiles.CONFIG.getCommandsChannelId())) {
            MessageEmbed err = Messages.getErrorEmbed(interaction.getJDA(), "This command can only be run in the Commands channel.");
            interaction.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return true;
        }

        return false;
    }

    public static boolean isNotCommandsChannel(SlashCommandActor actor) {
        if (!actor.channel().getId().equals(BotFiles.CONFIG.getCommandsChannelId())) {
            MessageEmbed err = Messages.getErrorEmbed(actor.jda(), "This command can only be run in the Commands channel.");
            actor.replyToInteraction(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return true;
        }

        return false;
    }
}

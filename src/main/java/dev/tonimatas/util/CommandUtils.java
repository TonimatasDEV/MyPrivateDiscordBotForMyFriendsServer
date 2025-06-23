package dev.tonimatas.util;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CommandUtils {
    private static final String COMMANDS_CHANNEL = "1380277341405581443";

    private CommandUtils() {
        // We don't need a constructor
    }
    
    public static boolean isNotCommandsChannel(SlashCommandInteraction interaction) {
        if (!interaction.getChannel().getId().equals(COMMANDS_CHANNEL)) {
            MessageEmbed err = Messages.getErrorEmbed(interaction.getJDA(), "This command can only be run in the Commands channel.");
            interaction.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return true;
        }

        return false;
    }
}

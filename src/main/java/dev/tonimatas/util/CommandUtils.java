package dev.tonimatas.util;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

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

    public static boolean isNotTicTacToeChannel(SlashCommandInteraction interaction) {
        if (!interaction.getChannel().getId().equals(BotFiles.CONFIG.getTicTacToeChannelId())) {
            MessageEmbed err = Messages.getErrorEmbed(interaction.getJDA(), "This command can only be run in the TicTacToe channel.");
            interaction.replyEmbeds(err).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return true;
        }

        return false;
    }
}

package dev.tonimatas.listeners;

import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SlashCommandListener extends ListenerAdapter {
    private static final String COMMANDS_CHANNEL = "1380277341405581443";

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String command = event.getFullCommandName();

        if (command.equalsIgnoreCase("ping")) {
            if (!event.getChannel().getId().equals(COMMANDS_CHANNEL)) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Commands channel.");
                event.replyEmbeds(embed).setEphemeral(true).queue();
            }
            
            long startTime = System.currentTimeMillis();

            event.reply("Pong!").queue(response ->
                    response.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - startTime).queue());
        }
    }
}

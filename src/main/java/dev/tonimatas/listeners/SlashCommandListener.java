package dev.tonimatas.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

// TODO: Convert it a task
public class SlashCommandListener extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getFullCommandName()) {
            case "ping" -> {
                long startTime = System.currentTimeMillis();
                
                event.reply("Pong!").setEphemeral(true).queue(response -> 
                        response.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - startTime).queue());
            }
        }
    }
}

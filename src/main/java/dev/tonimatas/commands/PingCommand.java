package dev.tonimatas.commands;

import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;

public class PingCommand {
    @Command("ping")
    @Description("Discord Ping! Pong!")
    public void execute(SlashCommandActor actor) {
        long startTime = System.currentTimeMillis();

        actor.replyToInteraction("Pong!").setEphemeral(true).queue(response ->
                response.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - startTime).queue());
    }
}

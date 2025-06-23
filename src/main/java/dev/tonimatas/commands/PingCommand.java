package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.List;

public class PingCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        long startTime = System.currentTimeMillis();

        interaction.reply("Pong!").setEphemeral(true).queue(response ->
                response.editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - startTime).queue());
    }

    @Override
    public String getCommandName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Discord Ping! Pong!";
    }

    @Override
    public List<InteractionContextType> getContexts() {
        return InteractionContextType.ALL.stream().toList();
    }
}

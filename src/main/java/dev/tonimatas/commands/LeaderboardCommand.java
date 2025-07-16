package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.util.CommandUtils;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Set;

public class LeaderboardCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;
        
    }

    @Override
    public String getName() {
        return "leaderboard";
    }

    @Override
    public String getDescription() {
        return "Shows the leaderboard.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

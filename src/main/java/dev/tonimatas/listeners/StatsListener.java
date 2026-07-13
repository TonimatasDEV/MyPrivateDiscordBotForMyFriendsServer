package dev.tonimatas.listeners;

import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class StatsListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        getUserStats(event.getUser()).increaseCommandsExecuted();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        getUserStats(event.getAuthor()).increaseMessagesSent();
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        getUserStats(event.retrieveUser().complete()).increaseTimesReacted();
    }

    private UserStats getUserStats(User user) {
        return BotFiles.USER.get(user.getId()).getStats();
    }
}

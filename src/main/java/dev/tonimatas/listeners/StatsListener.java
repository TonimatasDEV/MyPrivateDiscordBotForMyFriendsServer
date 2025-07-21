package dev.tonimatas.listeners;

import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ShutdownEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatsListener extends ListenerAdapter {
    private final Map<String, LocalDateTime> inVoiceMembers = new ConcurrentHashMap<>();

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        getUserStats(event.getUser()).increaseCommandsExecuted();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        getUserStats(event.getAuthor()).increaseMessagesSent();
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        String userId = event.getEntity().getId();
        AudioChannelUnion join = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        if (join != null) {
            inVoiceMembers.put(userId, LocalDateTime.now());
        } else if (left != null) {
            getUserStats(event.getEntity().getUser()).increaseTimeInVoice(inVoiceMembers.get(userId));
            inVoiceMembers.remove(userId);
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        event.getGuild().getMembers().forEach(member -> {
            GuildVoiceState voiceState = member.getVoiceState();
            if (voiceState != null && voiceState.inAudioChannel()) {
                inVoiceMembers.put(member.getId(), LocalDateTime.now());
            }
        });
    }

    @Override
    public void onShutdown(@NotNull ShutdownEvent event) {
        for (Map.Entry<String, LocalDateTime> entry : inVoiceMembers.entrySet()) {
            BotFiles.USER.get(entry.getKey()).getStats().increaseTimeInVoice(entry.getValue());
        }
    }

    private UserStats getUserStats(User user) {
        return BotFiles.USER.get(user.getId()).getStats();
    }
}

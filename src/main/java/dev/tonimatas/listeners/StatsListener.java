package dev.tonimatas.listeners;

import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.executors.ExecutorManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.GenericSessionEvent;
import net.dv8tion.jda.api.events.session.SessionState;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class StatsListener extends ListenerAdapter {
    private final Map<String, LocalDateTime> inVoiceMembers = new HashMap<>();

    public StatsListener() {
        ExecutorManager.addStopTask(this::save);
    }
    
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        getUserStats(event.getUser()).increaseCommandsExecuted();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        getUserStats(event.getAuthor()).increaseMessagesSent();
    }

    @Override
    public void onGenericSession(GenericSessionEvent event) {
        SessionState state = event.getState();
        
        if (state == SessionState.INVALIDATED || state == SessionState.DISCONNECTED) {
            save();
        }
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        String userId = event.getEntity().getId();
        AudioChannelUnion join = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        synchronized (inVoiceMembers) {
            if (join != null) {
                inVoiceMembers.put(userId, LocalDateTime.now());
            } else if (left != null) {
                LocalDateTime joinTime = inVoiceMembers.remove(userId);
                if (joinTime != null) {
                    getUserStats(event.getEntity().getUser()).increaseTimeInVoice(joinTime);
                }
            }
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        synchronized (inVoiceMembers) {
            event.getGuild().getMembers().forEach(member -> {
                GuildVoiceState voiceState = member.getVoiceState();
                if (voiceState != null && voiceState.inAudioChannel()) {
                    inVoiceMembers.put(member.getId(), LocalDateTime.now());
                }
            });
        }
    }

    private void save() {
        Map<String, LocalDateTime> snapshot;

        synchronized (inVoiceMembers) {
            snapshot = new HashMap<>(inVoiceMembers);
            inVoiceMembers.clear();
        }

        for (Map.Entry<String, LocalDateTime> entry : snapshot.entrySet()) {
            BotFiles.USER.get(entry.getKey()).getStats().increaseTimeInVoice(entry.getValue());
        }
    }

    private UserStats getUserStats(User user) {
        return BotFiles.USER.get(user.getId()).getStats();
    }
}

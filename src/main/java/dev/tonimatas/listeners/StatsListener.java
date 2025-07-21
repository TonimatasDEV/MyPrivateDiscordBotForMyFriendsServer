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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StatsListener extends ListenerAdapter {
    private final Map<String, LocalDateTime> inVoiceMembers = new ConcurrentHashMap<>();

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

    private void save() {
        for (Map.Entry<String, LocalDateTime> entry : inVoiceMembers.entrySet()) {
            BotFiles.USER.get(entry.getKey()).getStats().increaseTimeInVoice(entry.getValue());
        }

        inVoiceMembers.clear();
    }

    private UserStats getUserStats(User user) {
        return BotFiles.USER.get(user.getId()).getStats();
    }
}

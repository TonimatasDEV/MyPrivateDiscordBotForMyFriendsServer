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
import net.dv8tion.jda.api.events.session.SessionDisconnectEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

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
    public void onSessionDisconnect(@NotNull SessionDisconnectEvent event) {
        save();
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        String userId = event.getMember().getId();
        AudioChannelUnion join = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        synchronized (inVoiceMembers) {
            if (left != null) {
                LocalDateTime joinTime = inVoiceMembers.remove(userId);
                if (joinTime != null) {
                    getUserStats(event.getMember().getUser()).increaseTimeInVoice(joinTime);
                }
            }
            
            if (join != null) {
                inVoiceMembers.put(userId, LocalDateTime.now());
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
        synchronized (inVoiceMembers) {
            for (Map.Entry<String, LocalDateTime> entry : inVoiceMembers.entrySet()) {
                BotFiles.USER.get(entry.getKey()).getStats().increaseTimeInVoice(entry.getValue());
            }

            inVoiceMembers.clear();
        }
    }

    private UserStats getUserStats(User user) {
        return BotFiles.USER.get(user.getId()).getStats();
    }
}

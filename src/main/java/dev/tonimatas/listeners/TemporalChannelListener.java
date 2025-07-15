package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TemporalChannelListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Category category = BotFiles.CONFIG.getTemporalCategory(event.getJDA());
        AudioChannelUnion joined = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        if (category == null) return;
        if (joined != null) {
            VoiceChannel voice = joined.asVoiceChannel();

            if (voice.getId().equals(BotFiles.CONFIG.getTemporaryChannelId())) {
                createChannel(category, event.getMember());
            }
        }

        if (left != null) {
            VoiceChannel voice = left.asVoiceChannel();

            if (voice.getId().equals(BotFiles.CONFIG.getTemporaryChannelId())) return;

            if (voice.getMembers().isEmpty()) {
                left.delete().queue();
            }
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Category category = BotFiles.CONFIG.getTemporalCategory(event.getJDA());

        if (category == null) return;

        removeEmptyChannels(category);
        createChannelIfNecessary(category);
    }

    private void createChannel(Category category, Member member) {
        category.createVoiceChannel(member.getEffectiveName()).queue(voiceChannel ->
                voiceChannel.getGuild().moveVoiceMember(member, voiceChannel).queue());
    }

    private void removeEmptyChannels(Category category) {
        for (VoiceChannel voiceChannel : category.getVoiceChannels()) {
            if (voiceChannel.getId().equals(BotFiles.CONFIG.getTemporaryChannelId())) continue;

            if (voiceChannel.getMembers().isEmpty()) {
                voiceChannel.delete().queue();
            }
        }
    }

    private void createChannelIfNecessary(Category category) {
        VoiceChannel voice = category.getGuild().getVoiceChannelById(BotFiles.CONFIG.getTemporaryChannelId());

        if (voice == null) return;

        if (!voice.getMembers().isEmpty()) {
            Member member = voice.getMembers().getFirst();

            category.createVoiceChannel(member.getEffectiveName()).queue(voiceChannel -> {
                for (Member toMove : voice.getMembers()) {
                    voiceChannel.getGuild().moveVoiceMember(toMove, voiceChannel).queue();
                }
            });
        }
    }
}

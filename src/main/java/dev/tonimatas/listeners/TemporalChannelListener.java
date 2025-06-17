package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TemporalChannelListener extends ListenerAdapter {
    private static final String CATEGORY_ID = "1371074573449302209";
    private static final String CREATE_ID = "1371077321817198704";

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Category category = event.getGuild().getCategoryById(CATEGORY_ID);
        AudioChannelUnion joined = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        if (category == null) return;
        if (joined != null) {
            VoiceChannel voice = joined.asVoiceChannel();

            if (voice.getId().equals(CREATE_ID)) {
                createChannel(category, event.getMember());
            }
        }

        if (left != null) {
            VoiceChannel voice = left.asVoiceChannel();

            if (voice.getId().equals(CREATE_ID)) return;

            if (voice.getMembers().isEmpty()) {
                left.delete().queue();
            }
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        Category category = event.getGuild().getCategoryById(CATEGORY_ID);

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
            if (voiceChannel.getId().equals(CREATE_ID)) continue;

            if (voiceChannel.getMembers().isEmpty()) {
                voiceChannel.delete().queue();
            }
        }
    }

    private void createChannelIfNecessary(Category category) {
        VoiceChannel voice = category.getGuild().getVoiceChannelById(CREATE_ID);

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

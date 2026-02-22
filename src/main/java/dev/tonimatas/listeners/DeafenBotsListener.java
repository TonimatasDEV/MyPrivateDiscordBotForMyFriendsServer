package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DeafenBotsListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        AudioChannelUnion join = event.getChannelJoined();

        if (join != null) {
            if (member.getUser().isBot() && !event.getVoiceState().isGuildDeafened()) {
                member.deafen(true).queue();
            }
        }
    }
}

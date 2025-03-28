package dev.tonimatas.utils;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Collections;
import java.util.List;

public class Voice {
    public static List<Member> getMembers(String channelId) {
        VoiceChannel channel = Main.JDA.getVoiceChannelById(channelId);

        if (channel == null) return Collections.emptyList();

        return channel.getMembers();
    }
}

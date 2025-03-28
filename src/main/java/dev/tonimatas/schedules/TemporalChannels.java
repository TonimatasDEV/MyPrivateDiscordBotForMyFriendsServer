package dev.tonimatas.schedules;

import dev.tonimatas.Main;
import dev.tonimatas.utils.Voice;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TemporalChannels extends Thread {
    private final List<String> channels = new ArrayList<>();
    
    @Override
    public void run() {
        while (!Main.STOP) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                System.out.println();
            }

            deleteVoidChannels();
            
            Category category = Main.JDA.getCategoryById("1292533360857583697");
            
            if (category == null) continue;
            
            for (Member member : Voice.getMembers("1300577748158386196")) {
                if (member == null) continue;

                category.createVoiceChannel(member.getEffectiveName()).queue(voiceChannel -> {
                    voiceChannel.getGuild().moveVoiceMember(member, voiceChannel).queue();
                    channels.add(voiceChannel.getId());
                });
            }
        }
    }
    
    private void deleteVoidChannels() {
        for (String channelId : channels) {
            VoiceChannel voiceChannel = Main.JDA.getVoiceChannelById(channelId);
            if (voiceChannel == null) {
                channels.remove(channelId);
                continue;
            }

            if (Voice.getMembers(channelId).isEmpty()) {
                voiceChannel.delete().queue(consumer -> channels.remove(channelId));
            }
        }
    }
}

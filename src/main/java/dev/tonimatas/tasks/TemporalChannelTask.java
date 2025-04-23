package dev.tonimatas.tasks;

import dev.tonimatas.Main;
import dev.tonimatas.utils.Getters;
import dev.tonimatas.utils.Voice;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TemporalChannelTask extends Thread {
    private final Queue<String> channels = new ConcurrentLinkedQueue<>();

    @Override
    public void run() {
        while (!Main.STOP) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

            deleteVoidChannels();

            for (Member member : Voice.getMembers("1300577748158386196")) {
                if (member == null) continue;

                Getters.getVoiceCategory().createVoiceChannel(member.getEffectiveName()).queue(voiceChannel ->
                        addChannel(voiceChannel, member));
            }
        }
    }
    
    private void addChannel(VoiceChannel voiceChannel, Member member) {
        try {
            voiceChannel.getGuild().moveVoiceMember(member, voiceChannel).queue(
                    nothing -> channels.add(voiceChannel.getId()),
                    throwable -> channels.add(voiceChannel.getId()));
        } catch (Exception e) {
            channels.add(voiceChannel.getId());
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

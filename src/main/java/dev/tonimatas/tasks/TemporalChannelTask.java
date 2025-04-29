package dev.tonimatas.tasks;

import dev.tonimatas.Main;
import dev.tonimatas.utils.Voice;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TemporalChannelTask implements Runnable {
    private static final String CATEGORY_ID = "1292533360857583697";
    private final Queue<String> channels = new ConcurrentLinkedQueue<>();
    private final JDA jda;
    
    public TemporalChannelTask(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        while (!Main.STOP) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

            deleteVoidChannels(jda);

            for (Member member : Voice.getMembers("1300577748158386196")) {
                if (member == null) continue;

                Category category = jda.getCategoryById(CATEGORY_ID);

                if (category == null) continue;

                category.createVoiceChannel(member.getEffectiveName()).queue(voiceChannel -> addChannel(voiceChannel, member));
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

    private void deleteVoidChannels(JDA jda) {
        for (String channelId : channels) {
            VoiceChannel voiceChannel = jda.getVoiceChannelById(channelId);
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

package dev.tonimatas.tasks;

import dev.tonimatas.config.Configs;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class TemporalChannelTask implements Runnable {
    private static final String CATEGORY_ID = "1371077395141885972";
    private static final String CREATE_ID = "1371077321817198704";
    private static final String DATA_KEY = "temporalChannels";
    private final Queue<String> channels;
    private final JDA jda;
    
    public TemporalChannelTask(JDA jda) {
        this.jda = jda;
        this.channels = new ConcurrentLinkedQueue<>();
        load();
    }

    @Override
    public void run() {
        for (;;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

            deleteVoidChannels(jda);

            for (Member member : getMembers(CREATE_ID)) {
                if (member == null) continue;

                Category category = jda.getCategoryById(CATEGORY_ID);

                if (category == null) continue;

                category.createVoiceChannel(member.getEffectiveName()).queue(voiceChannel -> addChannel(voiceChannel, member));
                
                save();
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
            
            OffsetDateTime createdAnd5Seconds = voiceChannel.getTimeCreated().plusSeconds(5);

            if (getMembers(channelId).isEmpty() && createdAnd5Seconds.isBefore(OffsetDateTime.now())) {
                voiceChannel.delete().queue(consumer -> channels.remove(channelId));
            }
        }
    }

    private List<Member> getMembers(String channelId) {
        VoiceChannel channel = jda.getVoiceChannelById(channelId);

        if (channel == null) return Collections.emptyList();

        return channel.getMembers();
    }
    
    private void load() {
        String[] storedChannels = Configs.DATA.getValue(DATA_KEY).get().split(",");
        this.channels.addAll(Arrays.asList(storedChannels));
    }
    
    private void save() {
        Configs.DATA.setValue(DATA_KEY, String.join(",", channels));
    }
}

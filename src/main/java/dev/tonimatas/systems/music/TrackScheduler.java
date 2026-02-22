package dev.tonimatas.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
 
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final JDA jda;

    public TrackScheduler(AudioPlayer player, JDA jda) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.jda = jda;
    }

    public void queue(AudioTrack track) {
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        }
        
        updateMusicMessage();
    }

    public void nextTrack() {
        player.startTrack(queue.poll(), false);
        updateMusicMessage();
    }
    
    public void stopQueue() {
        player.stopTrack();
        queue.clear();
        updateMusicMessage();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            nextTrack();
        }
    }
    
    public void updateMusicMessage() {
        StringBuilder message = new StringBuilder();
        message.append("This is the primary message of the music system. Here you have the controls!\n\n**Listening:** ")
                .append(player.getPlayingTrack() != null ? player.getPlayingTrack().getInfo().title : "None");
        
        if (!queue.isEmpty()) {
            message.append("\n\n## Queue:");
        }
        
        int id = 1;
        for (AudioTrack track : queue) {
            message.append("\n - ").append(id).append(": ").append(track.getInfo().title);
            id++;
        }

        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Music", message.toString());
        BotFiles.CONFIG.getMusicChannel(jda).editMessageEmbedsById(MusicManager.messageId, embed).queue();
    }
}
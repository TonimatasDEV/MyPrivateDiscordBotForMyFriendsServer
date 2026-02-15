package dev.tonimatas.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.JDA;

public class GuildMusicManager {
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    
    public GuildMusicManager(AudioPlayerManager manager, JDA jda) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player, jda);
        player.addListener(scheduler);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }
}
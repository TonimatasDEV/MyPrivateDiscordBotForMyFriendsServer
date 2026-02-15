package dev.tonimatas.systems.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.HashMap;
import java.util.Map;

public class MusicManager {
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager source = new YoutubeAudioSourceManager();
        source.useOauth2(null, false);
        playerManager.registerSourceManager(source);
    }

    public void loadAndPlay(final TextChannel channel, final VoiceChannel toConnect, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                MessageEmbed embed = Messages.getDefaultEmbed(channel.getJDA(), "Music", "Adding to queue " + track.getInfo().title);
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(10));
                play(channel.getGuild(), toConnect, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().getFirst();
                }

                MessageEmbed embed = Messages.getDefaultEmbed(channel.getJDA(), "Music", "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(10));

                play(channel.getGuild(), toConnect, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                MessageEmbed embed = Messages.getErrorEmbed(channel.getJDA(), "Could not play: " + exception.getMessage());
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(10));
            }
        });
    }

    private void play(Guild guild, VoiceChannel toConnect, GuildMusicManager musicManager, AudioTrack track) {
        guild.getAudioManager().openAudioConnection(toConnect);
        musicManager.scheduler.queue(track);
    }

    public void skipTrack(TextChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        channel.sendMessage("Skipped to next track.").queue();
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager);
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }
    
    public static void setup(JDA jda) {
        TextChannel channel = BotFiles.CONFIG.getMusicChannel(jda);
        
        BotFiles.CONFIG.getMusicChannel(jda).sendMessage("Setup").setComponents(
                ActionRow.of(Button.primary("music-play", "Play"), Button.danger("music-skip", "Skip"))
        ).queue();
    }
}

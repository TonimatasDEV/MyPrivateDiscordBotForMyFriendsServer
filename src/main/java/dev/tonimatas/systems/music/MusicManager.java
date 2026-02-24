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
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicManager {
    public static String messageId = null;
    private final AudioPlayerManager playerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicManager() {
        this.musicManagers = new HashMap<>();
        this.playerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager source = new YoutubeAudioSourceManager();
        source.useOauth2(BotFiles.CONFIG.youtubeToken, false);
        playerManager.registerSourceManager(source);
    }

    public void loadAndPlay(final TextChannel channel, final VoiceChannel toConnect, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                MessageEmbed embed = Messages.getDefaultEmbed(channel.getJDA(), "Music", "Adding to queue " + track.getInfo().title);
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(5));
                play(channel.getGuild(), toConnect, musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().getFirst();
                }

                MessageEmbed embed = Messages.getDefaultEmbed(channel.getJDA(), "Music", "Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")");
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(5));

                play(channel.getGuild(), toConnect, musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                MessageEmbed embed = Messages.getErrorEmbed(channel.getJDA(), "Nothing found by " + trackUrl);
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(5));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                MessageEmbed embed = Messages.getErrorEmbed(channel.getJDA(), "Could not play: " + exception.getMessage());
                channel.sendMessageEmbeds(embed).queue(Messages.deleteBeforeX(5));
            }
        });
    }

    private void play(Guild guild, VoiceChannel toConnect, GuildMusicManager musicManager, AudioTrack track) {
        guild.getAudioManager().openAudioConnection(toConnect);
        musicManager.scheduler.queue(track);
    }

    public void skipTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.nextTrack();
    }

    public void stopTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.stopQueue();
        guild.getAudioManager().closeAudioConnection();
    }
    
    public void alternateRepeat(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.scheduler.alternateRepeat();
        musicManager.scheduler.updateMusicMessage();
    }
    
    public void pauseTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.pause();
        musicManager.scheduler.updateMusicMessage();
    }

    public void resumeTrack(Guild guild) {
        GuildMusicManager musicManager = getGuildAudioPlayer(guild);
        musicManager.resume();
        musicManager.scheduler.updateMusicMessage();
    }

    private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
        long guildId = Long.parseLong(guild.getId());
        GuildMusicManager musicManager = musicManagers.get(guildId);

        if (musicManager == null) {
            musicManager = new GuildMusicManager(playerManager, guild.getJDA());
            musicManagers.put(guildId, musicManager);
        }

        guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

        return musicManager;
    }
    
    public static void setup(JDA jda) {
        TextChannel channel = BotFiles.CONFIG.getMusicChannel(jda);
        
        List<Message> messages = channel.getHistory().retrievePast(20).complete();
        
        for (Message message : messages) {
            List<MessageEmbed> embeds = message.getEmbeds();

            if (embeds.isEmpty()) {
                message.delete().queue();
            } else {
                messageId = message.getId();
            }
        }
        
        if (messageId == null) {
            MessageEmbed embed = Messages.getDefaultEmbed(jda, "Music", "This is the primary message of the music system. Here you have the controls!\n\nQueue:");

            BotFiles.CONFIG.getMusicChannel(jda).sendMessageEmbeds(embed).setComponents(getButtons(false, false)
            ).queue(message -> messageId = message.getId());
        }
    }
    
    protected static ActionRow getButtons(boolean paused, boolean repeat) {
        return ActionRow.of(Button.success("music-play", "Play"),
                Button.primary("music-skip", "Skip"),
                repeat ? Button.success("music-repeat", "Stop Repeat") : Button.secondary("music-repeat", "Repeat"),
                paused ? Button.success("music-pause", "Resume") : Button.danger("music-pause", "Pause"),
                Button.danger("music-stop", "Stop")
        );
    }
}

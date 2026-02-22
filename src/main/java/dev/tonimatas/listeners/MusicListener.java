package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.music.MusicManager;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class MusicListener extends ListenerAdapter {
    private static final String PLAY_BUTTON = "music-play";
    private static final String URL_INPUT = "music-play-url";
    private static final String MODAL = "music-play-modal";
    private final MusicManager musicManager;

    public MusicListener() {
        this.musicManager = new MusicManager();
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        Button button = event.getButton();
        String buttonId = button.getCustomId();
        Member member = event.getMember();
        
        if (buttonId == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Error! Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String isInAnotherChannel = isInOtherVoiceChannel(member);

        if (isInAnotherChannel != null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), isInAnotherChannel);
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }
        
        if (buttonId.equals(PLAY_BUTTON)) {
            TextInput videoUrl = TextInput.create(URL_INPUT, TextInputStyle.SHORT).build();
            Modal modal = Modal.create(MODAL, "Music Play").addComponents(Label.of("YouTube URL", videoUrl)).build();
            event.replyModal(modal).queue();
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        Member member = event.getMember();
        
        if (event.getCustomId().equals(MODAL)) {
            String isInAnotherChannel = isInOtherVoiceChannel(member);

            if (isInAnotherChannel != null) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), isInAnotherChannel);
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
                return;
            }

            ModalMapping urlMapping = event.getInteraction().getValue(URL_INPUT);

            GuildVoiceState voiceState = isNotNull(member.getVoiceState());
            AudioChannelUnion channel = isNotNull(voiceState.getChannel());
            
            if (urlMapping != null) {
                String url = urlMapping.getAsString();
                musicManager.loadAndPlay(BotFiles.CONFIG.getMusicChannel(event.getJDA()), channel.asVoiceChannel(), url);
                event.deferEdit().queue();
            } else {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Error playing your URL. Check your URL and try again later.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }
        }
    }
    
    @Nullable
    private static String isInOtherVoiceChannel(Member member) {
        if (member == null) return "Error, please try again later.";
        if (!isMemberInVoiceChannel(member)) return "You are not in a voice channel.";
        
        GuildVoiceState botVoiceState = member.getGuild().getSelfMember().getVoiceState();
        
        if (botVoiceState == null || botVoiceState.getChannel() == null) return null;
        
        GuildVoiceState memberVoiceState = isNotNull(member.getVoiceState());
        AudioChannelUnion channel = isNotNull(memberVoiceState.getChannel());
        
        if (!channel.getId().equals(botVoiceState.getChannel().getId())) {
            return "The bot is currently playing music in another channel.";
        }
        
        return null;
    }
    
    public static boolean isMemberInVoiceChannel(@NotNull Member member) {
        GuildVoiceState voiceState = member.getVoiceState();
        
        if (voiceState == null) return false;
        
        return voiceState.inAudioChannel() && isNotNull(voiceState.getChannel()).getType() == ChannelType.VOICE;
    }
    
    @NotNull
    public static <T> T isNotNull(T object) {
        return Objects.requireNonNull(object);
    }
}

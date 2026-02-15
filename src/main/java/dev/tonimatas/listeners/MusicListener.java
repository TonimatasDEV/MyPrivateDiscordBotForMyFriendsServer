package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.music.MusicManager;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.components.label.Label;
import net.dv8tion.jda.api.components.textinput.TextInput;
import net.dv8tion.jda.api.components.textinput.TextInputStyle;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;
import net.dv8tion.jda.api.modals.Modal;
import org.jetbrains.annotations.NotNull;

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
        
        if (buttonId == null || member == null || member.getVoiceState() == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Error! Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue();
            return;
        }
        
        if (!member.getVoiceState().inAudioChannel()) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Are you in a voice channel?");
            event.replyEmbeds(embed).setEphemeral(true).queue();
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
            if (member == null || member.getVoiceState() == null) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Error! Please try again later.");
                event.replyEmbeds(embed).setEphemeral(true).queue();
                return;
            }

            if (!member.getVoiceState().inAudioChannel() || member.getVoiceState().getChannel() == null || member.getVoiceState().getChannel().getType() != ChannelType.VOICE) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Are you in a voice channel?");
                event.replyEmbeds(embed).setEphemeral(true).queue();
                return;
            }

            ModalMapping urlMapping = event.getInteraction().getValue(URL_INPUT);
            
            if (urlMapping != null) {
                String url = urlMapping.getAsString();
                musicManager.loadAndPlay(BotFiles.CONFIG.getMusicChannel(event.getJDA()), member.getVoiceState().getChannel().asVoiceChannel(), url);
                event.deferEdit().queue();
            } else {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Error playing your URL. Check your URL and try again later.");
                event.replyEmbeds(embed).setEphemeral(true).queue();
            }
        }
    }
}

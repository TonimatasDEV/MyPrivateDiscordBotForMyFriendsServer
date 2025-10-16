package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.Set;

public class MusicCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        Member member = interaction.getMember();
        Guild guild = interaction.getGuild();
        MessageEmbed infoError = Messages.getErrorEmbed(interaction.getJDA(), "Error getting your member info. Please try again later.");
        GuildVoiceState voiceState = member == null ? null : member.getVoiceState();

        if (guild == null || voiceState == null) {
            interaction.replyEmbeds(infoError).queue();
            return;
        }

        AudioChannelUnion voice = voiceState.getChannel();

        if (voice == null) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Where are you? I can't join in your channel. Please try again later.");
            interaction.replyEmbeds(embed).queue();
            return;
        }

        VoiceChannel channel = voice.asVoiceChannel();
        AudioManager manager = guild.getAudioManager();

        manager.setSendingHandler(null); // TODO
        manager.openAudioConnection(channel);
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOption(OptionType.STRING, "url", "The YouTube url of the song", true);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "You want music in your channel? This command can do it.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

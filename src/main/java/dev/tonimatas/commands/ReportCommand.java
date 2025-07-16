package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.config.BotConfig;
import dev.tonimatas.listeners.ReportListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.Set;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public class ReportCommand implements SlashCommand {

    @Override
    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userOption = interaction.getOption("message-id");
        if (userOption == null) {
            interaction.reply("You must provide a message id to report.").setEphemeral(true).queue();
            return;
        }

       String messageId = userOption.getAsString();
        TextChannel reportChannel = interaction.getJDA().getTextChannelById(BotConfig.getReportChannelId());
        if (reportChannel == null) {
            interaction.reply("Report channel not found.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Report")
                .setDescription("Message <@" + messageId + "> has been reported.")
                .setColor(Color.RED)
                .setFooter("React with ✅ to confirm or ❌ to dismiss.");

        reportChannel.sendMessageEmbeds(embed.build()).queue(reportMessage -> {
            reportMessage.addReaction(Emoji.fromUnicode("✅")).queue();
            reportMessage.addReaction(Emoji.fromUnicode("❌")).queue();
            ReportListener.trackMessage(reportMessage.getId(), messageId);
        });

        interaction.reply("Report sent to the moderators.").setEphemeral(true).queue();
    }

    @Override
    public SlashCommandData init(SlashCommandData slashCommandData) {
        return slashCommandData.addOption(OptionType.STRING, "message-id", "The message you want to report.", true);
    }

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String getDescription() {
        return "Report a message to the moderators.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}
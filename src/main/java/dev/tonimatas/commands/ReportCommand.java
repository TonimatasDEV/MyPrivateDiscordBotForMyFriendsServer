package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.listeners.ReportListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.awt.*;
import java.util.Set;

import static net.dv8tion.jda.api.interactions.commands.OptionType.USER;

public class ReportCommand implements SlashCommand {
    public static final String REPORT_CHANNEL_ID = "1371081363540938816";

    @Override
    public void execute(SlashCommandInteraction interaction) {
        OptionMapping userOption = interaction.getOption("user");
        if (userOption == null) {
            interaction.reply("You must provide a user to report.").setEphemeral(true).queue();
            return;
        }

        User reportedUser = userOption.getAsUser();
        TextChannel reportChannel = interaction.getJDA().getTextChannelById(REPORT_CHANNEL_ID);
        if (reportChannel == null) {
            interaction.reply("Report channel not found.").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder embed = new EmbedBuilder()
                .setTitle("Report")
                .setDescription("User <@" + reportedUser.getId() + "> has been reported.")
                .setColor(Color.RED)
                .setFooter("React with ✅ to confirm or ❌ to dismiss.");

        reportChannel.sendMessageEmbeds(embed.build()).queue(reportMessage -> {
            reportMessage.addReaction(Emoji.fromUnicode("✅")).queue();
            reportMessage.addReaction(Emoji.fromUnicode("❌")).queue();
            ReportListener.trackMessage(reportMessage.getId());
        });

        interaction.reply("Report sent to the moderators.").setEphemeral(true).queue();
    }

    @Override
    public SlashCommandData init(SlashCommandData slashCommandData) {
        return slashCommandData.addOption(USER, "user", "The user you want to report.", true);
    }

    @Override
    public String getName() {
        return "report";
    }

    @Override
    public String getDescription() {
        return "Report a user to the moderators.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}
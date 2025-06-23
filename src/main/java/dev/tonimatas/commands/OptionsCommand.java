package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.List;

public class OptionsCommand implements SlashCommand {

    @Override
    public void execute(SlashCommandInteraction interaction) {
        OptionMapping dailyNotifyOption = interaction.getOption("daily_notify");

        if (dailyNotifyOption != null) {
            boolean dailyNotify = dailyNotifyOption.getAsBoolean();

            String userId = interaction.getUser().getId();

            BotFiles.SETTINGS.getSettings(userId).setNotifyDaily(dailyNotify);
            BotFiles.BANK.getDaily(userId).setNotified(false);

            String description = "Daily notifier " + (dailyNotify ? "enabled." : "disabled.");
            MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Settings changed", description);
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }

    @Override
    public String getCommandName() {
        return "options";
    }

    @Override
    public String getDescription() {
        return "Configure your preferences";
    }

    @Override
    public List<InteractionContextType> getContexts() {
        return InteractionContextType.ALL.stream().toList();
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOption(OptionType.BOOLEAN, "daily_notify", "Do you prefer if the bot remembers when your daily reward is up?", true);
    }
}

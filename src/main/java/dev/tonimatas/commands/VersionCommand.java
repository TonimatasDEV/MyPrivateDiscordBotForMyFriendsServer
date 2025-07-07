package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.util.JarUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Set;

public class VersionCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction slashCommandInteraction) {
        MessageEmbed embed = Messages.getDefaultEmbed(slashCommandInteraction.getJDA(), "Version", JarUtils.getJarName());
        slashCommandInteraction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescription() {
        return "Shows the version of the bot.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return InteractionContextType.ALL;
    }
}

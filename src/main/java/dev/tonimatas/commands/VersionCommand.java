package dev.tonimatas.commands;

import dev.tonimatas.util.JarUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;

public class VersionCommand {
    @Command("version")
    @Description("Shows the version of the bot.")
    public void execute(SlashCommandActor actor) {
        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Version", JarUtils.getJarName());
        actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
    }
}

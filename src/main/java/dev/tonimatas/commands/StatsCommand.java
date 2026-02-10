package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

public class StatsCommand {
    @Command("stats")
    @Description("Show user statistics.")
    @GuildOnly
    public void execute(SlashCommandActor actor, @Description("The user that you want to check their stats.") User user) {
        if (CommandUtils.isNotCommandsChannel(actor.commandEvent())) return;

        if (user.isBot()) {
            MessageCreateData embed = Messages.getErrorEmbed_Lamp(actor.jda(), "Bots don't take part on any statistics.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        UserInfo userInfo = BotFiles.USER.get(user.getId());
        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Stats",
                "**" + user.getEffectiveName() + ":**" + "\n" +
                        "Times counted correctly: " + userInfo.getStats().getCountCorrectly() + ".\n" +
                        "Times counted incorrectly: " + userInfo.getStats().getCountIncorrectly() + ".\n" +
                        "Money won: " + userInfo.getStats().getMoneyWon() + "€.\n" +
                        "Money spent: " + userInfo.getStats().getMoneySpent() + "€.\n" +
                        "Messages sent: " + userInfo.getStats().getMessagesSent() + ".\n" +
                        "Commands Executed: " + userInfo.getStats().getCommandsExecuted() + "."
        );

        actor.replyToInteraction(embed).queue();
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

public class MoneyCommand {
    @Command("money")
    @Description("The user that you want to check their amount of money.")
    @GuildOnly
    public void execute(SlashCommandActor actor, @Named("user") @Description("The user that you want to check their amount of money.") @Optional User user) {
        if (CommandUtils.isNotCommandsChannel(actor)) return;

        if (user == null) {
            user = actor.user();
        }

        if (user.isBot()) {
            MessageEmbed embed = Messages.getErrorEmbed(actor.jda(), "Bots cannot storage money.");
            actor.replyToInteraction(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        long money = BotFiles.USER.get(user.getId()).getMoney();
        MessageEmbed embed = Messages.getDefaultEmbed(actor.jda(), "Money", user.getEffectiveName() + " has " + money + "€.");
        actor.replyToInteraction(embed).queue();
    }
}

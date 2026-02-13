package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.ToLongFunction;

public class StatsTopCommand {
    public static String getLongStatTop(String name, JDA jda, ToLongFunction<? super UserStats> value) {
        List<Map.Entry<String, UserStats>> entries = BotFiles.USER.getUserStatsEntries();
        entries.sort(Comparator.comparingLong(e -> value.applyAsLong(e.getValue())));

        StringBuilder result = new StringBuilder();

        result.append("### ").append(name).append(":\n");

        for (int i = 0; i < 3; i++) {
            if (entries.size() <= i) break;

            Map.Entry<String, UserStats> entry = entries.reversed().get(i);
            User user = jda.getUserById(entry.getKey());
            
            if (user != null && user.isBot()) continue;
   
            String userName = user == null ? "Unknown" : user.getEffectiveName();
            result.append(" - ").append(i + 1).append(". ").append(userName).append(": ").append(value.applyAsLong(entry.getValue())).append("\n");
        }

        return result.toString();
    }

    @Command("stats-top")
    @Description("Shows the statistics top.")
    @GuildOnly
    public void execute(SlashCommandActor actor) {
        if (CommandUtils.isNotCommandsChannel(actor)) return;

        JDA jda = actor.jda();

        String result = getLongStatTop("Times counted correctly", jda, UserStats::getCountCorrectly) +
                getLongStatTop("Times counted incorrectly", jda, UserStats::getCountIncorrectly) +
                getLongStatTop("Money won", jda, UserStats::getMoneyWon) +
                getLongStatTop("Money spent", jda, UserStats::getMoneySpent) +
                getLongStatTop("Messages sent", jda, UserStats::getMessagesSent) +
                getLongStatTop("Commands Executed", jda, UserStats::getCommandsExecuted);

        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(jda, "Statistics Top", result);
        actor.replyToInteraction(embed).queue();
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToLongFunction;

public class StatsTopCommand implements SlashCommand {
    public static String getLongStatTop(String name, JDA jda, ToLongFunction<? super UserStats> value) {
        return getLongStatTop(name, jda, value, null);
    }
    
    
    public static String getLongStatTop(String name, JDA jda, ToLongFunction<? super UserStats> value, Function<? super UserStats, String> getter) {
        List<Map.Entry<String, UserStats>> entries = BotFiles.USER.getUserStatsEntries();
        entries.sort(Comparator.comparingLong(e -> value.applyAsLong(e.getValue())));

        StringBuilder result = new StringBuilder();

        result.append("### ").append(name).append(":\n");

        for (int i = 0; i < 3; i++) {
            if (entries.size() <= i) break;

            Map.Entry<String, UserStats> entry = entries.reversed().get(i);
            User user = jda.getUserById(entry.getKey());
            String userName = user == null ? "Unknown" : user.getEffectiveName();

            result.append(" - ").append(i + 1).append(". ").append(userName).append(": ");
            if (getter == null) {
                result.append(value.applyAsLong(entry.getValue()));
            } else {
                result.append(getter.apply(entry.getValue()));
            }

            result.append("\n");
        }

        return result.toString();
    }

    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        String result = getLongStatTop("Times counted correctly", interaction.getJDA(), UserStats::getCountCorrectly) +
                getLongStatTop("Times counted incorrectly", interaction.getJDA(), UserStats::getCountIncorrectly) +
                getLongStatTop("Money won", interaction.getJDA(), UserStats::getMoneyWon) +
                getLongStatTop("Money spent", interaction.getJDA(), UserStats::getMoneySpent) +
                getLongStatTop("Messages sent", interaction.getJDA(), UserStats::getMessagesSent) +
                getLongStatTop("Commands Executed", interaction.getJDA(), UserStats::getCommandsExecuted) +
                getLongStatTop("Time in voice channels", interaction.getJDA(), UserStats::getTimeInVoiceLong, stats -> 
                        TimeUtils.formatDuration(stats.getTimeInVoice()));

        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Statistics Top", result);
        interaction.replyEmbeds(embed).queue();
    }

    @Override
    public String getName() {
        return "stats-top";
    }

    @Override
    public String getDescription() {
        return "Shows the statistics top.";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return Set.of(InteractionContextType.GUILD);
    }
}

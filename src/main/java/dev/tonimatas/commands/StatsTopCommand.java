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

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class StatsTopCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        if (CommandUtils.isNotCommandsChannel(interaction)) return;

        String result = getLongStatTop("Times counted correctly", interaction.getJDA(), UserStats::getCountCorrectly) +
                getLongStatTop("Times counted incorrectly", interaction.getJDA(), UserStats::getCountIncorrectly) +
                getLongStatTop("Money won", interaction.getJDA(), UserStats::getMoneyWon) +
                getLongStatTop("Money spent", interaction.getJDA(), UserStats::getMoneySpent) +
                getLongStatTop("Total transactions", interaction.getJDA(), UserStats::getTransactions) +
                getLongStatTop("Messages sent", interaction.getJDA(), UserStats::getMessagesSent) +
                getLongStatTop("Commands Executed", interaction.getJDA(), UserStats::getCommandsExecuted) +
                getDurationTop(interaction.getJDA());

        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Statistics Top", result);
        interaction.replyEmbeds(embed).queue();
    }
    
    public static String getLongStatTop(String name, JDA jda, Function<? super UserStats, Long> value) {
        Map<String, UserStats> users = BotFiles.USER.getUsers()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStats()));
        
        List<Map.Entry<String, UserStats>> entries = new ArrayList<>(users.entrySet());
        entries.sort(Comparator.comparingLong(e -> value.apply(e.getValue())));

        StringBuilder result = new StringBuilder();

        result.append("### ").append(name).append(":\n");

        for (int i = 0; i < 3; i++) {
            if (entries.size() <= i) break;

            Map.Entry<String, UserStats> entry = entries.reversed().get(i);
            User user = jda.getUserById(entry.getKey());
            String userName = user == null ? "Unknown" : user.getEffectiveName();

            result.append(" - ").append(i + 1).append(". ").append(userName).append(": ").append(value.apply(entry.getValue())).append("\n");
        }

        return result.toString();
    }
    
    public static String getDurationTop(JDA jda) {
        Map<String, UserStats> users = BotFiles.USER.getUsers()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStats()));

        List<Map.Entry<String, UserStats>> entries = new ArrayList<>(users.entrySet());
        entries.sort((e1, e2) -> e2.getValue().getTimeInVoice().compareTo(e1.getValue().getTimeInVoice()));
        
        StringBuilder result = new StringBuilder();
        result.append("### Time in voice channels:").append(":\n");

        for (int i = 0; i < 3; i++) {
            if (entries.size() <= i) break;

            Map.Entry<String, UserStats> entry = entries.get(i);
            User user = jda.getUserById(entry.getKey());
            String userName = user == null ? "Unknown" : user.getEffectiveName();

            result.append(" - ")
                    .append(i + 1)
                    .append(". ")
                    .append(userName).append(": ")
                    .append(TimeUtils.formatDuration(entry.getValue().getTimeInVoice()))
                    .append("\n");
        }
        
        return result.toString();
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

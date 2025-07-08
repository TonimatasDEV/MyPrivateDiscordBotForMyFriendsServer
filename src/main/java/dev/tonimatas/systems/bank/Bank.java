package dev.tonimatas.systems.bank;

import dev.tonimatas.api.data.UserInfo;
import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Bank {
    private Bank() {
        // We don't need a constructor
    }

    public static String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, UserInfo>> sortedList = BotFiles.USER.getUsers().entrySet()
                .stream()
                .sorted((a, b) -> Long.compare(b.getValue().getMoney(), a.getValue().getMoney()))
                .toList();

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            String name = "None";
            long money = 0;

            if (sortedList.size() > i) {
                Member user = guild.getMemberById(sortedList.get(i).getKey());

                name = (user != null) ? user.getEffectiveName() : "Unknown";
                money = sortedList.get(i).getValue().getMoney();
            }

            text.append(i).append(". ").append(name).append(" - ").append(money).append("€\n");
        }

        return text.toString();
    }

    public static String getTransactionsString(User user) {
        List<Transaction> transactions = BotFiles.USER.get(user.getId()).getTransactions();

        StringBuilder text = new StringBuilder();

        text.append("**").append(user.getEffectiveName()).append(":**\n");

        int i = 1;
        for (Transaction transaction : transactions) {
            text.append(String.format("%d. ", i++))
                    .append(transaction.getTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                    .append("  |  ")
                    .append(transaction.getReason())
                    .append("  |  ")
                    .append(transaction.getAmount())
                    .append("€\n");
        }

        return text.toString();
    }
}

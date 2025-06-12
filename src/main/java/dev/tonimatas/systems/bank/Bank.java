package dev.tonimatas.systems.bank;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.List;
import java.util.Map;

public class Bank {
    public static String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, Long>> sortedList = BotFiles.BANK.bank.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .toList();

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < 10; i++) {
            String name = "None";
            long money = 0;

            if (sortedList.size() > i) {
                Member user = guild.getMemberById(sortedList.get(i).getKey());

                name = (user != null) ? user.getEffectiveName() : "Unknown";
                money = sortedList.get(i).getValue();
            }

            text.append(i).append(". ").append(name).append(" - ").append(money).append("€\n");
        }

        return text.toString();
    }
}

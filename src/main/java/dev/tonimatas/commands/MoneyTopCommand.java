package dev.tonimatas.commands;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.CommandUtils;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;
import revxrsal.commands.jda.annotation.GuildOnly;

import java.util.List;
import java.util.Map;

public class MoneyTopCommand {
    private static String getMoneyTopString(Guild guild) {
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
                
                if (user != null && user.getUser().isBot()) continue;

                name = (user != null) ? user.getEffectiveName() : "Unknown";
                money = sortedList.get(i).getValue().getMoney();
            }

            text.append(i).append(". ").append(name).append(" - ").append(money).append("€\n");
        }

        return text.toString();
    }

    @Command("money-top")
    @Description("The top 10 richest people among us!")
    @GuildOnly
    public void execute(SlashCommandActor actor) {
        if (CommandUtils.isNotCommandsChannel(actor)) return;

        Guild guild = actor.guild();

        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Money Top", getMoneyTopString(guild));
        actor.replyToInteraction(embed).queue();
    }
}

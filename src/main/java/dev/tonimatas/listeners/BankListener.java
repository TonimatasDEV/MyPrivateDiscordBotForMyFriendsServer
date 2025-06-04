package dev.tonimatas.listeners;

import dev.tonimatas.config.BankData;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Map;

public class BankListener extends ListenerAdapter {
    private final BankData bankData;

    public BankListener(BankData bankData) {
        this.bankData = bankData;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member == null || guild == null) {
            event.reply("Internal error. Please try again later.").setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }

        String command = event.getFullCommandName();
        
        if (command.equalsIgnoreCase("money")) {
            long money = bankData.getMoney(member.getId());
            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money", "You have " + money + "€.");
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        } else if (command.equalsIgnoreCase("money-top")) {
            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money Top", getMoneyTopString(guild));
            event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
        }
    }

    private String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, Long>> sortedList = bankData.bank.entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .toList();

        StringBuilder text = new StringBuilder();

        for (int i = 0; i < 5; i++) {
            String name = "None";
            long money = 0;
            
            if (sortedList.size() > i) {
                Member member = guild.getMemberById(sortedList.get(i).getKey());
                name = (member != null) ? member.getEffectiveName() : "Unknown";
                money = sortedList.get(i).getValue();
            }

            text.append(i).append(". ").append(name).append(" - ").append(money).append("€\n");
        }

        return text.toString();
    }

}

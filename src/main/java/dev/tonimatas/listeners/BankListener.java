package dev.tonimatas.listeners;

import dev.tonimatas.config.BankData;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class BankListener extends ListenerAdapter {
    private static final String COMMANDS_CHANNEL = "1380277341405581443";
    private final BankData bankData;

    public BankListener(BankData bankData) {
        this.bankData = bankData;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member == null || guild == null) {
            MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "Internal error. Please try again later.");
            event.replyEmbeds(embed).setEphemeral(true).queue();
            return;
        }

        String command = event.getFullCommandName();

        if (command.equalsIgnoreCase("money")) {
            if (!event.getChannel().getId().equals(COMMANDS_CHANNEL)) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Commands channel.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }

            long money = bankData.getMoney(member.getId());
            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money", "You have " + money + "€.");
            event.replyEmbeds(embed).queue();
        } else if (command.equalsIgnoreCase("money-top")) {
            if (!event.getChannel().getId().equals(COMMANDS_CHANNEL)) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Commands channel.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }

            MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Money Top", getMoneyTopString(guild));
            event.replyEmbeds(embed).queue();
        } else if (command.equalsIgnoreCase("daily")) {
            if (!event.getChannel().getId().equals(COMMANDS_CHANNEL)) {
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "This command can only be run in the Commands channel.");
                event.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            }

            LocalDateTime now = LocalDateTime.now();
            LocalDateTime claimedTime = bankData.getDaily(member.getId());

            if (now.isAfter(claimedTime.plusHours(24))) {
                bankData.addMoney(member.getId(), 100);
                MessageEmbed embed = Messages.getDefaultEmbed(event.getJDA(), "Daily", "Yeah! You claimed 100€.");
                event.replyEmbeds(embed).queue();
                bankData.setDaily(member.getId(), now);
            } else {
                String formattedDate = bankData.getNextFormattedDaily(member.getId());
                MessageEmbed embed = Messages.getErrorEmbed(event.getJDA(), "You need to wait more. Your next daily will be available at " + formattedDate);
                event.replyEmbeds(embed).queue();
            }
        }
    }

    private String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, Long>> sortedList = bankData.bank.entrySet()
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

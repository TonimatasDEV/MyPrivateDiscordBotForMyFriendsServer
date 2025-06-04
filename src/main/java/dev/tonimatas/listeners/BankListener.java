package dev.tonimatas.listeners;

import dev.tonimatas.tasks.RouletteTask;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Map;

public class BankListener extends ListenerAdapter {
    private final RouletteTask rouletteTask;

    public BankListener(RouletteTask rouletteTask) {
        this.rouletteTask = rouletteTask;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        Guild guild = event.getGuild();

        if (member == null || guild == null) {
            event.reply("Internal error. Please try again later.").setEphemeral(true).queue(Messages.deleteBeforeX());
            return;
        }

        switch (event.getFullCommandName()) {
            case "money" -> {
                long money = rouletteTask.getMoney(member.getId());
                event.reply("You have " + money + "€.").queue(Messages.deleteBeforeX());
            }

            case "money-top" -> {
                MessageEmbed embed = new EmbedBuilder()
                        .setTitle("Money Top")
                        .setDescription(getMoneyTopString(guild))
                        .build();

                event.replyEmbeds(embed).queue(Messages.deleteBeforeX());
            }
        }
    }

    // TODO: Don't print empty string. 1. Unknown \n 2. Unknown...
    private String getMoneyTopString(Guild guild) {
        List<Map.Entry<String, Long>> sortedList = rouletteTask.getBank().entrySet()
                .stream()
                .sorted((a, b) ->
                        b.getValue().compareTo(a.getValue()))
                .toList();

        int counter = 0;
        StringBuilder text = new StringBuilder();

        for (Map.Entry<String, Long> entry : sortedList) {
            if (counter >= 5 || entry == null) break;

            Member member = guild.getMemberById(entry.getKey());

            String name = (member != null) ? member.getEffectiveName() : "Unknown";

            text.append((counter + 1))
                    .append(". ")
                    .append(name)
                    .append(" ")
                    .append(entry.getValue())
                    .append("€\n");

            counter++;
        }

        return text.toString();
    }

}

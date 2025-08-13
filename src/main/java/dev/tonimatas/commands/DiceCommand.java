package dev.tonimatas.commands;

import dev.tonimatas.cjda.slash.SlashCommand;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.security.SecureRandom;
import java.util.Set;

public class DiceCommand implements SlashCommand {
    @Override
    public void execute(SlashCommandInteraction interaction) {
        SecureRandom random = new SecureRandom();
        
        OptionMapping min = interaction.getOption("min");
        OptionMapping max = interaction.getOption("max");
        
        if (min == null || max == null) {
            MessageEmbed embed = Messages.getErrorEmbed(interaction.getJDA(), "Incorrect arguments");
            interaction.replyEmbeds(embed).setEphemeral(true).queue(Messages.deleteBeforeX(10));
            return;
        }
        
        int result = random.nextInt(min.getAsInt(), max.getAsInt() + 1);
        MessageEmbed embed = Messages.getDefaultEmbed(interaction.getJDA(), "Dice", "The number is " + result + ".");
        interaction.replyEmbeds(embed).queue();
    }

    @Override
    public SlashCommandData init(SlashCommandData data) {
        return data.addOption(OptionType.INTEGER, "min", "Minimum number")
                .addOption(OptionType.INTEGER, "max", "Maximum number");
    }

    @Override
    public String getName() {
        return "dice";
    }

    @Override
    public String getDescription() {
        return "Get a random number!";
    }

    @Override
    public Set<InteractionContextType> getContexts() {
        return InteractionContextType.ALL;
    }

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < 100; i++) {
            System.out.println(random.nextInt(1, 10 + 1));
        }
    }
}

package dev.tonimatas.commands;

import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.jda.actor.SlashCommandActor;

import java.security.SecureRandom;

public class DiceCommand {
    private static final SecureRandom RANDOM = new SecureRandom();

    @Command("dice")
    @Description("Get a random number!")
    public void execute(SlashCommandActor actor, @Description("Minimum number") Integer min, @Description("Maximum number") Integer max) {
        int result = RANDOM.nextInt(min, max + 1);
        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Dice", "The number is " + result + ".");
        actor.replyToInteraction(embed).queue();
    }
}

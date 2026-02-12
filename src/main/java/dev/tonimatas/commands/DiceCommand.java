package dev.tonimatas.commands;

import dev.tonimatas.util.Messages;
import dev.tonimatas.util.Utils;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.jda.actor.SlashCommandActor;

public class DiceCommand {

    @Command("dice")
    @Description("Get a random number!")
    public void execute(SlashCommandActor actor, @Named("min") @Description("Minimum number") Integer min, @Named("max") @Description("Maximum number") Integer max) {
        int result = Utils.RANDOM.nextInt(min, max + 1);
        MessageCreateData embed = Messages.getDefaultEmbed_Lamp(actor.jda(), "Dice", "The number is " + result + ".");
        actor.replyToInteraction(embed).queue();
    }
}

package dev.tonimatas.tasks;

import dev.tonimatas.config.Configs;
import dev.tonimatas.utils.Getters;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;

public class CountTask {
    private long currentNumber;

    public CountTask() {
        currentNumber = Configs.BOT.getValue("count").toLong();
    }
    
    public void checkNewNumber(String channelId, Message message) {
        if (message.getAuthor().isBot()) return;
        
        TextChannel channel = Getters.getCountChannel();
        
        if (!channelId.equals(channel.getId())) {
            return;
        }

        long numberFromMessage = getNumber(message);

        if (numberFromMessage == currentNumber + 1) {
            message.addReaction(Emoji.fromUnicode("✅")).queue();
            currentNumber++;
            Configs.BOT.setValue("count", currentNumber);
            return;
        }

        message.addReaction(Emoji.fromUnicode("❌")).queue();
        message.reply("Incorrecto. El siguiente número era: " + (currentNumber + 1) + ". Empezamos de nuevo por tu culpa.").queue();
        currentNumber = 0;
    }
    
    private long getNumber(Message message) {
        try {
            return Long.parseLong(message.getContentRaw());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}

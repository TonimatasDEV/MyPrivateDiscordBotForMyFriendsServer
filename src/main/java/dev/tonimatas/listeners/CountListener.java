package dev.tonimatas.listeners;

import dev.tonimatas.config.BotConfig;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CountListener extends ListenerAdapter {
    private static final String COUNT_CHANNEL_ID = "1371077663812222976";
    private final BotConfig config;

    public CountListener(BotConfig config) {
        this.config = config;
    }
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelId = event.getChannel().getId();
        
        if (message.getAuthor().isBot()) return;


        if (!channelId.equals(COUNT_CHANNEL_ID)) {
            return;
        }

        long numberFromMessage = getNumber(message);
        long currentNumber = config.count;
        
        if (numberFromMessage == currentNumber + 1) {
            message.addReaction(Emoji.fromUnicode("✅")).queue();
            currentNumber++;
            config.count = currentNumber;
            config.save();
            return;
        }

        message.addReaction(Emoji.fromUnicode("❌")).queue();
        message.reply("Incorrecto. El siguiente número era: " + (currentNumber + 1) + ". Empezamos de nuevo por tu culpa.").queue();
        config.count = 0;
        config.save();
    }
    
    private long getNumber(Message message) {
        try {
            return Long.parseLong(message.getContentRaw());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}

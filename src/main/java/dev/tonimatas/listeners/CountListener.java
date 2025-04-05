package dev.tonimatas.listeners;

import dev.tonimatas.Config;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CountListener extends ListenerAdapter {
    private static long currentNumber = 0;
    
    public static void init() {
        currentNumber = Config.getCount();
    }
    
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Channel channel = event.getChannel();

        if (event.getAuthor().isBot()) return;
        
        giveTextXP(event);

        if (channel.getId().equals("1344403479501996032")) {
            try {
                long messageNumber = Long.parseLong(event.getMessage().getContentRaw());
                
                if (messageNumber == currentNumber + 1) {
                    event.getMessage().addReaction(Emoji.fromUnicode("✅")).queue();
                    currentNumber++;
                    Config.setCount(currentNumber);
                    return;
                }
            } catch (NumberFormatException ignored) {
            }

            event.getMessage().addReaction(Emoji.fromUnicode("❌")).queue();
            event.getMessage().reply("Incorrecto. El siguiente número era: " + (currentNumber + 1) + ". Empezamos de nuevo por tu culpa.").queue();
            currentNumber = 0;
        }
    }
    
    private static void giveTextXP(MessageReceivedEvent event) {
        // TODO
    }
}

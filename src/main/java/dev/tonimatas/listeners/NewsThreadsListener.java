package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class NewsThreadsListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(BotFiles.CONFIG.getNewsChannelId())) {
            JDA jda = event.getJDA();
            
            event.getMessage().createThreadChannel("Comments").queue(thread -> {
                MessageEmbed embed = Messages.getDefaultEmbed(jda, "News Comments", "Here we can discuss things about the news.");
                thread.sendMessageEmbeds(embed).queue();
            });
            
            addReactions(event.getMessage());
        } else if (event.getChannel().getId().equals(BotFiles.CONFIG.getAnnouncementsChannelId())) {
            addReactions(event.getMessage());
        }
    }
    
    private static void addReactions(Message message) {
        message.addReaction(Emoji.fromUnicode("✅")).queue();
        message.addReaction(Emoji.fromUnicode("❌")).queue();
    }
}

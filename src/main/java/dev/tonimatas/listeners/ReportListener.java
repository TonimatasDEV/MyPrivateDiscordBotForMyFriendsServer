package dev.tonimatas.listeners;

import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReportListener extends ListenerAdapter {
    private static final String TONIMATAS_DEV_ID = "590555528112111616";

    private static final Map<String, Set<String>> yesVotes = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> noVotes = new ConcurrentHashMap<>();

    public static void trackMessage(String messageId) {
        yesVotes.putIfAbsent(messageId, new HashSet<>());
        noVotes.putIfAbsent(messageId, new HashSet<>());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;

        String messageId = event.getMessageId();
        String userId = event.getUserId();

        if (!yesVotes.containsKey(messageId) && !noVotes.containsKey(messageId)) return;

        String emoji = event.getReaction().getEmoji().getName();

        if (emoji.equals("✅")) {
            yesVotes.get(messageId).add(userId);
        } else if (emoji.equals("❌")) {
            noVotes.get(messageId).add(userId);
        }

        Set<String> yes = yesVotes.getOrDefault(messageId, Set.of());
        Set<String> no = noVotes.getOrDefault(messageId, Set.of());

        if (yes.contains(TONIMATAS_DEV_ID) && yes.size() >= 3) {
            event.getChannel().deleteMessageById(messageId).queue();
            yesVotes.remove(messageId);
            noVotes.remove(messageId);
            return;
        }

        if (no.contains(TONIMATAS_DEV_ID) || no.size() >= 4) {
            event.getChannel().deleteMessageById(messageId).queue();
            yesVotes.remove(messageId);
            noVotes.remove(messageId);
        }
    }
}
package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ReportListener extends ListenerAdapter {
    private static final String TONIMATAS_DEV_ID = "590555528112111616";

    private static final Map<String, Set<String>> yesVotes = new ConcurrentHashMap<>();
    private static final Map<String, Set<String>> noVotes = new ConcurrentHashMap<>();

    public static void trackMessage(String reportMessageId, String originalMessageId) {
        BotFiles.EXTRA.getReportToOriginalMap().put(reportMessageId, originalMessageId);
        yesVotes.put(reportMessageId, new HashSet<>());
        noVotes.put(reportMessageId, new HashSet<>());
    }

    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent event) {
        if (event.getUser().isBot()) return;

        String reportMessageId = event.getMessageId();
        String userId = event.getUserId();

        if (!yesVotes.containsKey(reportMessageId)) return;

        String emoji = event.getReaction().getEmoji().getName();

        if (emoji.equals("✅")) {
            yesVotes.get(reportMessageId).add(userId);
        } else if (emoji.equals("❌")) {
            noVotes.get(reportMessageId).add(userId);
        } else {
            return;
        }

        Set<String> yes = yesVotes.get(reportMessageId);
        Set<String> no = noVotes.get(reportMessageId);
        String originalMessageId = BotFiles.EXTRA.getReportToOriginalMap().get(reportMessageId);

        if (yes.contains(TONIMATAS_DEV_ID) && yes.size() >= 3) {
            event.getChannel().deleteMessageById(originalMessageId).queue();
            event.getChannel().deleteMessageById(reportMessageId).queue();
            cleanup(reportMessageId);
            return;
        }

        if (no.contains(TONIMATAS_DEV_ID) || no.size() >= 4) {
            event.getChannel().deleteMessageById(reportMessageId).queue();
            cleanup(reportMessageId);
        }
    }

    private void cleanup(String reportMessageId) {
        BotFiles.EXTRA.getReportToOriginalMap().remove(reportMessageId);
        yesVotes.remove(reportMessageId);
        noVotes.remove(reportMessageId);
    }
}
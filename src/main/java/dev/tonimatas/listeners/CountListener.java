package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CountListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message message = event.getMessage();
        String channelId = event.getChannel().getId();

        if (message.getAuthor().isBot()) return;

        if (!channelId.equals(BotFiles.CONFIG.getCountingChannelId())) {
            return;
        }

        long numberFromMessage = getNumber(message);
        long currentNumber = BotFiles.EXTRA.getCount();
        User user = event.getAuthor();

        if (numberFromMessage == currentNumber + 1) {
            message.addReaction(Emoji.fromUnicode("✅")).queue();
            BotFiles.BANK.addMoney(user.getId(), 1, "Counted correctly.");
            currentNumber++;
            BotFiles.EXTRA.setCount(currentNumber);
            return;
        }

        message.addReaction(Emoji.fromUnicode("❌")).queue();
        message.reply("Incorrecto. El siguiente número era: " + (currentNumber + 1) + ". Empezamos de nuevo por tu culpa.").queue();
        BotFiles.BANK.removeMoney(user.getId(), Math.min(BotFiles.BANK.getMoney(user.getId()), 50), "Counted incorrectly");
        BotFiles.EXTRA.setCount(0);
    }

    private long getNumber(Message message) {
        try {
            return Long.parseLong(message.getContentRaw());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}

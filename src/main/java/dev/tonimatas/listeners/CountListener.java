package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.GuildReadyEvent;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class CountListener extends ListenerAdapter {
    private User lastCountUser = null;
    private String lastCountMessageId = null;

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
            BotFiles.USER.get(user.getId()).getStats().increaseCountCorrectly();
            BotFiles.USER.get(user.getId()).addMoney(1);
            currentNumber++;
            BotFiles.EXTRA.setCount(currentNumber);
            lastCountUser = user;
            lastCountMessageId = event.getMessageId();
            return;
        }

        message.addReaction(Emoji.fromUnicode("❌")).queue();
        message.reply("Incorrecto. El siguiente número era: " + (currentNumber + 1) + ". Empezamos de nuevo por tu culpa y perdiste 50€.").queue();
        BotFiles.USER.get(user.getId()).getStats().increaseCountIncorrectly();
        BotFiles.USER.get(user.getId()).removeMoney(50);
        BotFiles.EXTRA.setCount(0);
    }

    @Override
    public void onMessageDelete(MessageDeleteEvent event) {
        executeTrollAction(event.getChannel(), lastCountUser, event.getMessageId());
    }

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        executeTrollAction(event.getChannel(), event.getAuthor(), event.getMessageId());
    }

    private void executeTrollAction(MessageChannelUnion channel, User author, String messageId) {
        String channelId = channel.getId();

        if (!channelId.equals(BotFiles.CONFIG.getCountingChannelId()) || lastCountMessageId == null) {
            return;
        }

        if (messageId.equals(lastCountMessageId)) {
            String currentNumber = String.valueOf(BotFiles.EXTRA.getCount() + 1);
            BotFiles.USER.get(lastCountUser.getId()).getStats().increaseCountIncorrectly();
            BotFiles.USER.get(lastCountUser.getId()).removeMoney(100);
            channel.sendMessage(author.getAsMention() + " ha intentado hacer que contemos mal, ahora tiene 100€ menos. \nEl siguiente número es: " + currentNumber).queue(message ->
                    message.addReaction(Emoji.fromUnicode("✅")).queue());
        }
    }

    @Override
    public void onGuildReady(GuildReadyEvent event) {
        TextChannel channel = event.getJDA().getTextChannelById(BotFiles.CONFIG.getCountingChannelId());

        if (channel != null) {
            lastCountMessageId = channel.getLatestMessageId();
            if (lastCountMessageId.equals("0")) return;
            channel.retrieveMessageById(lastCountMessageId).queue((message -> lastCountUser = message.getAuthor()));
        }
    }

    private long getNumber(Message message) {
        try {
            return Long.parseLong(message.getContentRaw());
        } catch (NumberFormatException ignored) {
            return -1;
        }
    }
}

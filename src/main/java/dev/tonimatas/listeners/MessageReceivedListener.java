package dev.tonimatas.listeners;

import com.openai.models.*;
import dev.tonimatas.Main;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.lang.Thread;
import java.util.List;

public class MessageReceivedListener extends ListenerAdapter {
    private long lastRequestTime = 0;
    private static final long REQUEST_INTERVAL_MS = 2000;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String message = event.getMessage().getContentRaw();

        if (event.getAuthor().isBot()) {
            return;
        }

        if (event.getChannel().getId().equals("1276354816880279617")) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastRequestTime < REQUEST_INTERVAL_MS) {
                long waitTime = REQUEST_INTERVAL_MS - (currentTime - lastRequestTime);
                try {
                    Thread.sleep(waitTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String botResponse = getOpenAIResponse(message);

            event.getChannel().sendMessage(botResponse).queue();
            lastRequestTime = System.currentTimeMillis();
        }
    }

    private String getOpenAIResponse(String userMessage) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .messages(List.of(ChatCompletionMessageParam.ofChatCompletionUserMessageParam(ChatCompletionUserMessageParam.builder()
                        .role(ChatCompletionUserMessageParam.Role.USER)
                        .content(ChatCompletionUserMessageParam.Content.ofTextContent(userMessage))
                        .build())))
                .model(ChatModel.GPT_4O_MINI)
                .build();

        ChatCompletion chatCompletion = Main.client.chat().completions().create(params);

        return chatCompletion.choices().getFirst().message().content().orElse("No entendi absolutamente nada.");
    }
}

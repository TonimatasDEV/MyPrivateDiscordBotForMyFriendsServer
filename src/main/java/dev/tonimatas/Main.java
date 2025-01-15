package dev.tonimatas;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import dev.tonimatas.listeners.MessageReceivedListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static OpenAIClient client = OpenAIOkHttpClient.builder()
            .apiKey(Config.getOpenAIApiKey())
            .build();;
    
    public static void main(String[] args) {
        String token = Config.getToken();
        
        if (!token.isEmpty() && !Config.getOpenAIApiKey().isEmpty()) {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.enableIntents(Arrays.stream(GatewayIntent.values()).toList());
            jdaBuilder.addEventListeners(new MessageReceivedListener());
            jdaBuilder.setAutoReconnect(true);
            JDA jda = jdaBuilder.build();
        }
    }
}
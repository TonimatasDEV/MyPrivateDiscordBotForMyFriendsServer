package dev.tonimatas;

import dev.tonimatas.listeners.MessageReceivedListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;

public class Main {
    public static JDA JDA;
    
    public static void main(String[] args) {
        String token = Config.getToken();
        
        if (!token.isEmpty()) {
            JDABuilder jdaBuilder = JDABuilder.createDefault(token);
            jdaBuilder.enableIntents(Arrays.stream(GatewayIntent.values()).toList());
            jdaBuilder.addEventListeners(new MessageReceivedListener());
            jdaBuilder.setAutoReconnect(true);
            JDA = jdaBuilder.build();
        }
    }
}
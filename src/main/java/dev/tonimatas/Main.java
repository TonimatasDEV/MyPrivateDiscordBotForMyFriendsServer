package dev.tonimatas;

import dev.tonimatas.listeners.CountListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.Arrays;

public class Main {
    public static JDA JDA;
    
    public static void main(String[] args) {
        String token = Config.getToken();
        
        if (token.isEmpty()) return;
        
        JDA = JDABuilder.createDefault(token)
                .disableCache(Arrays.stream(CacheFlag.values()).toList())
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(new CountListener())
                .setAutoReconnect(true)
                .build();
            
        CountListener.init();
    }
}
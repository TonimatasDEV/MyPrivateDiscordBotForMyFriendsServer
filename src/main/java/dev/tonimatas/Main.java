package dev.tonimatas;

import dev.tonimatas.listeners.CountListener;
import dev.tonimatas.listeners.SlashCommandListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static JDA JDA;
    
    public static void main(String[] args) {
        String token = Config.getToken();
        
        if (token.isEmpty()) return;
        
        JDA = JDABuilder.createDefault(token)
                .disableCache(Arrays.stream(CacheFlag.values()).toList())
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(new CountListener(), new SlashCommandListener())
                .setAutoReconnect(true)
                .build();
        
        JDA.updateCommands().addCommands(
                Commands.slash("ping", "Discord Ping! Pong!")
        ).queue();
            
        CountListener.init();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Done!");
    }
}
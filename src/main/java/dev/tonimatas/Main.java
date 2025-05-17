package dev.tonimatas;

import dev.tonimatas.config.Configs;
import dev.tonimatas.listeners.AutoRoleListener;
import dev.tonimatas.listeners.CountListener;
import dev.tonimatas.listeners.JoinLeaveMessageListener;
import dev.tonimatas.listeners.SlashCommandListener;
import dev.tonimatas.listeners.RouletteListener;
import dev.tonimatas.tasks.RouletteTask;
import dev.tonimatas.tasks.TemporalChannelTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

// TODO: Add stop method.
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final List<Thread> threads = new ArrayList<>();
    
    public static void main(String[] args) {
        String token = Configs.BOT.getValue("token").get();
        
        if (token.isEmpty()) return;
        
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .setAutoReconnect(true)
                .build();
        
        RouletteTask rouletteTask = new RouletteTask(jda);
        
        jda.addEventListener(new RouletteListener(rouletteTask), new SlashCommandListener(), 
                new AutoRoleListener(), new CountListener(), new JoinLeaveMessageListener());

        jda.updateCommands()
                .addCommands(Commands.slash("ping", "Discord Ping! Pong!"))
                .addCommands(Commands.slash("bet", "Make a bet on the roulette!"))
                .queue();
        

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error initializing JDA!", e);
        }

        registerTask(new TemporalChannelTask(jda));
        registerTask(rouletteTask);
        //registerTask(new ExperienceTask());

        LOGGER.info("Done!");
    }
    
    private static void registerTask(Runnable runnable) {
        Thread thread = new Thread(runnable);
        threads.add(thread);
        thread.start();
    }
}
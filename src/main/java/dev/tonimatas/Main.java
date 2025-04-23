package dev.tonimatas;

import dev.tonimatas.config.Configs;
import dev.tonimatas.listeners.TasksListener;
import dev.tonimatas.listeners.SlashCommandListener;
import dev.tonimatas.roulette.RouletteSlashCommandsListener;
import dev.tonimatas.schedules.RouletteManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class Main {
    public static boolean STOP = false;
    public static Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static JDA JDA;
    
    public static void main(String[] args) {
        Configs.init();

        String token = Configs.BOT.getValue("token").get();
        
        if (token.isEmpty()) return;
        
        JDA = JDABuilder.createDefault(token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(new TasksListener(), new SlashCommandListener(), new RouletteSlashCommandsListener())
                .setAutoReconnect(true)
                .build();
        
        JDA.updateCommands().addCommands(
                Commands.slash("ping", "Discord Ping! Pong!")
        ).queue();

        TaskManager.init();
        RouletteManager.init(); // TODO: Make it a task
        
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Done!");
    }
}
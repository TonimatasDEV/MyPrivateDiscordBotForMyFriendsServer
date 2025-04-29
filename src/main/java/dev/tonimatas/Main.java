package dev.tonimatas;

import dev.tonimatas.config.Configs;
import dev.tonimatas.listeners.AutoRoleListener;
import dev.tonimatas.listeners.CountListener;
import dev.tonimatas.listeners.JoinLeaveMessageListener;
import dev.tonimatas.listeners.SlashCommandListener;
import dev.tonimatas.roulette.RouletteSlashCommandsListener;
import dev.tonimatas.schedules.RouletteManager;
import dev.tonimatas.tasks.TemporalChannelTask;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    @Deprecated
    public static JDA JDA;
    @Deprecated
    public static boolean STOP = false;
    
    public static void main(String[] args) {
        Configs.init();

        String token = Configs.BOT.getValue("token").get();
        
        if (token.isEmpty()) return;
        
        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .addEventListeners(new SlashCommandListener(), new AutoRoleListener(), new CountListener(), 
                        new RouletteSlashCommandsListener(), new JoinLeaveMessageListener())
                .setAutoReconnect(true)
                .build();

        jda.updateCommands().addCommands(Commands.slash("ping", "Discord Ping! Pong!")).queue();

        JDA = jda; // TODO: Remove

        new TemporalChannelTask(jda).run();
        RouletteManager.init(); // TODO: Make it runnable

        LOGGER.info("Done!");
    }
}
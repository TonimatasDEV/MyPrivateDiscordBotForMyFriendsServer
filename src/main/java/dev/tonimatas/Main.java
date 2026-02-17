package dev.tonimatas;

import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import dev.tonimatas.commands.*;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.listeners.*;
import dev.tonimatas.systems.bank.DailyNotifier;
import dev.tonimatas.systems.executors.ExecutorManager;
import dev.tonimatas.systems.music.MusicManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import revxrsal.commands.Lamp;
import revxrsal.commands.jda.JDALamp;
import revxrsal.commands.jda.JDAVisitors;
import revxrsal.commands.jda.actor.SlashCommandActor;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    static void main() throws InterruptedException {
        JDA jda = JDABuilder.createDefault(BotFiles.CONFIG.token)
                .enableIntents(List.of(GatewayIntent.values()))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setAudioModuleConfig(new AudioModuleConfig().withDaveSessionFactory(new JDaveSessionFactory()))
                .enableCache(List.of(CacheFlag.values()))
                .setAutoReconnect(true)
                .build();
        
        Lamp<SlashCommandActor> lamp = JDALamp.builder().build();

        lamp.register(
                new BetCommand(),
                new DailyCommand(),
                new CoinFlipCommand(),
                new DiceCommand(),
                new HiCommand(),
                new MoneyCommand(),
                new MoneyTopCommand(),
                new OptionsCommand(),
                new PayCommand(),
                new PeakImpostorCommand(),
                new PingCommand(),
                new StatsCommand(),
                new StatsTopCommand(),
                new VersionCommand()
        );

        lamp.accept(JDAVisitors.slashCommands(jda));

        jda.addEventListener(
                new AutoRoleListener(),
                new CountListener(),
                new JoinLeaveMessageListener(),
                new TemporalChannelListener(),
                new MusicListener(),
                new PayListener(),
                new StatsListener(),
                new CoinFlipListener()
        );

        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "The Guild"));
        jda.awaitReady();

        addStopHook(jda);

        MusicManager.setup(jda);

        ExecutorManager.addRunnableAtFixedRate(new DailyNotifier(jda), 1, TimeUnit.MINUTES);
        ExecutorManager.addRunnableAtFixedRate(BotFiles::save, 5, TimeUnit.SECONDS);

        LOGGER.info("Done!");
    }

    private static void addStopHook(JDA jda) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping...");

            ExecutorManager.stop();
            jda.shutdown();

            try {
                if (!jda.awaitShutdown(10, TimeUnit.SECONDS)) {
                    jda.shutdownNow();
                }
            } catch (InterruptedException e) {
                LOGGER.error("Error stopping JDA: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }

            BotFiles.save();

            LOGGER.info("Stopped!");
        }));
    }
}
package dev.tonimatas;

import dev.tonimatas.cjda.CJDA;
import dev.tonimatas.cjda.CJDABuilder;
import dev.tonimatas.commands.*;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.listeners.*;
import dev.tonimatas.systems.bank.DailyNotifier;
import dev.tonimatas.systems.executors.ExecutorManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(BotFiles.CONFIG.token)
                .enableIntents(List.of(GatewayIntent.values()))
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableCache(List.of(CacheFlag.values()))
                .setAutoReconnect(true)
                .build();

        LocalizationFunction localization = ResourceBundleLocalizationFunction
                .fromBundles("lang", DiscordLocale.SPANISH)
                .build();

        CJDA cjda = CJDABuilder.createLocalized(jda, localization);

        cjda.registerCommands(
                new MoneyCommand(),
                new PingCommand(),
                new MoneyTopCommand(),
                new DailyCommand(),
                new HiCommand(),
                new StatsTopCommand(),
                new OptionsCommand(),
                new PayCommand(),
                new TransactionsCommand(),
                new BetCommand(),
                new VersionCommand(),
                new StatsCommand()
        ).init().queue();

        jda.addEventListener(
                new AutoRoleListener(),
                new CountListener(),
                new JoinLeaveMessageListener(),
                new TemporalChannelListener(),
                new TransactionListener(),
                new StatsListener()
        );

        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "The Guild"));
        jda.awaitReady();

        addStopHook(jda);

        ExecutorManager.addRunnableAtFixedRate(new DailyNotifier(jda), 1, TimeUnit.MINUTES);
        ExecutorManager.addRunnableAtFixedRate(BotFiles::save, 5, TimeUnit.SECONDS);

        LOGGER.info("Done!");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void addStopHook(JDA jda) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping...");

            ExecutorManager.stop();
            jda.shutdown();

            try {
                jda.awaitShutdown(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                LOGGER.error("Error stopping JDA: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }

            BotFiles.save();

            LOGGER.info("Stopped!");
        }));
    }
}
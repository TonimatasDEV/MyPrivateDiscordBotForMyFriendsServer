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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        JDA jda = JDABuilder.createDefault(BotFiles.CONFIG.token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
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
                new OptionsCommand(),
                new PayCommand(),
                new TransactionsCommand(),
                new BetCommand(),
                new VersionCommand()
        ).init().queue();

        jda.addEventListener(
                new AutoRoleListener(),
                new CountListener(),
                new JoinLeaveMessageListener(),
                new TemporalChannelListener(),
                new TransactionListener()
        );

        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "The Guild"));
        jda.awaitReady();

        addStopHook(jda);

        DailyNotifier.init(jda);
        BotFiles.autosave();

        LOGGER.info("Done!");
    }

    private static void addStopHook(JDA jda) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping...");
            jda.shutdown();

            try {
                jda.awaitShutdown();
            } catch (InterruptedException e) {
                LOGGER.error("Error stopping JDA: {}", e.getMessage());
                Thread.currentThread().interrupt();
            }

            ExecutorManager.stop();

            LOGGER.info("Stopped!");
        }));
    }
}
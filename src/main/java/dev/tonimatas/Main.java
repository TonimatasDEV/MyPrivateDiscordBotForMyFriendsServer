package dev.tonimatas;

import dev.tonimatas.cjda.CJDA;
import dev.tonimatas.cjda.CJDABuilder;
import dev.tonimatas.commands.MoneyCommand;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.listeners.*;
import dev.tonimatas.systems.bank.DailyNotifier;
import dev.tonimatas.systems.executors.ExecutorManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.commands.localization.LocalizationFunction;
import net.dv8tion.jda.api.interactions.commands.localization.ResourceBundleLocalizationFunction;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
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
                new MoneyCommand()
        ).init().queue();

        jda.addEventListener(
                new AutoRoleListener(),
                new CountListener(),
                new JoinLeaveMessageListener(),
                new SlashCommandListener(),
                new TemporalChannelListener(),
                new TransactionListener()
        );
        
        jda.updateCommands()
                .addCommands(Commands.slash("ping", "Discord Ping! Pong!")
                        .setLocalizationFunction(localization))
                .addCommands(Commands.slash("bet", "Make a bet on the roulette!")
                        .setLocalizationFunction(localization)
                        .addSubcommands(new SubcommandData("color", "Bet to color")
                                .addOptions(new OptionData(OptionType.STRING, "option", "Select the option.", true)
                                                .addChoice("green", "green")
                                                .addChoice("red", "red")
                                                .addChoice("black", "black"),
                                        new OptionData(OptionType.STRING, "money", "Money for the bet", true)))
                        .addSubcommands(new SubcommandData("column", "Bet to column")
                                .addOptions(new OptionData(OptionType.STRING, "option", "Select the option.", true)
                                                .addChoice("first", "first")
                                                .addChoice("second", "second")
                                                .addChoice("third", "third"),
                                        new OptionData(OptionType.STRING, "money", "Money for the bet", true)))
                        .addSubcommands(new SubcommandData("dozen", "Bet to dozen")
                                .addOptions(new OptionData(OptionType.STRING, "option", "Select the option.", true)
                                                .addChoice("first", "first")
                                                .addChoice("second", "second")
                                                .addChoice("third", "third"),
                                        new OptionData(OptionType.STRING, "money", "Money for the bet", true)))
                        .addSubcommands(new SubcommandData("number", "Bet to number"))
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("money-top", "The top 10 richest people among us!")
                        .setLocalizationFunction(localization)
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("daily", "Daily money!")
                        .setLocalizationFunction(localization)
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("pay", "Send an amount of money to a member.")
                        .setLocalizationFunction(localization)
                        .addOption(OptionType.USER, "user", "The member who is gonna receive your money.", true)
                        .addOption(OptionType.STRING, "amount", "The quantity of money you are gonna loose.", true)
                        .addOption(OptionType.STRING, "reason", "If you want to say why are you paying.", false))
                .addCommands(Commands.slash("hi", "Receive a greeting from our friendly bot.")
                        .setLocalizationFunction(localization)
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("options", "Configure your preferences")
                        .setLocalizationFunction(localization)
                        .addOption(OptionType.BOOLEAN, "daily_notify", "Do you prefer if the bot remembers when your daily reward is up?", true))
                .addCommands(Commands.slash("transactions", "See your own transactions.")
                        .setLocalizationFunction(localization)
                        .addOption(OptionType.USER, "user", "If you want to see the transactions of an specific user.", false))
                .queue();

        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "The Guild"));

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error initializing JDA!", e);
        }

        addStopHook(jda);

        DailyNotifier.init(jda);

        LOGGER.info("Done!");
    }

    private static void addStopHook(JDA jda) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.info("Stopping...");
            jda.shutdown();

            try {
                jda.awaitShutdown();
            } catch (InterruptedException e) {
                throw new RuntimeException("Error stopping JDA!", e);
            }

            ExecutorManager.stop();

            LOGGER.info("Stopped!");
        }));
    }
}
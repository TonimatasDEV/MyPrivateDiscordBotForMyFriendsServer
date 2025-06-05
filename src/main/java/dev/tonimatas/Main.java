package dev.tonimatas;

import dev.tonimatas.config.BankData;
import dev.tonimatas.config.BotConfig;
import dev.tonimatas.config.ExtraData;
import dev.tonimatas.config.JsonFile;
import dev.tonimatas.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

// TODO: Add stop method.
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    
    public static void main(String[] args) {
        BotConfig bot = JsonFile.loadOrCreate(BotConfig.class, "bot.json");
        BankData bankData = JsonFile.loadOrCreate(BankData.class, "data/bank.json");
        ExtraData extraData = JsonFile.loadOrCreate(ExtraData.class, "data/extra.json");

        JDA jda = JDABuilder.createDefault(bot.token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .setAutoReconnect(true)
                .build();
        
        jda.addEventListener(new RouletteListener(jda, bankData),
                new SlashCommandListener(),
                new AutoRoleListener(),
                new CountListener(extraData),
                new JoinLeaveMessageListener(),
                new TemporalChannelListener(),
                new BankListener(bankData)
        );

        jda.updateCommands()
                .addCommands(Commands.slash("ping", "Discord Ping! Pong!"))
                .addCommands(Commands.slash("bet", "Make a bet on the roulette!")
                        .addOption(OptionType.STRING, "bet-type", "Select what you want to bet.", true, true)
                        .addOption(OptionType.STRING, "bet-option", "Select the option.", true, true)
                        .addOption(OptionType.STRING, "bet-money", "Money for the bet.", true, true)
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("money", "See your amount of money.")
                        .setContexts(InteractionContextType.GUILD))
                        .addCommands(Commands.slash("money-top", "See your amount of money.")
                                .setContexts(InteractionContextType.GUILD))
                .queue();
        
        jda.getPresence().setActivity(Activity.of(Activity.ActivityType.WATCHING, "The Guild"));

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            throw new RuntimeException("Error initializing JDA!", e);
        }

        LOGGER.info("Done!");
    }
}
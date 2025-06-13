package dev.tonimatas;

import dev.tonimatas.config.*;
import dev.tonimatas.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.InteractionContextType;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

// TODO: Add stop method.
public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(BotFiles.CONFIG.token)
                .enableIntents(Arrays.stream(GatewayIntent.values()).toList())
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setAutoReconnect(true)
                .build();

        jda.addEventListener(new SlashCommandListener(),
                new AutoRoleListener(),
                new CountListener(),
                new JoinLeaveMessageListener(),
                new TemporalChannelListener(),
                new PaymentListener()
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
                .addCommands(Commands.slash("daily", "Daily money!")
                        .setContexts(InteractionContextType.GUILD))
                .addCommands(Commands.slash("pay", "Send an amount of money to a member.")
                        .addOption(OptionType.USER, "user", "The member who is gonna receive your money.", true)
                        .addOption(OptionType.STRING, "amount", "The quantity of money you are gonna loose.", true)
                        .addOption(OptionType.STRING, "reason", "If you want to say why are you paying.", false))
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
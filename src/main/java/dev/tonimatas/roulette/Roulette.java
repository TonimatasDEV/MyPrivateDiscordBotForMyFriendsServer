package dev.tonimatas.roulette;

import dev.tonimatas.config.BankData;
import dev.tonimatas.roulette.bets.Bet;
import dev.tonimatas.util.Messages;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
    private static final String ROULETTE_CHANNEL = "1371077395141885972";
    private static final String GUILD_ID = "1371074572786597960";
    private static final Random RAND = new SecureRandom();
    private final List<Bet> bets;
    private final Thread rouletteThread;
    private final BankData bankData;
    private final JDA jda;

    public Roulette(JDA jda, BankData bankData) {
        this.bankData = bankData;
        this.jda = jda;
        this.bets = new ArrayList<>();
        this.rouletteThread = rouletteThread();
    }

    public void addBet(Bet bet) {
        bankData.removeMoney(bet.getId(), bet.getMoney());
        bets.add(bet);

        if (!rouletteThread.isAlive()) {
            start();
        }
    }

    public Thread rouletteThread() {
        return new Thread(() -> {
            long remainingTime = 10;

            while (true) {
                if (remainingTime == 0) {
                    int winner = RAND.nextInt(0, 37);
                    
                    giveRewards(winner);

                    stop();
                    break;
                }

                // TODO: Every five seconds execute Roulette#updateDiscordChannel()

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {
                }

                remainingTime--;
            }
        });
    }
    
    public void start() {
        rouletteThread.start();
        // TODO: Update discord channel
        getRouletteChannel().sendMessage("Started").queue();
    }

    public void stop() {
        rouletteThread.interrupt();
        // TODO: Update discord channel
    }

    private void updateDiscordChannel() {
        // TODO: Implement me
    }

    private void giveRewards(int winner) {
        StringBuilder rewards = new StringBuilder();
        rewards.append("**The winner number is ").append(winner).append(".**\n\n");
        
        int count = 1;
        for (Bet bet : bets) {
            Member member = getGuild().getMemberById(bet.getId());
            if (member == null) continue;

            long reward = bet.getReward(winner);
            bankData.addMoney(bet.getId(), reward);
            
            rewards.append(count).append(". ").append(member.getEffectiveName()).append(" ").append(bet.getMessage(winner)).append("\n");
            count++;
        }
        
        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Roulette Results", rewards.toString());
        getRouletteChannel().sendMessageEmbeds(embed).queue();
    }

    @NotNull
    public Guild getGuild() {
        return Objects.requireNonNull(jda.getGuildById(GUILD_ID));
    }

    @NotNull
    public TextChannel getRouletteChannel() {
        return Objects.requireNonNull(getGuild().getTextChannelById(ROULETTE_CHANNEL));
    }
}

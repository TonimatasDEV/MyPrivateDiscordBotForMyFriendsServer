package dev.tonimatas.roulette;

import dev.tonimatas.config.BankData;
import dev.tonimatas.roulette.bets.Bet;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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
            rouletteThread.start();
        }
    }

    public Thread rouletteThread() {
        return new Thread(() -> {
            long remainingTime = 300;

            while (true) {
                if (remainingTime == 0) {
                    int winner = RAND.nextInt(0, 37);

                    for (Bet bet : bets) {
                        giveReward(bet, winner);
                    }

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

    public void stop() {
        rouletteThread.interrupt();
        // TODO: Update discord channel
    }

    private void updateDiscordChannel() {
        // TODO: Implement me
    }

    private void giveReward(Bet bet, int winner) {
        Member member = getGuild().getMemberById(bet.getId());

        if (member == null) return;

        long reward = bet.getReward(winner);

        bankData.addMoney(bet.getId(), reward);

        String text;

        if (reward > 0) {
            text = member.getEffectiveName() + " has ganado " + reward + "€ con tu apuesta.";
        } else {
            text = member.getEffectiveName() + " tu apuesta no ha ganado esta vez.\nHas perdido " + bet.getMoney() + ".";
        }

        getRouletteChannel().sendMessage(text).queue();
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

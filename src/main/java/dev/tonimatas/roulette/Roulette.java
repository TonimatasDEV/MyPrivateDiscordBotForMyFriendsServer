package dev.tonimatas.roulette;

import dev.tonimatas.config.BankData;
import dev.tonimatas.roulette.bets.Bet;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.security.SecureRandom;
import java.time.Duration;
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
    private final BankData bankData;
    private final JDA jda;
    private Thread rouletteThread;
    private String messageId;

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
            long remainingTime = 30;

            while (true) {
                if (remainingTime == 0) {
                    int winner = RAND.nextInt(0, 37);

                    giveRewards(winner);

                    stop();
                    break;
                }

                if (remainingTime % 5 == 0) {
                    updatePrimaryMessage(remainingTime);
                }

                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException ignored) {
                }

                remainingTime--;
            }
        });
    }

    public void start() {
        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Roulette", "**Has been started.**");
        getRouletteChannel().sendMessageEmbeds(embed).queue(hook -> {
            messageId = hook.getId();
            rouletteThread.start();
        });
    }

    public void stop() {
        rouletteThread.interrupt();
        rouletteThread = rouletteThread();
    }

    private void updatePrimaryMessage(long remainingTime) {
        Duration duration = Duration.ofSeconds(remainingTime);
        String formatted = TimeUtils.formatDuration(duration);

        StringBuilder message = new StringBuilder();
        message.append("**").append(formatted).append("**\n\n");

        message.append("Actual bets:\n");

        int count = 1;
        for (Bet bet : bets) {
            Member member = getGuild().getMemberById(bet.getId());
            if (member == null) continue;

            message.append(count).append(". ").append(member.getEffectiveName()).append(" ").append(bet.getBetMessage()).append("\n");
            count++;
        }

        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Roulette", message.toString());
        getRouletteChannel().editMessageEmbedsById(messageId, embed).queue();
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

            rewards.append(count).append(". ").append(member.getEffectiveName()).append(" ").append(bet.getRewardMessage(winner)).append("\n");
            count++;
        }

        bets.clear();

        MessageEmbed embed = Messages.getDefaultEmbed(jda, "Roulette", rewards.toString());
        getRouletteChannel().editMessageEmbedsById(messageId, embed).queue(Messages.deleteBeforeX(30));
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

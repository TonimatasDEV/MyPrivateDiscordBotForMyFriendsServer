package dev.tonimatas.systems.roulette;

import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.roulette.bets.*;
import dev.tonimatas.util.Messages;
import dev.tonimatas.util.TimeUtils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
    private static final String ROULETTE_NAME = "Roulette";
    private static final Random RANDOM = new SecureRandom();
    private static Roulette instance;
    private final List<Bet> bets;
    private final JDA jda;
    private Thread rouletteThread;
    private String messageId;

    public Roulette(JDA jda) {
        this.jda = jda;
        this.bets = new ArrayList<>();
        this.rouletteThread = rouletteThread();
    }

    public static Roulette getRoulette(JDA jda) {
        if (instance == null) {
            instance = new Roulette(jda);
        }

        return instance;
    }

    public static Bet getBet(String type, String id, String option, long money) {
        return switch (type) {
            case "color" -> new ColorBet(id, option, money);
            case "column" -> new ColumnBet(id, option, money);
            case "dozen" -> new DozenBet(id, option, money);
            case "number" -> new NumberBet(id, option, money);
            default -> null;
        };
    }

    public void addBet(Bet newBet) {
        BotFiles.USER.get(newBet.getId()).removeMoney(newBet.getMoney(), ROULETTE_NAME);

        Bet mergeableBet = null;
        for (Bet bet : bets) {
            if (bet.canMerge(newBet)) {
                mergeableBet = bet;
                break;
            }
        }

        if (mergeableBet != null) {
            mergeableBet.addMoney(newBet.getMoney());
        } else {
            bets.add(newBet);
        }

        if (!rouletteThread.isAlive()) {
            start();
        }
    }

    public Thread rouletteThread() {
        return new Thread(() -> {
            long remainingTime = 300;

            while (true) {
                if (remainingTime == 0) {
                    int winner = RANDOM.nextInt(0, 37);

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
                    crash();
                    stop();
                }

                remainingTime--;
            }
        });
    }

    private void crash() {
        for (Bet bet : bets) {
            BotFiles.USER.get(bet.getId()).addMoney(bet.getMoney(), ROULETTE_NAME + " crashed");
        }

        MessageEmbed embed = Messages.getErrorEmbed(jda, ROULETTE_NAME + " crashed. All money should be in your accounts.");
        BotFiles.CONFIG.getRouletteChannel(jda).sendMessageEmbeds(embed).queue();
    }

    public void start() {
        MessageEmbed embed = Messages.getDefaultEmbed(jda, ROULETTE_NAME, "**Has been started.**");
        BotFiles.CONFIG.getRouletteChannel(jda).sendMessageEmbeds(embed).queue(hook -> {
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
            User user = jda.getUserById(bet.getId());
            if (user == null) continue;

            message.append(count).append(". ").append(user.getEffectiveName()).append(" ").append(bet.getBetMessage()).append("\n");
            count++;
        }

        MessageEmbed embed = Messages.getDefaultEmbed(jda, ROULETTE_NAME, message.toString());
        BotFiles.CONFIG.getRouletteChannel(jda).editMessageEmbedsById(messageId, embed).queue();
    }

    private void giveRewards(int winner) {
        StringBuilder rewards = new StringBuilder();
        rewards.append("**The winner number is ").append(winner).append(".**\n\n");

        int count = 1;
        for (Bet bet : bets) {
            User user = jda.getUserById(bet.getId());
            if (user == null) continue;

            long reward = bet.getReward(winner);
            BotFiles.USER.get(bet.getId()).addMoney(reward, ROULETTE_NAME);

            rewards.append(count).append(". ").append(user.getEffectiveName()).append(" ").append(bet.getRewardMessage(winner)).append("\n");
            count++;
        }

        bets.clear();

        MessageEmbed embed = Messages.getDefaultEmbed(jda, ROULETTE_NAME, rewards.toString());
        BotFiles.CONFIG.getRouletteChannel(jda).editMessageEmbedsById(messageId, embed).queue();
    }
}

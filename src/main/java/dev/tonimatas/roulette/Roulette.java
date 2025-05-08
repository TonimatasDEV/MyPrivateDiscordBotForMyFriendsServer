package dev.tonimatas.roulette;

import dev.tonimatas.tasks.RouletteTask;
import net.dv8tion.jda.api.entities.Member;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
    private static final Random RAND = new SecureRandom();
    private long next;
    private final List<Bet> bets;
    private final RouletteTask rouletteTask;

    public Roulette(RouletteTask rouletteTask) {
        this.next = TimeUnit.MINUTES.toSeconds(5);
        this.bets = new ArrayList<>();
        this.rouletteTask = rouletteTask;
    }

    public boolean addBet(Bet bet) {
        if (bet.isValid()) {
            bets.add(bet);
            return true;
        }

        return false;
    }
    
    public void update() {
        if (next <= 0) {
            updateDiscordChannel();

            int winner = RAND.nextInt(0, 37);

            for (Bet bet : bets) {
                giveReward(bet, winner);
            }

            rouletteTask.saveMoney();
            resetTimer();
        } else {
            if (!bets.isEmpty()) {
                next--;
            } else {
                resetTimer();
            }
        }
    }
    
    private void updateDiscordChannel() {
        // TODO: Implement me
    }
    
    private void giveReward(Bet bet, int winner) {
        Member member = rouletteTask.getGuild().getMemberById(bet.id());

        if (member == null) return;

        long currentMoney = rouletteTask.getMoney(bet.id());
        long profit = bet.getProfit(winner);

        rouletteTask.setMoney(bet.id(), currentMoney + profit);

        String text;

        if (profit > 0) {
            text = member.getEffectiveName() + " has ganado " + profit + "€ con tu apuesta al " + bet.value() + " (" + bet.type() + ").";
        } else {
            text = member.getEffectiveName() + " tu apuesta al " + bet.value() + " (" + bet.type() + ") no ha ganado esta vez.\nHas perdido " + bet.money() + ".";
        }

        rouletteTask.getRouletteChannel().sendMessage(text).queue();
    }
    
    private void resetTimer() {
        next = TimeUnit.MINUTES.toSeconds(5);
    }
}

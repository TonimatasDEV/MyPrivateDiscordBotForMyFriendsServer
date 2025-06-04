package dev.tonimatas.roulette;

import dev.tonimatas.roulette.bets.Bet;
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

    public void addBet(Bet bet) {
        bets.add(bet);
    }
    
    public void update() {
        if (next <= 0) {
            int winner = RAND.nextInt(0, 37);

            for (Bet bet : bets) {
                giveReward(bet, winner);
            }

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
        Member member = rouletteTask.getGuild().getMemberById(bet.getId());

        if (member == null) return;

        long currentMoney = rouletteTask.getBank().getMoney(bet.getId());
        long reward = bet.getReward(winner);

        rouletteTask.getBank().setMoney(bet.getId(), currentMoney + reward);

        String text;

        if (reward > 0) {
            text = member.getEffectiveName() + " has ganado " + reward + "€ con tu apuesta"; // al " + bet.value() + " (" + bet.type() + ").";
        } else {
            text = member.getEffectiveName() + " tu apuesta al " + /*bet.value() + " (" + bet.type() + ")*/ "no ha ganado esta vez.\nHas perdido " + bet.getMoney() + ".";
        }

        rouletteTask.getRouletteChannel().sendMessage(text).queue();
    }
    
    private void resetTimer() {
        next = TimeUnit.MINUTES.toSeconds(5);
    }
}

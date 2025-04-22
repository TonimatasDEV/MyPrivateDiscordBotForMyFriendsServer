package dev.tonimatas.roulette;

import dev.tonimatas.schedules.RouletteManager;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Roulette {
    private static final Random RAND = new SecureRandom();
    private long next;
    private final List<Bet> bets;

    public Roulette() {
        next = TimeUnit.MINUTES.toSeconds(5);
        bets = new ArrayList<>();
    }

    public boolean addBet(Bet bet) {
        if (bet.isValid()) {
            bets.add(bet);
            return true;
        }

        return false;
    }
    
    public void tick() {
        if (next <= 0) {

            //  TODO: Discord description update

            int winner = RAND.nextInt(0, 37);

            for (Bet bet : bets) {
                bet.giveReward(winner);
            }

            RouletteManager.saveMoney();
            next = TimeUnit.MINUTES.toSeconds(5);
        } else {
            if (!bets.isEmpty()) {
                next--;
            }
        }
    }
}

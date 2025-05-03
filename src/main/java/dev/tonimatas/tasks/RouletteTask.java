package dev.tonimatas.tasks;

import dev.tonimatas.roulette.Roulette;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RouletteTask implements Runnable {
    @Deprecated
    public static final Map<String, Long> bankAccounts = new HashMap<>(); // TODO: Should be non-static
    @Deprecated
    public static Roulette roulette = new Roulette(); // TODO: Should be non-static

    public static void saveMoney() {
        // TODO: Implement me
    }

    private static void loadMoney() {
        // TODO: Implement me
    }

    public void run() {
        loadMoney();
        
        for (;;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

            roulette.tick();
        }
    }
}

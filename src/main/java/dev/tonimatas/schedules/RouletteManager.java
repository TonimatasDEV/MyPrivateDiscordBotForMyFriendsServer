package dev.tonimatas.schedules;

import dev.tonimatas.Main;
import dev.tonimatas.roulette.Roulette;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RouletteManager {
    public static final Map<String, Long> bankAccounts = new HashMap<>();
    public static Roulette roulette = new Roulette();

    public static void saveMoney() {
        // TODO: Implement me
    }

    private static void loadMoney() {
        // TODO: Implement me
    }

    public static void init() {
        loadMoney();

        new Thread(() -> { // TODO: See Main#main
            while (Main.STOP) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    continue;
                }

                roulette.tick();
            }
        }).start();
    }
}

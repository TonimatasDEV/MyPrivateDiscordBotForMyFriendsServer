package dev.tonimatas.systems.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorManager {
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);

    public static void addRunnableAtFixedRate(Runnable runnable, long period, TimeUnit unit) {
        EXECUTOR.scheduleAtFixedRate(runnable, 0, period, unit);
    }

    public static void stop() {
        LOGGER.info("Stopping ExecutorManager.");
        EXECUTOR.shutdown();

        try {
            if (!EXECUTOR.awaitTermination(3, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("ExecutorManager stopped.");
    }
}

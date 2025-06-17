package dev.tonimatas.systems.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ExecutorManager {
    private static final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);
    
    public static void addRunnableAtFixedRate(Runnable runnable, long delay, TimeUnit unit) {
        executor.scheduleAtFixedRate(runnable, 0, delay, unit);
    }

    public static void stop() {
        LOGGER.info("Stopping ExecutorManager.");
        executor.shutdown();

        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("ExecutorManager stopped.");
    }
}

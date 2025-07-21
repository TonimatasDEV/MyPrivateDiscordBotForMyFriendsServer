package dev.tonimatas.systems.executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.*;

public class ExecutorManager {
    private static final Queue<Runnable> stopTasks = new ConcurrentLinkedQueue<>();
    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorManager.class);

    private ExecutorManager() {
        // We don't need a constructor
    }

    public static void addRunnableAtFixedRate(Runnable runnable, long period, TimeUnit unit) {
        EXECUTOR.scheduleAtFixedRate(runnable, 0, period, unit);
    }
    
    public static void addStopTask(Runnable runnable) {
        stopTasks.add(runnable);
    }

    public static void stop() {
        LOGGER.info("Stopping ExecutorManager.");

        for (Runnable runnable : stopTasks) {
            EXECUTOR.submit(runnable);
        }

        EXECUTOR.shutdown();

        try {
            if (!EXECUTOR.awaitTermination(10, TimeUnit.SECONDS)) {
                EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.error("Interrupted while waiting for Executor to shutdown.", e);
            Thread.currentThread().interrupt();
        }

        LOGGER.info("ExecutorManager stopped.");
    }
}

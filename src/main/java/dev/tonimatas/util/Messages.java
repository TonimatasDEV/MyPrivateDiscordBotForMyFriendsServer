package dev.tonimatas.util;

import net.dv8tion.jda.api.interactions.InteractionHook;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Messages {
    public static Consumer<InteractionHook> deleteBeforeX() {
        return hook -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            hook.deleteOriginal().queue();
        };
    }
}

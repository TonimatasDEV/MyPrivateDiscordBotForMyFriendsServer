package dev.tonimatas.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.time.ZonedDateTime;
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
    
    public static EmbedBuilder getDefaultEmbed(JDA jda) {
        SelfUser selfUser = jda.getSelfUser();
        
        return new EmbedBuilder()
                .setAuthor(selfUser.getEffectiveName(), null, selfUser.getEffectiveAvatarUrl())
                .setColor(Color.YELLOW)
                .setTimestamp(ZonedDateTime.now())
                .setFooter("Anticonstitucionalmente");

    }
}

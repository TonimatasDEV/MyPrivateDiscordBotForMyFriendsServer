package dev.tonimatas.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.interactions.InteractionHook;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Messages {
    public static Consumer<InteractionHook> deleteBeforeX(long seconds) {
        return hook -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            hook.deleteOriginal().queue();
        };
    }
    
    public static MessageEmbed getDefaultEmbed(JDA jda, String title, String description) {
        SelfUser selfUser = jda.getSelfUser();
        
        return new EmbedBuilder()
                .setTitle(title)
                .setDescription(description)
                .setAuthor(selfUser.getEffectiveName(), null, selfUser.getEffectiveAvatarUrl())
                .setColor(Color.YELLOW)
                .setTimestamp(ZonedDateTime.now())
                .setFooter("Anticonstitucionalmente")
                .build();
    }

    public static MessageEmbed getErrorEmbed(JDA jda, String description) {
        SelfUser selfUser = jda.getSelfUser();

        return new EmbedBuilder()
                .setTitle("Error")
                .setDescription(description)
                .setAuthor(selfUser.getEffectiveName(), null, selfUser.getEffectiveAvatarUrl())
                .setColor(Color.RED)
                .setTimestamp(ZonedDateTime.now())
                .setFooter("Anticonstitucionalmente")
                .build();
    }
}

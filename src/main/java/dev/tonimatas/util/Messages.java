package dev.tonimatas.util;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Messages {
    private static final Logger LOGGER = LoggerFactory.getLogger(Messages.class);

    public static <T> Consumer<T> deleteBeforeX(long seconds) {
        return thing -> {
            try {
                TimeUnit.SECONDS.sleep(seconds);
            } catch (InterruptedException e) {
                return;
            }

            if (thing instanceof InteractionHook hook) {
                hook.deleteOriginal().queue(
                        null,
                        throwable -> {
                        }
                );
            } else if (thing instanceof Message msg) {
                msg.delete().queue(
                        null,
                        throwable -> {
                        }
                );
            } else {
                LOGGER.warn("Messages#deleteBeforeX is not compatible with: {}", thing.getClass().getName());
            }
        };
    }

    private static EmbedBuilder getBasicEmbedBuilder(JDA jda) {
        SelfUser selfUser = jda.getSelfUser();
        return new EmbedBuilder()
                .setAuthor(selfUser.getEffectiveName(), null, selfUser.getEffectiveAvatarUrl())
                .setTimestamp(ZonedDateTime.now())
                .setFooter("Anticonstitucionalmente");
    }

    public static MessageEmbed getDefaultEmbed(JDA jda, String title, String description) {
        return getBasicEmbedBuilder(jda)
                .setTitle(title)
                .setDescription(description)
                .setColor(Color.YELLOW)
                .build();
    }

    public static MessageEmbed getErrorEmbed(JDA jda, String description) {
        return getBasicEmbedBuilder(jda)
                .setTitle("Error")
                .setDescription(description)
                .setColor(Color.RED)
                .build();
    }
}

package dev.tonimatas.utils;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Deprecated
public class Getters {
    @Deprecated
    public static @NotNull Guild getGuild() {
        return nonNull(Main.JDA.getGuildById("1166787850235289693"));
    }
    
    @Deprecated
    public static @NotNull TextChannel getRouletteChannel() {
        return nonNull(getGuild().getTextChannelById("1344403446316531853"));
    }
    
    @Deprecated
    private static <T> T nonNull(T object) {
        return Objects.requireNonNull(object);
    }
}

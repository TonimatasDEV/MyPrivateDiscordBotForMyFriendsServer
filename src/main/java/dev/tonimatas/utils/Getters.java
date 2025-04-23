package dev.tonimatas.utils;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class Getters {
    public static @NotNull Guild getGuild() {
        return nonNull(Main.JDA.getGuildById("1166787850235289693"));
    }
    
    public static @NotNull TextChannel getCountChannel() {
        return nonNull(getGuild().getTextChannelById("1344403479501996032"));
    }
    
    public static @NotNull TextChannel getRouletteChannel() {
        return nonNull(getGuild().getTextChannelById("1344403446316531853"));
    }
    
    public static @NotNull Category getVoiceCategory() {
        return nonNull(getGuild().getCategoryById("1292533360857583697"));
    }
    
    public static @NotNull Role getUserAutoRole() {
        return nonNull(getGuild().getRoleById("1276355544873173116"));
    }

    public static @NotNull Role getBotAutoRole() {
        return nonNull(getGuild().getRoleById("1276514624631476244"));
    }
    
    public static @NotNull TextChannel getJoinLeaveChannel() {
        return nonNull(getGuild().getTextChannelById("1276330253052018800"));
    }
    
    private static <T> T nonNull(T object) {
        return Objects.requireNonNull(object);
    }
}

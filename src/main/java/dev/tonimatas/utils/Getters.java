package dev.tonimatas.utils;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.NotNull;

public class Getters {
    public static @NotNull Guild getGuild() {
        Guild guild = Main.JDA.getGuildById("1166787850235289693");

        if (guild != null) {
            return guild;
        } else {
            throw new NullPointerException("La Resistenzia guild Not Found!");
        }
    }
}

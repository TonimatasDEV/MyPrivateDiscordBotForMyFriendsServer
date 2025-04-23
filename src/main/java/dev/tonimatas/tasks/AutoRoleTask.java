package dev.tonimatas.tasks;

import dev.tonimatas.utils.Getters;
import net.dv8tion.jda.api.entities.User;

public class AutoRoleTask {
    public void addRole(User user) {
        if (user.isBot()) {
            Getters.getGuild().addRoleToMember(user, Getters.getBotAutoRole()).queue();
        } else {
            Getters.getGuild().addRoleToMember(user, Getters.getUserAutoRole()).queue();
        }
    }
}

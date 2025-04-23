package dev.tonimatas.tasks;

import dev.tonimatas.utils.Getters;
import net.dv8tion.jda.api.entities.User;

public class JoinLeaveMessageTask {
    public void sendJoin(User user) {
        int memberCount = Getters.getGuild().getMemberCount();
        Getters.getJoinLeaveChannel().sendMessageFormat("Hola %s, bienvenido al servidor de La Resistenzia. ¡Ya somos %s!", user.getAsMention(), memberCount).queue();
    }

    public void sendLeave(User user) {
        Getters.getJoinLeaveChannel().sendMessageFormat("La Resistenzia disminuye. Nuestro soldado %s a caido.", user.getName()).queue();
    }
}

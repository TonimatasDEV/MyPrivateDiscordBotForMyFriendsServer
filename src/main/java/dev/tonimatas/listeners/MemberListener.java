package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberListener extends ListenerAdapter {
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        sendMessage(event.getGuild(), event.getUser(), true);
        
        if (event.getUser().isBot()) {
            Role role = event.getGuild().getRoleById("1276514624631476244");

            if (role != null) {
                event.getGuild().addRoleToMember(event.getUser(), role).queue();
            }
        } else {
            Role role = event.getGuild().getRoleById("1276355544873173116");
            
            if (role != null) {
                event.getGuild().addRoleToMember(event.getUser(), role).queue();
            }
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        sendMessage(event.getGuild(), event.getUser(), false);
    }
    
    private static void sendMessage(Guild guild, User user, boolean join) {
        TextChannel channel = guild.getSystemChannel();

        if (channel != null) {
            if (join) {
                channel.sendMessageFormat("Hola %s, bienvenido al servidor de La Resistenzia. ¡Ya somos %s!", user.getAsMention(), guild.getMemberCount()).queue();
            } else {
                channel.sendMessageFormat("La Resistenzia disminuye. Nuestro soldado %s a caido.", user.getName()).queue();
            }
        }
    }
}

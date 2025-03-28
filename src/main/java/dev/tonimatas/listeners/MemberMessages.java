package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MemberMessages extends ListenerAdapter {
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        sendMessage(event.getGuild(), event.getMember(), true);
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        sendMessage(event.getGuild(), event.getMember(), false);
    }
    
    private static void sendMessage(Guild guild, Member member, boolean join) {
        TextChannel channel = guild.getSystemChannel();

        if (channel != null) {
            if (join) {
                channel.sendMessageFormat("Hola %s, bienvenido al servidor de La Resistenzia. ¡Ya somos %s!", member.getAsMention(), guild.getMemberCount()).queue();
            } else {
                channel.sendMessageFormat("La Resistenzia disminuye. Nuestro soldado %s a caido.", member.getUser().getName()).queue();
            }
        }
    }
}

package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinLeaveMessageListener extends ListenerAdapter {
    private static final String CHANNEL_ID = "1371077003528241253";

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        int memberCount = event.getGuild().getMemberCount();

        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);

        if (channel != null) {
            channel.sendMessageFormat("Hola %s, bienvenido a nuestro servidor. ¡Ya somos %s!", event.getUser().getAsMention(), memberCount).queue();
        }
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);

        if (channel != null) {
            channel.sendMessageFormat("%s Se ha salido del servidor.", event.getUser().getName()).queue();
        }
    }
}

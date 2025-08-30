package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinLeaveMessageListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        int memberCount = event.getGuild().getMemberCount();
        TextChannel channel = BotFiles.CONFIG.getJoinLeftChannel(event.getJDA());

        if (channel == null) return;

        String welcome = "Hola " + member.getAsMention() + ", bienvenido a nuestro servidor. ¡Ya somos " + memberCount + "!";
        channel.sendMessage(welcome).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel channel = BotFiles.CONFIG.getJoinLeftChannel(event.getJDA());

        if (channel != null) {
            channel.sendMessageFormat("%s Se ha salido del servidor.", event.getUser().getName()).queue();
        }
    }
}

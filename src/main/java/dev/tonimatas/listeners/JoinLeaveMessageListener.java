package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;
import java.time.ZonedDateTime;

public class JoinLeaveMessageListener extends ListenerAdapter {
    private static final String GIF_URL = "https://media2.giphy.com/media/v1.Y2lkPTc5MGI3NjExYmtmNDN0ZTE1aTh5bW54M2lxM2kzc2I4ZmF2aHpiOHowb29waWR6diZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/78sCPOfIAsnbzk3f1D/giphy.gif";

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Member member = event.getMember();
        int memberCount = event.getGuild().getMemberCount();
        TextChannel channel = BotFiles.CONFIG.getJoinLeftChannel(event.getJDA());

        if (channel == null) return;

        TextChannel textChannel = event.getGuild().getRulesChannel();
        MessageEmbed embed = new EmbedBuilder()
                .setTitle("Bienvenido\\a a " + event.getGuild().getName())
                .setAuthor(member.getEffectiveName(), null, member.getEffectiveAvatarUrl())
                .setDescription(" - Recuerda leer las " + (textChannel == null ? "normas" : textChannel.getAsMention()))
                .setThumbnail(event.getGuild().getIconUrl())
                .setImage(GIF_URL)
                .setColor(Color.RED.darker())
                .setFooter("¡Ya somos " + memberCount + "!")
                .setTimestamp(ZonedDateTime.now())
                .build();
        channel.sendMessageEmbeds(embed).queue();
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel channel = BotFiles.CONFIG.getJoinLeftChannel(event.getJDA());

        if (channel != null) {
            channel.sendMessageFormat("Ohh no! %s left this server.", event.getUser().getName()).queue();
        }
    }
}

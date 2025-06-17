package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Invite;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteCreateEvent;
import net.dv8tion.jda.api.events.guild.invite.GuildInviteDeleteEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JoinLeaveMessageListener extends ListenerAdapter {
    private static final String CHANNEL_ID = "1371077003528241253";
    private final Map<String, List<Invite>> cachedInvites = new HashMap<>();

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        Guild guild = event.getGuild();
        String guildId = guild.getId();

        int memberCount = event.getGuild().getMemberCount();

        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);

        if (channel == null) return;

        guild.retrieveInvites().queue(newInvites -> {
            List<Invite> oldInvites = cachedInvites.get(guildId);
            Invite usedInvite = null;

            if (oldInvites != null) {
                for (Invite newInvite : newInvites) {
                    for (Invite oldInvite : oldInvites) {
                        if (newInvite.getCode().equals(oldInvite.getCode()) && newInvite.getUses() > oldInvite.getUses()) {
                            usedInvite = newInvite;
                            break;
                        }
                    }
                    if (usedInvite != null) break;
                }
            }
            cachedInvites.put(guildId, newInvites);

            Member newMember = event.getMember();

            StringBuilder welcome = new StringBuilder("Hola ")
                    .append(newMember.getAsMention())
                    .append(", bienvenido a nuestro servidor. ¡Ya somos ")
                    .append(memberCount)
                    .append("!");
            if (usedInvite != null && usedInvite.getInviter() != null) {
                welcome.append("\n Invitado por: **")
                        .append(usedInvite.getInviter().getName())
                        .append("**.");
            }

            channel.sendMessage(welcome.toString()).queue();
        });
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TextChannel channel = event.getGuild().getTextChannelById(CHANNEL_ID);

        if (channel != null) {
            channel.sendMessageFormat("%s Se ha salido del servidor.", event.getUser().getName()).queue();
        }
    }


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        for (Guild guild : event.getJDA().getGuilds()) {
            guild.retrieveInvites().queue(invites -> cachedInvites.put(guild.getId(), invites));
        }
    }

    @Override
    public void onGuildInviteCreate(@NotNull GuildInviteCreateEvent event) {
        updateCachedInvites(event.getGuild());
    }

    @Override
    public void onGuildInviteDelete(@NotNull GuildInviteDeleteEvent event) {
        updateCachedInvites(event.getGuild());
    }

    private void updateCachedInvites(Guild guild) {
        guild.retrieveInvites().queue(invites -> cachedInvites.put(guild.getId(), invites));
    }
}

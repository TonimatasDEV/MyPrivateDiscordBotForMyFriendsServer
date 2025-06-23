package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoRoleListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        Role role = BotFiles.CONFIG.getAutoRole(event.getJDA());

        if (role != null) {
            event.getGuild().addRoleToMember(user, role).queue();
        }
    }
}

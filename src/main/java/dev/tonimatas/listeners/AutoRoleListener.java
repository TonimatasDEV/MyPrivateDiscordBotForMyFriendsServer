package dev.tonimatas.listeners;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AutoRoleListener extends ListenerAdapter {
    private static final String USER_ROLE_ID = "1371074867423875112";
    private static final String BOT_ROLE_ID = "1371077723685912697";
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        User user = event.getUser();
        Role role;
        
        if (user.isBot()) {
            role = event.getGuild().getRoleById(BOT_ROLE_ID);
        } else {
            role = event.getGuild().getRoleById(USER_ROLE_ID);
        }

        if (role != null) {
            event.getGuild().addRoleToMember(user, role).queue();
        }
    }
}

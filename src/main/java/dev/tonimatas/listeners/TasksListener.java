package dev.tonimatas.listeners;

import dev.tonimatas.TaskManager;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class TasksListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        TaskManager.countTask.checkNewNumber(event.getChannel().getId(), event.getMessage());
    }
    
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        TaskManager.joinLeaveMessageTask.sendJoin(event.getUser());
        TaskManager.autoRoleTask.addRole(event.getUser());
    }

    @Override
    public void onGuildMemberRemove(GuildMemberRemoveEvent event) {
        TaskManager.joinLeaveMessageTask.sendLeave(event.getUser());
    }
}

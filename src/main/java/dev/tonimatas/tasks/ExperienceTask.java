package dev.tonimatas.tasks;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;

import java.util.concurrent.TimeUnit;

// TODO: Finish it?
public class ExperienceTask implements Runnable {
    private final JDA jda;
    
    public ExperienceTask(JDA jda) {
        this.jda = jda;
    }
    
    @Override
    public void run() {
        for (;;) {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                continue;
            }

            Guild guild = jda.getGuildById("1166787850235289693");
            
            if (guild == null) continue;
            
            for (GuildVoiceState guildVoiceState : guild.getVoiceStates()) {
                if (guildVoiceState == null) continue;
                if (guildVoiceState.isDeafened()) continue;
                if (!guildVoiceState.inAudioChannel()) continue;

                if (guildVoiceState.isMuted()) {
                    // TODO: Give muted XP 1/3
                } else {
                    // TODO: Give XP
                }
            }
        }
    }
}

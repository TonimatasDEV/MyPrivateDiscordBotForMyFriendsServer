package dev.tonimatas.schedules;

import dev.tonimatas.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;

import java.util.concurrent.TimeUnit;

// TODO: Finish it?
public class VoiceXP extends Thread {
    @Override
    public void run() {
        while (!Main.STOP) {
            try {
                TimeUnit.SECONDS.sleep(30);
            } catch (InterruptedException e) {
                System.out.println();
            }

            Guild guild = Main.JDA.getGuildById("1166787850235289693");
            
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

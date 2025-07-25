package dev.tonimatas.systems.bank;

import dev.tonimatas.api.bank.DailyInfo;
import dev.tonimatas.api.user.UserSettings;
import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.JDA;

import java.time.LocalDateTime;

public class DailyNotifier implements Runnable {
    private final JDA jda;

    public DailyNotifier(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        BotFiles.USER.getUsers().keySet().forEach(userId -> {
            UserSettings settings = BotFiles.USER.get(userId).getSettings();
            DailyInfo dailyInfo = BotFiles.USER.get(userId).getDaily();

            if (!settings.isNotifyDaily() || dailyInfo.isNotified()) return;
            if (dailyInfo.getLast() == null || now.isBefore(dailyInfo.getNext())) return;

            jda.retrieveUserById(userId).queue(user ->
                    user.openPrivateChannel().queue(channel ->
                            channel.sendMessage("Reclama tú daily, ya está disponible, usa `/daily` en el canal de comandos para reclamarlo.")
                                    .queue()
                    )
            );

            dailyInfo.setNotified(true);
            BotFiles.USER.save();
        });
    }
}

package dev.tonimatas.systems.bank;

import dev.tonimatas.config.BankData;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.executors.ExecutorManager;
import dev.tonimatas.systems.settings.UserSettings;
import net.dv8tion.jda.api.JDA;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

public class DailyNotifier implements Runnable {
    private final JDA jda;
    private final BankData bank;

    public DailyNotifier(JDA jda) {
        this.jda = jda;
        this.bank = BotFiles.BANK;
    }

    public static void init(JDA jda) {
        ExecutorManager.addRunnableAtFixedRate(new DailyNotifier(jda), 1, TimeUnit.MINUTES);
    }

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        bank.daily.keySet().forEach(userId -> {
            UserSettings settings = BotFiles.SETTINGS.getSettings(userId);
            DailyInfo dailyInfo = bank.getDaily(userId);
            
            if (!settings.isNotifyDaily() || dailyInfo.isNotified()) return;
            if (dailyInfo.getLast() == null || now.isBefore(dailyInfo.getNext())) return;

            jda.retrieveUserById(userId).queue(user ->
                    user.openPrivateChannel().queue(channel ->
                            channel.sendMessage("Reclama tú daily, ya está disponible, usa `/daily` en el canal de comandos para reclamarlo.")
                                    .queue()
                    )
            );

            dailyInfo.setNotified(true);
            BotFiles.SETTINGS.save();
        });
    }
}

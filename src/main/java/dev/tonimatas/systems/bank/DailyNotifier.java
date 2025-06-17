package dev.tonimatas.systems.bank;

import dev.tonimatas.config.BankData;
import dev.tonimatas.config.BotFiles;
import dev.tonimatas.systems.executors.ExecutorManager;
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

    @Override
    public void run() {
        LocalDateTime now = LocalDateTime.now();

        bank.daily.keySet().forEach(userId -> {
            UserSettings settings = BotFiles.SETTINGS.getSettings(userId);
            if (!settings.isNotifyDaily() || settings.isNotifiedDaily()) return;

            LocalDateTime lastClaim = bank.getDaily(userId);
            if (lastClaim == null || now.isBefore(lastClaim.plusHours(24))) return;

            jda.retrieveUserById(userId).queue(user ->
                user.openPrivateChannel().queue(channel ->
                    channel.sendMessage("Reclama tu daily, ya está disponible, usa `/daily` para reclamarla")
                            .queue()
                )
            );

            settings.setNotifiedDaily(true);
            BotFiles.SETTINGS.save();
        });
    }

    public static void init(JDA jda) {
        ExecutorManager.addRunnableAtFixedRate(new DailyNotifier(jda),0,TimeUnit.MINUTES);
    }
}

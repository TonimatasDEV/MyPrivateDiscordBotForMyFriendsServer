package dev.tonimatas.tasks;

import dev.tonimatas.config.BankData;
import dev.tonimatas.roulette.Roulette;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class RouletteTask implements Runnable {
    private static final String ROULETTE_CHANNEL = "1371077395141885972";
    private static final String GUILD_ID = "1371074572786597960";
    private final BankData bankData;
    private final Roulette roulette;
    private final JDA jda;

    public RouletteTask(JDA jda, BankData bankData) {
        this.roulette = new Roulette(this);
        this.jda = jda;
        this.bankData = bankData;
    }

    public void run() {
        for (;;) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                continue;
            }

            roulette.update();
        }
    }
    
    public Roulette get() {
        return roulette;
    }
    
    public BankData getBank() {
        return bankData;
    }
    
    @NotNull
    public Guild getGuild() {
        return Objects.requireNonNull(jda.getGuildById(GUILD_ID));
    }

    @NotNull
    public TextChannel getRouletteChannel() {
        return Objects.requireNonNull(getGuild().getTextChannelById(ROULETTE_CHANNEL));
    }
}

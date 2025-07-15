package dev.tonimatas.config.config;

import dev.tonimatas.config.JsonFile;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BotConfig extends JsonFile {
    public final String token;
    private final Map<String, String> ids;
    

    @SuppressWarnings("unused")
    public BotConfig() {
        this("default-token", new HashMap<>());
    }

    public BotConfig(String token, Map<String, String> ids) {
        this.token = token;
        this.ids = ids;
    }

    @Override
    protected String getFilePath() {
        return "bot.json";
    }
    
    public TextChannel getRouletteChannel(JDA jda) {
        return Objects.requireNonNull(jda.getTextChannelById(ids.get("rouletteChannelId")));
    }
    
    public String getCommandsChannelId() {
        return ids.get("commandsChannelId");
    }
    
    public String getTemporaryChannelId() {
        return ids.get("temporalChannelId");
    }

    public Category getTemporalCategory(JDA jda) {
        return Objects.requireNonNull(jda.getCategoryById(ids.get("temporalCategoryId")));
    }
    
    public String getCountingChannelId() {
        return ids.get("countingChannelId");
    }
    
    public TextChannel getJoinLeftChannel(JDA jda) {
        return Objects.requireNonNull(jda.getTextChannelById(ids.get("joinLeftChannelId")));
    }

    public String getTicTacToeChannelId() {
        return ids.get("tictactoeChannelId");
    }

    public Role getAutoRole(JDA jda) {
        return Objects.requireNonNull(jda.getRoleById(ids.get("autoRoleId")));
    }
}

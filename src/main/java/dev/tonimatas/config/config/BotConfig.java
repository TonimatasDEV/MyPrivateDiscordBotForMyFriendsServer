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
    public final String youtubeToken;
    private final Map<String, String> ids;


    @SuppressWarnings("unused")
    public BotConfig() {
        this("default-token", "youtube-token", new HashMap<>());
    }

    public BotConfig(String token, String youtubeToken, Map<String, String> ids) {
        this.token = token;
        this.youtubeToken = youtubeToken;
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

    public Role getAutoRole(JDA jda) {
        return Objects.requireNonNull(jda.getRoleById(ids.get("autoRoleId")));
    }

    public Role getAllowedGameRole(JDA jda) {
        return Objects.requireNonNull(jda.getRoleById(ids.get("allowedGameRole")));
    }
    
    public TextChannel getMusicChannel(JDA jda) {
        return Objects.requireNonNull(jda.getTextChannelById(ids.get("musicChannelId")));
    }

    public String getNewsChannelId() {
        return ids.get("newsChannelId");
    }

    public String getAnnouncementsChannelId() {
        return ids.get("announcementsChannelId");
    }
}

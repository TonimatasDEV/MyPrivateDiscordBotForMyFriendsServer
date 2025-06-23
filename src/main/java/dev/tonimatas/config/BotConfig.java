package dev.tonimatas.config;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.Objects;

public class BotConfig extends JsonFile {
    public final String token;
    private final String rouletteChannelId;
    private final String commandsChannelId;
    private final String temporalChannelId;
    private final String temporalCategoryId;
    private final String countingChannelId;
    private final String joinLeftChannelId;
    private final String autoRoleId;
    

    @SuppressWarnings("unused")
    public BotConfig() {
        this("default-token", "", "", "", "", "", "", "");
    }

    public BotConfig(String token, String rouletteChannelId, String commandsChannelId, String temporalChannelId, String temporalCategoryId, String countingChannelId, String joinLeftChannelId, String autoRoleId) {
        this.token = token;
        this.rouletteChannelId = rouletteChannelId;
        this.commandsChannelId = commandsChannelId;
        this.temporalChannelId = temporalChannelId;
        this.temporalCategoryId = temporalCategoryId;
        this.countingChannelId = countingChannelId;
        this.joinLeftChannelId = joinLeftChannelId;
        this.autoRoleId = autoRoleId;
    }

    @Override
    protected String getFilePath() {
        return "bot.json";
    }
    
    public TextChannel getRouletteChannel(JDA jda) {
        return Objects.requireNonNull(jda.getTextChannelById(rouletteChannelId));
    }
    
    public String getCommandsChannelId() {
        return commandsChannelId;
    }
    
    public String getTemporaryChannelId() {
        return temporalChannelId;
    }
    
    public String getCountingChannelId() {
        return countingChannelId;
    }
    
    public TextChannel getJoinLeftChannel(JDA jda) {
        return Objects.requireNonNull(jda.getTextChannelById(joinLeftChannelId));
    }
    
    public Role getAutoRole(JDA jda) {
        return Objects.requireNonNull(jda.getRoleById(autoRoleId));
    }
    
    public Category getTemporalCategory(JDA jda) {
        return Objects.requireNonNull(jda.getCategoryById(temporalCategoryId));
    }
}

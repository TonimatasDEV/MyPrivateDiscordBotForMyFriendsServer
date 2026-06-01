package dev.tonimatas.listeners;

import dev.tonimatas.config.BotFiles;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceChannelLoggerListener extends ListenerAdapter {
    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Category category = BotFiles.CONFIG.getTemporalCategory(event.getJDA());
        AudioChannelUnion joined = event.getChannelJoined();
        AudioChannelUnion left = event.getChannelLeft();

        if (category == null) return;
        if (joined != null && joined.getType() == ChannelType.VOICE) {
            VoiceChannel voice = joined.asVoiceChannel();

            if (!voice.getId().equals(BotFiles.CONFIG.getTemporaryChannelId()) && category.getId().equals(voice.getParentCategoryId())) {
                voice.sendMessage("✅ **" +event.getEntity().getEffectiveName() + "** joined in the channel.").queue();
            }
        }

        if (left != null) {
            VoiceChannel voice = left.asVoiceChannel();

            if (!voice.getId().equals(BotFiles.CONFIG.getTemporaryChannelId()) && category.getId().equals(voice.getParentCategoryId())) {
                voice.sendMessage("❌ **" + event.getEntity().getEffectiveName() + "** left in the channel.").queue();
            }
        }
    }
}

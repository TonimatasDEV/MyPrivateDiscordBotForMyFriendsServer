package dev.tonimatas.points;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoicePointsManager {
    // Map for storaging all users with their respective points
    private final Map<String, Long> userPoints = new HashMap<>();

    public void processVoiceActivity(Guild guild) {
        if (guild == null) return;

        for (GuildVoiceState voiceState : guild.getVoiceStates()) {
            if (voiceState.getChannel() == null || guild.getAfkChannel() == voiceState.getChannel().asVoiceChannel()) {
                continue;
            }

            List<Member> members = voiceState.getChannel().getMembers();
            if (members.size() <= 1) continue;

            int defPoints = 5;
            double multiplier = getMultiplier(voiceState);

            // Total points which it is the defPoints * Multiplier
            long totPoints = (long) (defPoints * multiplier);

            addPoints(voiceState.getMember().getId(), totPoints);
        }
    }

    private double getMultiplier(GuildVoiceState voiceState) {
        double multiplier = 1.0;

        if (voiceState.isDeafened()) {
            multiplier *= 0.25;
        } else if (voiceState.isMuted()) {
            multiplier *= 0.5;
        }
        if (voiceState.isStream()) {
            multiplier *= 2.0;
        }

        return multiplier;
    }

    private void addPoints(String userId, long points) {
        userPoints.put(userId, userPoints.getOrDefault(userId, 0L) + points);
    }

    // Method for getting each user points (leaderboard)
    public long getPoints(String userId) {
        return userPoints.getOrDefault(userId, 0L);
    }

    // Method to get all user and their respective points (leaderboard)
    public Map<String, Long> getAllPoints() {
        return userPoints;
    }
}

package dev.tonimatas.voicexp;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoiceXPManager {
    // Map for storaging all users with their respective points
    private final Map<String, Integer> userPoints = new HashMap<>();

    public void processVoiceActivity(Guild guild) {
        for (VoiceChannel voiceChannel : guild.getVoiceChannels()) {
            List<Member> members = voiceChannel.getMembers();
            if (members.size() <= 1) continue;

            for (Member member : members) {
                if (member.getUser().isBot()) continue;

                // Default number of points which it will be given to each user
                int defPoints = 5;
                double multiplier = getMultiplier(member);

                // Total points which it is the defPoints * Multiplier
                int totPoints = (int) (defPoints * multiplier);

                addPoints(member.getId(), totPoints);
            }
        }
    }

    private double getMultiplier(Member member) {
        double multiplier = 1.0;

        if (member.getVoiceState().isDeafened()) multiplier *= 0.25;
        else if (member.getVoiceState().isMuted()) multiplier *= 0.5;
        if (member.getVoiceState().isStream()) multiplier *= 2.0;

        return multiplier;
    }

    private void addPoints(String userId, int points) {
        userPoints.put(userId, userPoints.getOrDefault(userId, 0) + points);
    }

    // Method for getting each user points (leaderboard)
    public int getPoints(String userId) {
        return userPoints.getOrDefault(userId,0);
    }

    // Method to get all user and their respective points (leaderboard)
    public Map<String,Integer> getAllPoints() {
        return userPoints;
    }
}

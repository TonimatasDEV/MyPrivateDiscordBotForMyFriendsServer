package dev.tonimatas.systems.points;

import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.Map;

public class TextPointsManager {
    private final Map<String, Long> userPoints = new HashMap<>();
    private final Map<String, Long> lastMessageTime = new HashMap<>();

    private final int DEFAULT_POINTS = 5;
    private final long MESSAGE_COOLDOWN_MS = 1000;

    public void processTextMessage(User user, String content) {
        if (user == null || content == null || content.isBlank()) return;

        String userId = user.getId();
        long now = System.currentTimeMillis();

        // AntiSpam
        long lastTime = lastMessageTime.getOrDefault(userId, 0L);
        int charCount = content.trim().length();
        if (charCount < 5) return;

        // Reverse exponential multiplier
        double multiplier = getExponentialMultiplier(charCount);

        long points = Math.min((long) (charCount * multiplier), DEFAULT_POINTS);
        addPoints(userId, points);
    }

    private double getExponentialMultiplier(int charCount) {
        return Math.max(0.05, Math.min(1.0, 50 / Math.pow(charCount, 0.8)));
    }

    private void addPoints(String userId, long points) {
        userPoints.put(userId, userPoints.getOrDefault(userId,0L) + points);
    }

    public long getPoints(String userId) {
        return userPoints.getOrDefault(userId, 0L);
    }

    public Map<String, Long> getAllPoints() {
        return userPoints;
    }
}

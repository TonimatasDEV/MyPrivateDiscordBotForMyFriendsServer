package dev.tonimatas.config;

import java.util.HashMap;
import java.util.Map;

public class PointsData extends JsonFile {
    public final Map<String, Long> points;

    @SuppressWarnings("unused")
    public PointsData() {
        this(new HashMap<>());
    }

    public PointsData(Map<String, Long> points) {
        this.points = points;
    }

    @Override
    protected String getFilePath() {
        return "data/points.json";
    }

    public long getPoints(String userId) {
        if (!points.containsKey(userId)) {
            setPoints(userId, 0);
        }
        return this.points.get(userId);
    }

    public void setPoints(String userId, long points) {
        this.points.put(userId, points);
        save();
    }

    public void addPoints(String userId, long points) {
        setPoints(userId, getPoints(userId) + points);
    }

    public void removePoints(String userId, long points) {
        setPoints(userId, getPoints(userId) - points);
    }
}

package dev.tonimatas.config;

import java.util.HashMap;
import java.util.Map;

public class PointsData extends JsonFile {
    public Map<String, Long> points = new HashMap<>();

    @Override
    protected String getFilePath() {
        return "data/points.json";
    }

    public long getPoints(String memberId) {
        if (!points.containsKey(memberId)) {
            setPoints(memberId, 0);
        }
        return this.points.get(memberId);
    }

    public void setPoints(String memberId, long points) {
        this.points.put(memberId, points);
        save();
    }

    public void addPoints(String memberId, long points) {
        setPoints(memberId, getPoints(memberId) + points);
    }

    public void removePoints(String memberId, long points) {
        setPoints(memberId, getPoints(memberId) - points);
    }
}

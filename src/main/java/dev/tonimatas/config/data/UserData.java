package dev.tonimatas.config.data;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.api.user.UserStats;
import dev.tonimatas.config.JsonFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class UserData extends JsonFile {
    private final Map<String, UserInfo> users;

    public UserData() {
        this(new HashMap<>());
    }

    public UserData(Map<String, UserInfo> users) {
        this.users = users;
    }

    public UserInfo get(String userId) {
        UserInfo user = users.computeIfAbsent(userId, id -> new UserInfo(userId));
        save();
        return user;
    }

    public Map<String, UserInfo> getUsers() {
        return users;
    }

    public List<Map.Entry<String, UserStats>> getUserStatsEntries() {
        Map<String, UserStats> entries = users
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getStats()));

        return new ArrayList<>(entries.entrySet());
    }

    @Override
    protected String getFilePath() {
        return "data/user.json";
    }
}

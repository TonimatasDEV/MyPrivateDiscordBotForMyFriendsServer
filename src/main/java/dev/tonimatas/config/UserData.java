package dev.tonimatas.config;

import dev.tonimatas.api.data.UserInfo;

import java.util.HashMap;
import java.util.Map;

public class UserData extends JsonFile {
    private final Map<String, UserInfo> users;

    public UserData() {
        this(new HashMap<>());
    }

    public UserData(Map<String, UserInfo> users) {
        this.users = users;
    }

    public UserInfo get(String userId) {
        return getOrElse(userId);
    }
    
    public Map<String, UserInfo> getUsers() {
        return users;
    }

    private UserInfo getOrElse(String userId) {
        UserInfo user = users.computeIfAbsent(userId, id -> new UserInfo(userId));
        save();
        return user;
    }

    @Override
    protected String getFilePath() {
        return "data/user.json";
    }
}

package dev.tonimatas.config.data;

import dev.tonimatas.api.user.UserInfo;
import dev.tonimatas.config.JsonFile;

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
        UserInfo user = users.computeIfAbsent(userId, id -> new UserInfo(userId));
        save();
        return user;
    }

    public Map<String, UserInfo> getUsers() {
        return users;
    }

    @Override
    protected String getFilePath() {
        return "data/user.json";
    }
}

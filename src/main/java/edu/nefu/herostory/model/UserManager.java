package edu.nefu.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户管理
 */
public final class UserManager {
    static private final Map<Integer, User> _userMap = new HashMap<>();

    private UserManager() {
    }

    public static void addUser(User newUser) {
        if (null != newUser) {
            _userMap.put(newUser.userId, newUser);
        }
    }

    public static void removeUserById(int userId) {
        _userMap.remove(userId);
    }

    public static Collection<User> listUser() {
        return _userMap.values();
    }
}

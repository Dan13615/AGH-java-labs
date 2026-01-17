package i2jp.oop;

import java.util.HashMap;
import java.util.Map;

public class GroupRegistry {
    private static final Map<String, String> REG = new HashMap<>();

    public static boolean isAssigned(String personId) {
        return REG.containsKey(personId);
    }

    public static String assignedGroup(String personId) {
        return REG.get(personId);
    }

    public static void assign(String personId, String groupName) {
        REG.put(personId, groupName);
    }

    public static void unassign(String personId) {
        REG.remove(personId);
    }

    public static void clear() {
        REG.clear();
    }
}
// File: src/i2jp/oop/GroupRegistry.java
// Author: Student & Group Manager Implementation
package i2jp.oop;

import java.util.HashMap;
import java.util.Map;

public class GroupRegistry {
    // Maps student/person ID → group name
    private static final Map<String, String> REG = new HashMap<>();

    /** Checks whether the student is already assigned to any group. */
    public static boolean isAssigned(String personId) {
        return REG.containsKey(personId);
    }

    /**
     * Returns the name of the group the student currently belongs to, or null if
     * none.
     */
    public static String assignedGroup(String personId) {
        return REG.get(personId);
    }

    /** Registers a student as belonging to a given group. */
    public static void assign(String personId, String groupName) {
        REG.put(personId, groupName);
    }

    /** Removes a student from the registry, freeing their assignment. */
    public static void unassign(String personId) {
        REG.remove(personId);
    }

    /** Clears all assignments (useful for testing or resetting). */
    public static void clear() {
        REG.clear();
    }
}
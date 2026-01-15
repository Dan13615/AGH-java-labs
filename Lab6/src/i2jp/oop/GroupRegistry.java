// File: src/i2jp/oop/GroupRegistry.java
// GroupRegistry with Log4j 2 logging integration
package i2jp.oop;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GroupRegistry {
  private static final Logger log = LogManager.getLogger(GroupRegistry.class);
  
  // Maps student/person ID → group name
  private static final Map<String, String> REG = new HashMap<>();

  /** Checks whether the student is already assigned to any group. */
  public static boolean isAssigned(String personId) {
    boolean assigned = REG.containsKey(personId);
    log.trace("Checking assignment for personId={}: {}", personId, assigned);
    return assigned;
  }

  /** Returns the name of the group the student currently belongs to, or null if none. */
  public static String assignedGroup(String personId) {
    String group = REG.get(personId);
    log.trace("Getting assigned group for personId={}: {}", personId, group);
    return group;
  }

  /** Registers a student as belonging to a given group. */
  public static void assign(String personId, String groupName) {
    REG.put(personId, groupName);
    log.info("Assigned personId={} to group={}", personId, groupName);
  }

  /** Removes a student from the registry, freeing their assignment. */
  public static void unassign(String personId) {
    String removed = REG.remove(personId);
    if (removed != null) {
      log.debug("Unassigned personId={} from group={}", personId, removed);
    } else {
      log.warn("Attempt to unassign non-registered personId={}", personId);
    }
  }
  
  /** Clears all assignments (useful for testing or resetting). */
  public static void clear() {
    int size = REG.size();
    REG.clear();
    log.info("Cleared all {} assignments from registry", size);
  }
}
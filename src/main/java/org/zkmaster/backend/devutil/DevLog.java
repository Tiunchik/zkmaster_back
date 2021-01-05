package org.zkmaster.backend.devutil;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.utils.ZKNodes;

import java.util.List;
import java.util.Map;

/**
 * Simple custom log for dev environment.
 */
public class DevLog {

    /**
     * @param place place marker, info about class, method, or something other.
     * @param info  your msg.
     */
    public static void print(String place, String info) {
        System.err.println("DEV " + place + " :: " + info);
    }

    /**
     * @param place place marker, info about class, method, or something other.
     * @param info  value description, usually variable name.
     * @param val   value || variable for logging|debugging.
     */
    public static void print(String place, String info, Object val) {
        if (val instanceof String) {
            System.err.println("DEV " + place + " :: " + info + "==\"" + val + '\"');
        } else if (val instanceof List) {
            printList(place, info, (List<Object>) val);
        } else if (val instanceof Map) {
            printMap(place, info, (Map<Object, Object>) val);
        } else if (val instanceof ZKNode) {
            printZKNode(place, info, (ZKNode) val);
        } else {
            System.err.println("DEV " + place + " :: " + info + "==" + val);
        }
    }

    private static void printList(String place, String info, List<Object> list) {
        if (!list.isEmpty()) {
            if (list.get(0) instanceof String) {
                list.forEach(each -> System.err.println("DEV " + place + " :: " + info
                        + "== key:\"" + each + '\"'
                ));
            } else {
                list.forEach(each -> System.err.println("DEV " + place + " :: " + info
                        + "== key:" + each
                ));
            }
        }
    }

    private static void printMap(String place, String info, Map<Object, Object> map) {
        for (var each : map.entrySet()) {
            if (each.getKey() instanceof String) {
                System.err.println("DEV " + place + " :: " + info
                        + "== key:\"" + each.getKey() + '\"'
                        + " val: " + each.getValue()
                );
            } else {
                System.err.println("DEV " + place + " :: " + info
                        + "== key:\"" + each.getKey() + '\"'
                        + " val: \"" + each.getValue() + '\"'
                );
            }
        }
    }

    private static void printZKNode(String place, String info, ZKNode node) {
        System.err.println("DEV " + place + " :: " + info);
        ZKNodes.printNode(node, System.err::println);
    }

}

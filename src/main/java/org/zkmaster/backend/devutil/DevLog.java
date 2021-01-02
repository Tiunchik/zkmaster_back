package org.zkmaster.backend.devutil;

import java.util.List;
import java.util.Map;

public class DevLog {

    public static void print(String place, String info) {
        System.err.println("DEV " + place + " :: " + info);
    }

    public static void print(String place, String info, Object val) {
        if (val instanceof String) {
            System.err.println("DEV " + place + " :: " + info + "==\"" + val + '\"');
        } else if (val instanceof List) {
            printList(place, info, (List<Object>) val);
        } else if (val instanceof Map) {
            printMap(place, info, (Map<Object, Object>) val);
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

}

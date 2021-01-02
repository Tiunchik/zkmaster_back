package org.zkmaster.backend.devutil;

import java.util.Map;

public class DevLog {

    public static void print(String place, String info, Object val) {
        if (val instanceof String) {
            System.err.println("DEV " + place + " :: " + info + "==\"" + val + '\"');
        }if (val instanceof Map) {
          printMap(place, info, (Map<Object, Object>) val);
        } else {
            System.err.println("DEV " + place + " :: " + info + "==" + val);
        }
    }

    public static void printMap(String place, String info, Map<Object, Object> map) {
        for (var each : map.entrySet()) {
            if (each.getKey() instanceof String) {
                System.err.println("DEV " + place + " :: " + info
                        + "== key:\"" + each.getKey() + '\"'
                        + " val: \"" + each.getValue() + '\"'
                );
            } else {
                System.err.println("DEV " + place + " :: " + info + "==" + map);
            }
        }
    }


    public static void print(String place, String info) {
        System.err.println("DEV " + place + " :: " + info);
    }

}

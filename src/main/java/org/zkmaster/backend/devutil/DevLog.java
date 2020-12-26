package org.zkmaster.backend.devutil;

public class DevLog {

    public static void print(String place, String info, Object val) {
        if (val instanceof String) {
            System.err.println("DEV " + place + " :: " + info + "==\"" + val + '\"');
        } else {
            System.err.println("DEV " + place + " :: " + info + "==" + val);
        }
    }
    public static void print(String place, String info) {
        System.err.println("DEV " + place + " :: " + info);
    }

}

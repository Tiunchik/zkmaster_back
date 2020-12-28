package org.zkmaster.backend.zexprun;

import org.zkmaster.backend.devutil.DevLog;

public class ExpRun2 {

    public static void main(String[] args) {
        String src = "^1^1-1^1-1-1";

        var rsl = src.replace('^', '/');
        DevLog.print("TEST", "rsl", rsl);
    }
}

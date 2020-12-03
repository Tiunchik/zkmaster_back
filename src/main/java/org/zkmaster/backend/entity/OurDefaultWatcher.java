package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * Just Comunizden from:
 * @see org.apache.zookeeper.ZooKeeperMain.MyWatcher
 * * It red cause, it's private class.
 */
public class OurDefaultWatcher implements Watcher {
//    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    @Override
    public void process(WatchedEvent event) {
//        ZooKeeperMain.printMessage("WATCHER::");
//        ZooKeeperMain.printMessage(event.toString());
        innerPrint("WATCHER::");
        innerPrint("event.toString(): " + event.toString());
    }

    private void innerPrint(String msg) {
        System.out.println("\n" + msg);
    }


}

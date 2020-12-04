package org.zkmaster.adapters;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;


public class WatcherImplAdapter implements Watcher {



    @Override
    public void process(WatchedEvent event) {

        if (event.getState() == Event.KeeperState.Expired ||
                event.getState() == Event.KeeperState.Disconnected) {

        }

    }

}

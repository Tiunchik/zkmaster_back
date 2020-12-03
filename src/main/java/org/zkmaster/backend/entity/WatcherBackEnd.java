package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

/**
 * This class must to process all events from server.
 * Process - do correct logic things, do correct code.
 */
public class WatcherBackEnd implements Watcher {
//    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);
    private final String hostUrl;

    public WatcherBackEnd(String hostUrl) {
        this.hostUrl = hostUrl;
    }


    @Override
    public void process(WatchedEvent event) {
        System.out.println("BackEnd Watcher - work!");
        System.out.println("watcher.hostUrl = " + hostUrl);

        if (event.getState() == Event.KeeperState.Expired ||
                event.getState() == Event.KeeperState.Disconnected) {

        }
//        if (event.getType() == Event.EventType.NodeCreated) {
//            context.publishEvent(new MyEvent(event.getPath()));
//        }
    }




}

package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

/**
 * This class must to process all events from server.
 * Process - do correct logic things, do correct code.
 *
 * TODO: доделать - через Map, как завещал Пётр - I didn't start it.
 */
public class ZKWatcherDefault implements Watcher {

    @Autowired
    private ApplicationContext context;

    private String hostUrl;



    @Override
    public void process(WatchedEvent event) {
        System.out.println("BackEnd Watcher - work!");
        System.out.println("watcher.hostUrl = " + hostUrl);
//        context.publishEvent(new MyEvent(watcher));

//        if (event.getState() == Event.KeeperState.Expired ||
//                event.getState() == Event.KeeperState.Disconnected) {
//
//        }
//        if (event.getType() == Event.EventType.NodeCreated) {
//                context.publishEvent(new MyEvent(event.getPath()));
//        }
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    public String getHostUrl() {
        return hostUrl;
    }
}

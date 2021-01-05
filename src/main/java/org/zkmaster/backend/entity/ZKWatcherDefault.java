package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * This class must to process all events from server.
 * Process - do correct logic things, do correct code.
 * <p>
 * TODO - доделать - через Map, как завещал Пётр - I didn't start it.
 */
@Component("watcherDefault")
@Scope(SCOPE_PROTOTYPE)
public class ZKWatcherDefault implements Watcher {

    private final ApplicationContext context;

    /**
     * This watcher catch all events from real server by this host-connection.
     */
    private String host;

    @Autowired
    public ZKWatcherDefault(ApplicationContext context) {
        this.context = context;
    }

    /**
     * TODO process all events from real server.
     *
     * <p>Need to processes {@link WatchedEvent#keeperState}:
     * {@link Watcher.Event.KeeperState#Disconnected}
     * {@link Watcher.Event.KeeperState#SyncConnected}
     * - connection is created success.
     * {@link Watcher.Event.KeeperState#Expired} - ?????????????????????
     * TODO skazano 4to: kidaetsja, kogda u classtera eta Sessija prosro4ena ↑↑↑(zna4ki: ↓)
     * TODO zna4ki: https://www.alt-codes.net/
     *
     *
     * <p>Need to processes {@link WatchedEvent#eventType}:
     * {@link Watcher.Event.EventType#None}
     * - nodes weren't changed. (usual for connect/disconnect events)
     * {@link Watcher.Event.EventType#NodeCreated}
     * {@link Watcher.Event.EventType#NodeDeleted}
     * {@link Watcher.Event.EventType#NodeDataChanged}
     * {@link Watcher.Event.EventType#NodeChildrenChanged}
     *
     * <p> Don't need to process, cause it isn't "server state change"
     * {@link Watcher.Event.EventType#DataWatchRemoved} - Don't need to process
     * {@link Watcher.Event.EventType#ChildWatchRemoved} - Don't need to process
     * {@link Watcher.Event.EventType#PersistentWatchRemoved} - Don't need to process
     * <p>
     * ! path won't be null if event touch any node.
     *
     * @param event - server event.
     */
    @Override
    public void process(WatchedEvent event) {
        System.out.println("ZKWatcherDefault :: Event watched:");
        System.out.println("ZKWatcherDefault :: host = " + host);

        if (event.getState() == Event.KeeperState.SyncConnected
                && event.getType() == Event.EventType.None
                && event.getPath() == null) {

            System.out.println("ZKWatcherDefault :: event = Connection created.");
            System.out.println("ZKWatcherDefault :: event informer.");

        } else if (event.getState() == Event.KeeperState.SyncConnected
                && event.getType() == Event.EventType.NodeDataChanged
                && event.getPath().equals("/")) {

            System.out.println("ZKWatcherDefault :: event = " + event);
            System.out.println("ZKWatcherDefault :: event published.");
            context.publishEvent(new EventServerClose(host));

        } else if (event.getState() == Event.KeeperState.Disconnected) {

            System.out.println("ZKWatcherDefault :: event = " + event);
            System.out.println("ZKWatcherDefault :: event published.");
            context.publishEvent(new EventServerClose(host));

//        } else if (event.getType() == Event.EventType.NodeCreated
//                || event.getType() == Event.EventType.NodeDeleted
//                || event.getType() == Event.EventType.NodeDataChanged
//                || event.getType() == Event.EventType.NodeChildrenChanged) {
        } else if (
//                event.getType() == Event.EventType.NodeCreated
//                || event.getType() == Event.EventType.NodeDeleted
//                || event.getType() == Event.EventType.NodeDataChanged
//                ||
                event.getType() == Event.EventType.NodeChildrenChanged
        ) {
            System.out.println("ZKWatcherDefault :: event = " + event);
            System.out.println("ZKWatcherDefault :: event published.");
            context.publishEvent(new EventServerStateChange(host));
        } else {
            System.out.println("ZKWatcherDefault :: event = " + event);
            System.out.println("ZKWatcherDefault :: event ignored.");
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

}

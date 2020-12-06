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
 * TODO: доделать - через Map, как завещал Пётр - I didn't start it.
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
     * <p>TODO - Maksja, skazi. - eti 6tuki nuzno obrabativatj? Vrode kak eto signal 4to watcher ubrali gde-to.
     * {@link Watcher.Event.EventType#DataWatchRemoved} - ?????????????????????
     * {@link Watcher.Event.EventType#ChildWatchRemoved} - ?????????????????????
     * {@link Watcher.Event.EventType#PersistentWatchRemoved} - ?????????????????????
     *
     * ! path won't be null if event touch any node.
     *
     * @param event - server event.
     */
    @Override
    public void process(WatchedEvent event) {
        System.out.println("BackEnd Watcher - work!");
        System.out.println("watcher.hostUrl = " + host);
        System.out.println("watcher.event = " + event);

        if (event.getState() == Event.KeeperState.Disconnected) {
            context.publishEvent(new EventServerClose(host));
        } else if (event.getType() == Event.EventType.NodeCreated
                || event.getType() == Event.EventType.NodeDeleted
                || event.getType() == Event.EventType.NodeDataChanged
                || event.getType() == Event.EventType.NodeChildrenChanged) {
            context.publishEvent(new EventServerStateChange(host));
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

}

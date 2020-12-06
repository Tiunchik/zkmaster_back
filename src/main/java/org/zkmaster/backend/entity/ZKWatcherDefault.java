package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

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
    private String hostUrl;

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
     * {@link Watcher.Event.KeeperState#Expired} - ?????????????????????
     * TODO skazano 4to: kidaetsja, kogda u classtera eta Sessija prosro4ena ↑↑↑(zna4ki: ↓)
     * TODO zna4ki: https://www.alt-codes.net/
     *
     *
     * <p>Need to processes {@link WatchedEvent#eventType}:
     * {@link Watcher.Event.EventType#None} - ?????????????????????
     * {@link Watcher.Event.EventType#NodeCreated}
     * {@link Watcher.Event.EventType#NodeDeleted}
     * {@link Watcher.Event.EventType#NodeDataChanged}
     * {@link Watcher.Event.EventType#NodeChildrenChanged}
     *
     *
     * <p>TODO - Maksja, skazi. - eti 6tuki nuzno obrabativatj? Vrode kak eto signal 4to watcher ubrali gde-to.
     * {@link Watcher.Event.EventType#DataWatchRemoved} - ?????????????????????
     * {@link Watcher.Event.EventType#ChildWatchRemoved} - ?????????????????????
     * {@link Watcher.Event.EventType#PersistentWatchRemoved} - ?????????????????????
     *
     * @param event - server event.
     */
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

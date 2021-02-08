package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;

import java.util.StringJoiner;
import java.util.function.Predicate;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * This class must to process all events from server.
 * Process - do correct logic things, do correct code.
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
     * <p>Need to processes {@link WatchedEvent#keeperState}:
     * {@link Watcher.Event.KeeperState#Disconnected} - lost connection with real-server.
     * {@link Watcher.Event.KeeperState#Closed} - then method invoke:
     * {@link ZooKeeper#close()}
     * {@link Watcher.Event.KeeperState#SyncConnected}
     * - connection is created success.
     * {@link Watcher.Event.KeeperState#Expired} - ?????????????????????
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
    @Log
    @Override
    public void process(WatchedEvent event) {
        var watcherMsg = new StringJoiner(System.lineSeparator());
        watcherMsg.add(EVENT_WATCHED);
        watcherMsg.add(EVENT_HOST + host);
        
        if (CONNECTED.test(event)) {
            watcherMsg.add(MSG_CONNECTION_CREATED);
            watcherMsg.add(MSG_PROCESSING_IGNORED + "(inform event)");
            
        } else if (NODE_CHANGED.test(event)) {
            publishEvent(event, new EventServerStateChange(host), watcherMsg);
            
        } else if (NODE_CHILDREN_CHANGED.test(event)) {
            publishEvent(event, new EventServerStateChange(host), watcherMsg);
            
        } else if (DISCONNECTED.test(event)) {
            publishEvent(event, new EventServerClose(host), watcherMsg);
            
        } else {
            watcherMsg.add(EVENT_INFO + event);
            watcherMsg.add(MSG_PROCESSING_IGNORED);
        }
        System.out.println(watcherMsg.toString());
    }
    
    private void publishEvent(WatchedEvent comeEvent, ApplicationEvent publishEvent, StringJoiner watcherMsg) {
        watcherMsg.add(EVENT_INFO + comeEvent);
        watcherMsg.add(MSG_PROCESSING_PUBLISHED);
        context.publishEvent(publishEvent);
    }
    
    public String getHost() {
        return host;
    }
    
    /* Processing Events */
    
    private static final Predicate<WatchedEvent> CONNECTED = (event) ->
            event.getState() == Event.KeeperState.SyncConnected
                    && event.getType() == Event.EventType.None;
    
    private static final Predicate<WatchedEvent> NODE_CHANGED = (event) ->
            event.getState() == Event.KeeperState.SyncConnected
                    && event.getType() == Event.EventType.NodeDataChanged;
    
    private static final Predicate<WatchedEvent> NODE_CHILDREN_CHANGED = (event) ->
            event.getState() == Event.KeeperState.SyncConnected
                    && event.getType() == Event.EventType.NodeChildrenChanged;
    
    private static final Predicate<WatchedEvent> DISCONNECTED = (event) ->
            event.getState() == Event.KeeperState.Disconnected
                    && event.getType() == Event.EventType.None;
    
    public void setHost(String host) {
        this.host = host;
    }
    
    /* msg && Logger Constants */
    
    private static final String LOG_PREFIX = "ZKWatcherDefault :: ";
    private static final String EVENT_WATCHED = LOG_PREFIX + "Event watched:";
    private static final String EVENT_HOST = LOG_PREFIX + "host = ";
    private static final String EVENT_INFO = LOG_PREFIX + "event = ";
    
    private static final String MSG_CONNECTION_CREATED = EVENT_INFO + "Connection created";
    private static final String MSG_PROCESSING_PUBLISHED = LOG_PREFIX + "processing = event published";
    private static final String MSG_PROCESSING_IGNORED = LOG_PREFIX + "processing = event ignored";
    
}

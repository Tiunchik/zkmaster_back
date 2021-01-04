package org.zkmaster.backend.mocks;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.events.EventServerClose;
import org.zkmaster.backend.events.EventServerStateChange;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component("watcherFake")
@Scope(SCOPE_PROTOTYPE)
public class ZKWatcherFake implements Watcher {

    private final ApplicationContext context;
    private String host;

    @Autowired
    public ZKWatcherFake(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void process(WatchedEvent event) {
        System.out.println("ZKWatcherFake :: Event watched:");
        System.out.println("ZKWatcherFake :: host = " + host);

        if (event.getState() == Event.KeeperState.SyncConnected
                && event.getType() == Event.EventType.None
                && event.getPath() == null) {

            System.out.println("ZKWatcherFake :: event = Connection created.");
            System.out.println("ZKWatcherFake :: event informer.");

        } else if (event.getState() == Event.KeeperState.SyncConnected
                && event.getType() == Event.EventType.NodeDataChanged
                && event.getPath().equals("/")) {

            System.out.println("ZKWatcherFake :: event = " + event);
            System.out.println("ZKWatcherFake :: event published.");
            context.publishEvent(new EventServerClose(host));

        } else if (event.getState() == Event.KeeperState.Disconnected) {
            System.out.println("ZKWatcherFake :: event = " + event);
            System.out.println("ZKWatcherFake :: event published.");
            context.publishEvent(new EventServerClose(host));

        } else if (event.getType() == Event.EventType.NodeChildrenChanged) {
            System.out.println("ZKWatcherFake :: event = " + event);
            System.out.println("ZKWatcherFake :: event published.");
            context.publishEvent(new EventServerStateChange(host));

        } else {
            System.out.println("ZKWatcherFake :: event = " + event);
            System.out.println("ZKWatcherFake :: event ignored.");
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }
}

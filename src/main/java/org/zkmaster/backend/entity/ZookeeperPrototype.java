package org.zkmaster.backend.entity;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Component
@Scope(SCOPE_PROTOTYPE)
public class ZookeeperPrototype {

    @Autowired
    ApplicationContext context;
    private ZooKeeper zoo;
    private Watcher watcher = new MyWatcher();
    public ZooKeeper getConnection(String host) {

        return zoo;
    }

    private class MyWatcher implements Watcher {

        @Override
        public void process(WatchedEvent event) {
            if (event.getState() == Event.KeeperState.Expired ||
                    event.getState() == Event.KeeperState.Disconnected) {

            }
            if (event.getType() == Event.EventType.NodeCreated) {
//                context.publishEvent(new MyEvent(event.getPath()));
            }
        }
    }

}

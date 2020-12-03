package org.zkmaster.events;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.zkmaster.backend.entity.WatcherBackEnd;

@Component
public class ThAndGo {

    @Autowired
    private ApplicationContext context;

    @Value("#{@zooConfig != null ? @zooConfig : null}")
    private ZooKeeper zooKeeper;

    public void addWatcherToDB(WatcherBackEnd watcher) {
        context.publishEvent(new MyEvent(watcher));
    }

}

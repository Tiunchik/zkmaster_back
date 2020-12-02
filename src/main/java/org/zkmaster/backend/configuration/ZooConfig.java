package org.zkmaster.backend.configuration;

import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkmaster.backend.entity.OurDefaultWatcher;

import java.io.IOException;

@Configuration
public class ZooConfig {

    @Bean("zoo")
    public ZooKeeper init(Watcher stubWatcher) {
        try {
            return new ZooKeeper("localhost:2181", 4000, stubWatcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean("stubWatcher")
    public Watcher createWatcher() {
        return new OurDefaultWatcher();
    }

}

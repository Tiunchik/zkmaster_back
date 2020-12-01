package org.zkmaster.backend.configuration;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.IOException;

@Configuration
public class ZooConfig {

    @Bean(name = "zoo")
    public ZooKeeper init(Watcher watcher) {
        try {
            return new ZooKeeper("localhost:2181", 4000, watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public Watcher createWatcher() {
        return new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                System.out.println(event.toString());
            }
        };
    }


}

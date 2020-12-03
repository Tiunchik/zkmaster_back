package org.zkmaster.backend.configuration;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zkmaster.backend.entity.WatcherBackEnd;
import org.zkmaster.backend.entity.ZKFactory;
import org.zkmaster.backend.repositories.ZKControllerContext;

import java.io.IOException;

@Configuration
public class ZooConfig {

    @Bean("zoo")
    public ZooKeeper init() {
        ZooKeeper rsl = null;
        try {
            String hostUrl = "localhost:2181";
            rsl = new ZooKeeper(hostUrl, 4000, new WatcherBackEnd(hostUrl));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    @Bean
    public ZKControllerContext zkControllerHolder() {
        return new ZKControllerContext();
    }

    @Bean
    public ZKFactory zkFactory(ZKControllerContext zkControllerContext) {
        return new ZKFactory(zkControllerContext);
    }

//    @Bean("stubWatcher")
//    public Watcher createWatcher() {
//        return new WatcherBackEnd(hostUrl);
//    }

}

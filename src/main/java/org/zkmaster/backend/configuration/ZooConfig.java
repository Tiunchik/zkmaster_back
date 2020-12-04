package org.zkmaster.backend.configuration;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.zkmaster.backend.entity.ZKWatcherDefault;

import java.io.IOException;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * TODO: мб удалить
 */
@Configuration
public class ZooConfig {

    @Bean
    @Scope(SCOPE_PROTOTYPE)
    public ZKWatcherDefault watcherDefault() {
        return new ZKWatcherDefault();
    }

    @Bean("zoo")
    public ZooKeeper init() {
        String initHostUrl = "localhost:2181";

        ZooKeeper rsl = null;
        try {
            ZKWatcherDefault watcher = watcherDefault();
            watcher.setHostUrl(initHostUrl);
            rsl = new ZooKeeper(initHostUrl, 4000, watcher);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

}

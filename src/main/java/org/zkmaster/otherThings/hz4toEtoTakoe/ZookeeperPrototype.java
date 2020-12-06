package org.zkmaster.otherThings.hz4toEtoTakoe;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * Ja zh kak v nine6nej sborke eto mozno primenitj,
 * no delitetj aj eto toze ne ho4ju, tut estj interesnie ideji.
 */
@Component
@Scope(SCOPE_PROTOTYPE)
@Deprecated
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

        }
    }

}

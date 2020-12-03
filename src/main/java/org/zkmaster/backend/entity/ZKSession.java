package org.zkmaster.backend.entity;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class ZKSession {
    private static final Logger LOG = LoggerFactory.getLogger(ZKSession.class);
    private final ZooKeeper zoo;

    public ZKSession(ZooKeeper zoo) {
        this.zoo = zoo;
    }

    public void create(String path, String value) {
        try {
            zoo.create(path,
                    value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            LOG.error("Something Wrong! Check it.");
            e.printStackTrace();
        }
    }

    /**
     * Default realization. Don't use Watcher and State.
     *
     * @param path-
     * @return String value by nude.
     */
    public String read(String path) {
        String rsl = null;
        try {
            rsl = new String(zoo.getData(path, false, null), StandardCharsets.UTF_8);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    public void setData(String path, String value) {
//        zoo.create()
    }

    /**
     * Default realization. Don't use Watcher and State.
     *
     * @param path-
     * @return String value by nude.
     */
    public List<String> getChildren(String path) throws KeeperException, InterruptedException {
        return zoo.getChildren(path, false, null);
    }
}

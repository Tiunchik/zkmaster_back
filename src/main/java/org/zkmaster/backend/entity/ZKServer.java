package org.zkmaster.backend.entity;

import org.apache.zookeeper.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Warp default ZooKeeper API into comfortable API. We call it "facade" of real server.
 * <p>
 * If you need more API from original ZooKeeper,
 * decorate it in this class.
 */
public class ZKServer implements AutoCloseable {
    private final String hostUrl;
    private final ZooKeeper zoo;

    /**
     * Regular constructor for facade of real server.
     *
     * @param hostUrl        - URL of real ZooKeeper server.
     * @param sessionTimeout - timeout for query to real server.
     * @param watcher        - catch all event(changes) from real server.
     * @throws IOException - Fail to create connection with real ZooKeeper server!
     */
    public ZKServer(String hostUrl, int sessionTimeout, Watcher watcher) throws IOException {
        this.hostUrl = hostUrl;
        this.zoo = new ZooKeeper(hostUrl, sessionTimeout, watcher);
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path  -
     * @param value -
     */
    public void create(String path, String value) {
        try {
            zoo.create(path,
                    value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path -
     * @return String value by nude.
     */
    public String read(String path) {
        String rsl = null;
        try {
            rsl = new String(zoo.getData(path, false, null), StandardCharsets.UTF_8);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * !!! If the given version is -1, it matches any node's versions.
     *
     * @param path  -
     * @param value -
     * @return - update complete success or not.
     */
    public boolean setData(String path, String value) {
        boolean rsl = true;
        try {
            zoo.setData(path, value.getBytes(), -1);
        } catch (KeeperException | InterruptedException e) {
            rsl = false;
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * !!! If the given version is -1, it matches any node's versions.
     *
     * @param path -
     * @return - delete complete success or not.
     */
    public boolean delete(String path) {
        boolean rsl = true;
        try {
            zoo.delete(path, -1);
        } catch (InterruptedException | KeeperException e) {
            rsl = false;
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * Default wrap, doesn't use watch and State.
     *
     * @param path -
     * @return children names OR null.
     */
    public List<String> getChildren(String path) {
        List<String> rsl = null;
        try {
            rsl = zoo.getChildren(path, false, null);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    @Override
    public void close() throws Exception {
        zoo.close();
    }

}

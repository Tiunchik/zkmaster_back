package org.zkmaster.backend.entity;

import org.apache.zookeeper.*;
import org.zkmaster.backend.exceptions.NodeExistsException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Warp default {@link ZooKeeper} API into comfortable API. We call it "facade" of real server.
 * <p>
 * If you need more API from original {@link ZooKeeper},
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
     * TODO : сделать catch на KeeperException.NodeExistsException StackTrace:
     * TODO : org.apache.zookeeper.KeeperException$NodeExistsException: KeeperErrorCode = NodeExists for /l1
     * TODO : 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:126)
     * TODO : 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:54)
     * TODO : 	at org.apache.zookeeper.ZooKeeper.create(ZooKeeper.java:1733)
     * TODO : 	at org.zkmaster.backend.entity.ZKServer.create(ZKServer.java:40)
     *
     *
     * Default wrap. Don't use Watcher and State.
     *
     * @param path  -
     * @param value -
     */
    public void create(String path, String value) throws NodeExistsException {
        try {
            zoo.create(path,
                    value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException.NodeExistsException e) {
            throw new NodeExistsException(path);
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
            byte[] temp = zoo.getData(path, true, null);
            if (temp != null) {
                rsl = new String(temp, StandardCharsets.UTF_8);
            }
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * TODO : сделать catch на KeeperException.KeeperErrorCode  StackTrace:
     * TODO : org.apache.zookeeper.KeeperException$NoNodeException: KeeperErrorCode = NoNode for /l1valuedfgdff
     * TODO : 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:126)
     * TODO : 	at org.apache.zookeeper.KeeperException.create(KeeperException.java:54)
     * TODO : 	at org.apache.zookeeper.ZooKeeper.create(ZooKeeper.java:1733)
     * TODO : 	at org.zkmaster.backend.entity.ZKServer.create(ZKServer.java:40)
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
            rsl = zoo.getChildren(path, true, null);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    public ZKTransaction transaction() {
        return new ZKTransaction(zoo.transaction());
    }

    public String getHostUrl() {
        return hostUrl;
    }

    @Override
    public void close() throws Exception {
        zoo.close();
    }

}

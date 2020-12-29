package org.zkmaster.backend.entity;

import org.apache.zookeeper.*;
import org.zkmaster.backend.exceptions.node.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

/**
 * Warp default {@link ZooKeeper} API into comfortable API. We call it "facade" of real server.
 * <p>
 * If you need more API from original {@link ZooKeeper},
 * decorate it in this class.
 */
public class Host implements AutoCloseable {
    private final String hostAddress;
    private final ZooKeeper zoo;

    /**
     * Regular constructor for facade of real server.
     *
     * @param hostAddress    - URL of real ZooKeeper server.
     * @param sessionTimeout - timeout for query to real server.
     * @param watcher        - catch all event(changes) from real server.
     * @throws IOException - Fail to create connection with real ZooKeeper server!
     */
    public Host(String hostAddress, int sessionTimeout, Watcher watcher) throws IOException {
        this.hostAddress = hostAddress;
        this.zoo = new ZooKeeper(hostAddress, sessionTimeout, watcher);
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path  -
     * @param value -
     */
    public boolean create(String path, String value) throws NodeExistsException, NodeCreateException {
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
            throw new NodeCreateException(hostAddress, path, value);
        }
        return true;
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path -
     * @return String value by nude.
     */
    public String read(String path) throws NodeReadException {
        String rsl = null;
        try {
            byte[] temp = zoo.getData(path, true, null);
            if (temp != null) {
                rsl = new String(temp, StandardCharsets.UTF_8);
            }
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
            throw new NodeReadException(hostAddress, path);
        }
        return rsl;
    }

    /**
     * Update node value by params.
     * * If the given version is "-1", it matches any node's versions.
     *
     * @param path  Node path.
     * @param value new Node name.
     * @return Update OR throw {@link NodeSaveException}.
     * @throws NodeSaveException throw if something went wrong.
     */
    public boolean setData(String path, String value) throws NodeSaveException {
        try {
            zoo.setData(path, value.getBytes(), -1);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
            throw new NodeSaveException(hostAddress, path, value);
        }
        return true;
    }

    /**
     * Delete node by {@param path}.
     * * If the given version is "-1", it matches any node's versions.
     *
     * @param path Node path.
     * @return Delete OR throw {@link NodeDeleteException}.
     * @throws NodeDeleteException throw if something went wrong.
     */
    public boolean delete(String path) throws NodeDeleteException {
        try {
            zoo.delete(path, -1);
        } catch (InterruptedException | KeeperException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
            throw new NodeDeleteException(hostAddress, path);
        }
        return true;
    }

    /**
     * Default wrap, doesn't use watch and State.
     *
     * @param path -
     * @return children names OR null.
     */
    public List<String> getChildren(String path) {
        List<String> rsl = new LinkedList<>();
        try {
            rsl = zoo.getChildren(path, true, null);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Unexpected ZK API Exception! Check the case.");
            e.printStackTrace();
        }
        return rsl;
    }

    public ZKTransaction transaction() {
        return new ZKTransaction(zoo.transaction());
    }

    public String getHostAddress() {
        return hostAddress;
    }

    @Override
    public void close() throws Exception {
        zoo.close();
    }

}

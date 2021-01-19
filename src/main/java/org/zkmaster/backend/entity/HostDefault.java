package org.zkmaster.backend.entity;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.exceptions.HostCloseException;
import org.zkmaster.backend.exceptions.node.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Default warp {@link ZooKeeper} API into comfortable API.
 * <p>
 * If you need more API from original {@link ZooKeeper},
 * decorate it in this class.
 */
public class HostDefault implements Host {
    private final String hostAddress;
    private final ZooKeeper zoo;

    /**
     * Regular constructor for wrap of code class(ZooKeeper real-server).
     *
     * @param hostAddress    - URL of real ZooKeeper server.
     * @param sessionTimeout - timeout for query to real server.
     * @param watcher        - catch all event(changes) from real server.
     * @throws IOException - Fail to create connection with real ZooKeeper server!
     */
    public HostDefault(String hostAddress, int sessionTimeout, Watcher watcher) throws IOException {
        this.hostAddress = hostAddress;
        this.zoo = new ZooKeeper(hostAddress, sessionTimeout, watcher);
    }

    /**
     * @implSpec docs for {@link ZooKeeper#create(String, byte[], List, CreateMode)}:
     * params:
     * String : path - Node path.
     * byte[] : String as bytes
     * List : acl{@link org.apache.zookeeper.data.ACL}
     * CreateMode : createMode{@link CreateMode}
     */
    @Override
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
     * @implSpec docs for {@link ZooKeeper#getData(String, boolean, Stat)}:
     * params:
     * String : path - Node path for read.
     * boolean : watch - touch watchers for this server-action.
     * Stat : state - could be null. Need for save full info of Node state.
     * <p>
     * return: encoded String as bytes. Ways to decode:
     * {@code String value = new String(bytes, StandardCharsets.UTF_8); }
     * IMPORTANT: Workable StandardCharsets (only):
     * StandardCharsets.UTF_8
     * StandardCharsets.US_ASCII
     * StandardCharsets.ISO_8859_1
     */
    @Override
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
     * @throws NodeSaveException throw if something went wrong.
     * @implSpec docs for {@link ZooKeeper#setData(String, byte[], int)}:
     * String : path - Node path for set new value.
     * byte[] : data - String as bytes.
     * int : version - Node state version.
     * * If the given version is "-1", it matches any Node versions.
     */
    @Override
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
     * @implSpec docs for {@link ZooKeeper#delete(String, int)}:
     * String : path - Node path for set new value.
     * int : version - Node state version.
     * * If the given version is "-1", it matches any Node versions.
     */
    @Override
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
     * @implSpec docs for {@link ZooKeeper#getChildren(String, boolean)}:
     * String : path - Node path for read.
     * boolean : watch - touch watchers for this server-action.
     * Stat : state - could be null. Need for save full info of Node state.
     */
    @Override
    public List<String> getChildrenNames(String path) throws NodeReadException {
        List<String> rsl;
        try {
            rsl = zoo.getChildren(path, true, null);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Unexpected ZK API Exception! Check the case.");
            e.printStackTrace();
            throw new NodeReadException(hostAddress, path);
        }
        return rsl;
    }

    @Override
    public List<String> getChildrenPaths(String path) throws NodeReadException {
        return getChildrenNames(path).stream()
                .map(childName -> ("/".equals(path))   // is it root
                        ? path + childName             // true
                        : path + "/" + childName       // false
                ).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren(String path) throws NodeReadException {
        return !this.getChildrenNames(path).isEmpty();
    }

    @Override
    public ZKNode readNode(String path) throws NodeReadException {
        ZKNode rsl = new ZKNode();
        try {
            rsl.setPath(path);
            rsl.setValue(read(path));
            rsl.setName(ZKNodes.nameFromPath(path));
        } catch (NodeReadException e) {
            System.err.println("Something Wrong! Unexpected ZK API Exception! Check the case.");
            e.printStackTrace();
            throw new NodeReadException(hostAddress, path);
        }
        return rsl;
    }

    @Override
    public ZKTransaction transaction() {
        return new ZKTransactionDefault(zoo.transaction());
    }

    @Override
    public String getHostAddress() {
        return hostAddress;
    }

    @Override
    public void close() throws HostCloseException {
        try {
            zoo.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new HostCloseException(hostAddress);
        }
    }

}

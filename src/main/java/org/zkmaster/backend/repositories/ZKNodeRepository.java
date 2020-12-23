package org.zkmaster.backend.repositories;

import org.apache.zookeeper.KeeperException;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.NodeExistsException;

/**
 * CRUD interface for {@link ZKNode}.
 * ZKNodeRepository == connection with real server.
 */
public interface ZKNodeRepository {

    /**
     * !!! Maybe it would better if that it will accept {@link ZKNode}???
     *
     */
    boolean create(String path, String value) throws NodeExistsException;

//    /**
//     * Simple "Node-value" from real server, packed in {@link ZKNode}.
//     *
//     * @return {@link ZKNode} without children, only info OR null.
//     */
//    @Deprecated(since = "since front == null")
//    ZKNode getSimpleNode(String path);

    /**
     * Full "host-value" from real server, packed in {@link ZKNode}.
     *
     * @return {@link ZKNode} with all children(sub-nodes) OR null.
     */
    ZKNode getHostValue();

    /**
     * Selector of inner {@link ZKNode}. Tree-width walk by List.
     *
     * @param root - start node.
     * @param path - absolute path of searching {@link ZKNode}.
     * @return result || null, if searching {@link ZKNode} doesn't found.
     */
    ZKNode getSubZKNodeByFullPath(ZKNode root, String path);

    /**
     * Update node value by {@param path}.
     * @param path path
     * @param value -
     * @return -
     */
    boolean set(String path, String value);

    /**
     * Delete "Node" in real server.
     *
     * @param path Node path.
     * @return Delete success OR not.
     */
    boolean delete(String path);

    /**
     * Rename Node in real server.
     *
     * @param path Node path.
     * @param value New Node name.
     * @return Rename success OR not.
     */
    boolean rename(String path, String value, ZKNode currentHostValue) throws KeeperException, InterruptedException;
}

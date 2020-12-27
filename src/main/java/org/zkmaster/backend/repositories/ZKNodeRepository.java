package org.zkmaster.backend.repositories;

import org.apache.zookeeper.KeeperException;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.NodeExistsException;

/**
 * CRUD interface for {@link ZKNode}.
 * ZKNodeRepository == connection with real server.
 */
public interface ZKNodeRepository {

    /**
     * @param path  Node path.
     * @param value Node value.
     * @return Create success OR not.
     * @throws NodeExistsException ZooKeeper API - exception
     */
    boolean create(String path, String value) throws NodeExistsException;

    /**
     * Get host-value(root with all sub-nodes) from real server, packed as tree-node{@link ZKNode}.
     *
     * @return {@link ZKNode} host-value OR null.
     */
    ZKNode getHostValue();

    /**
     * Select inner sub-node from {@param root}.
     *
     * @param root - start node. Default=hostValue(root).
     * @param path - absolute path of searching {@link ZKNode}.
     * @return result || null ==>> if searching {@link ZKNode} doesn't found.
     * @implNote Tree traversal should be: for width by List, Not recursion.
     * * Cause we don't know how deep tree is.(Argument for List)
     */
    ZKNode getSubNode(ZKNode root, String path);

    /**
     * Update Node value.
     *
     * @param path  Node path.
     * @param value new Node value.
     * @return Update success OR not.
     */
    boolean set(String path, String value);

    /**
     * Delete Node in real server.
     *
     * @param path Node path.
     * @return Delete success OR not.
     */
    boolean delete(String path);

    /**
     * @param path             Node path.
     * @param name             new Node name.
     * @param value            new Node value.
     * @param currentHostValue host root for iteration by ZooKeeper nodes for renaming all sub-nodes
     *                         && delete all old sub-nodes.
     * @return Rename success OR not.
     * @throws KeeperException      ZooKeeper API - exception
     * @throws InterruptedException ZooKeeper API - exception
     * @throws NodeExistsException  ZooKeeper API - exception
     */
    boolean rename(String path, String name, String value, ZKNode currentHostValue)
            throws KeeperException, InterruptedException, NodeExistsException;

    ZKTransaction transaction();

}

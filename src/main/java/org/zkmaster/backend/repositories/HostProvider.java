package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;

/**
 * High level api of real-server. Provide and extend {@link Host} API.
 */
public interface HostProvider {

    /**
     * @param path  Node path.
     * @param value Node value.
     * @return Create success OR not.
     * @throws NodeExistsException ZooKeeper API - exception
     */
    boolean createNode(String path, String value) throws NodeExistsException, NodeCreateException;

    /**
     * Read host-value(root with all sub-nodes) from real server, packed as tree-node{@link ZKNode}.
     *
     * @return {@link ZKNode} host-value OR null.
     */
    ZKNode readHostValue() throws NodeReadException;

    /**
     * Select inner sub-node from {@param root}.
     *
     * @param root - start node. Default=hostValue(root).
     * @param path - absolute path of searching {@link ZKNode}.
     * @return Node with {@param path} with all sub-node OR
     * null ==>> if searching {@link ZKNode} doesn't found.
     * @implNote Tree traversal should be: for width by List, Not recursion.
     * * Cause we don't know how deep tree is.(Argument for List)
     */
    ZKNode getSubNode(ZKNode root, String path);

    /**
     * Update Node value if Node name == {@param name}
     * Rename Node and all sub-nodes if Node name != {@param name}
     *
     * @param path        Node path.
     * @param name        new Node name.
     * @param value       new Node value.
     * @param actualCache * Need for comfortable iteration by tree.
     *                    In order not to read host-value from real server
     *                    every time then need just rename one node.
     * @return Save success OR not.
     */
    boolean saveNode(String path, String name, String value, ZKNode actualCache) throws NodeSaveException;

    /**
     * Delete Node in real server.
     *
     * @param path Node path.
     * @return Delete success OR not.
     */
    boolean deleteNode(String path, ZKNode actualCache) throws NodeDeleteException;

    /**
     * Access to low level API.
     */
    ZKTransaction transaction();

}

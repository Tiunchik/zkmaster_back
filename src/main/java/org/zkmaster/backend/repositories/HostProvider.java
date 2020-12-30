package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;

/**
 * Interface that provider you API for make any changes on real-server that this host is.
 * High level api of real-server. Provide and extend {@link Host} API.
 */
public interface HostProvider {

    /**
     * Create Node on real-server by provided params.
     *
     * @param path  Node path.
     * @param value Node value.
     * @return Create success OR not.
     * @throws NodeExistsException -
     * @throws NodeCreateException -
     */
    boolean createNode(String path, String value) throws NodeExistsException, NodeCreateException;

    /**
     * Read host-value from real server.
     *
     * @return host-value OR throw Exception.
     * @throws NodeReadException -
     */
    ZKNode readHostValue() throws NodeReadException;

    /**
     * Save any changes with Node on real-server by provided params.
     *
     * @param path        Node path.
     * @param name        new Node name.
     * @param value       new Node value.
     * @param actualCache * Need for comfortable iteration by tree.
     *                    In order not to read host-value from real server
     *                    every time then need just rename one node.
     * @return Save success OR not.
     * @throws NodeSaveException -
     */
    boolean saveNode(String path, String name, String value, ZKNode actualCache) throws NodeSaveException;

    /**
     * Delete Node on real server by provided params.
     *
     * @param path Node path.
     * @return Delete success OR not.
     * @throws NodeDeleteException -
     */
    boolean deleteNode(String path, ZKNode actualCache) throws NodeDeleteException;

    /**
     * Provide transaction for real-server.
     *
     * @see ZKTransaction
     */
    ZKTransaction transaction();

}

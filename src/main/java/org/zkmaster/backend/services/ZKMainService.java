package org.zkmaster.backend.services;

import org.zkmaster.backend.controllers.APIController;
import org.zkmaster.backend.controllers.CRUDController;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.*;
import org.zkmaster.backend.listeners.ServerEventListener;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;

import java.util.List;
import java.util.Map;

/**
 * TODO Feature: - Save cache then server have been close.
 * TODO Feature: - Delete cache anyway if Front-end doesn't send approve that we still need this connection.
 * <p>
 * Main service that provide API for using in *-end classes(see below).
 *
 * @author Daniils Loputevs.
 * @see CRUDController
 * @see APIController
 * @see ServerEventListenerDefault
 */
public interface ZKMainService {

    /* ####### CRUD ####### */

    /**
     * For: {@link CRUDController}
     * Default CRUD - CREATE
     *
     * @param host -
     * @return Create success or not.
     */
    boolean createNode(String host, String path, String value) throws NodeExistsException;

    /**
     * For: {@link CRUDController}
     * Default CRUD - REED
     * <p>
     * !!! This method MUST wait while cache is refreshing.
     *
     * @param host -
     * @return Host value in format: Node(tree).
     */
    ZKNode getHostValue(String host);

    /**
     * For: {@link CRUDController}
     * Default CRUD - UPDATE
     *
     * @param host -
     * @param path  Node path.
     * @param name  new Node name.
     * @param value new Node value.
     * @return Rename success OR not.
     * @throws NodeSaveException   ZooKeeper API - exception
     * @throws NodeRenameException ZooKeeper API - exception
     * @implNote Update && rename should be implements and use in this method.
     */
    boolean saveNode(String host, String path, String name, String value) throws NodeSaveException, NodeRenameException;

    /**
     * For: {@link CRUDController}
     * Default CRUD - DELETE
     *
     * @param host -
     * @param path -
     * @return Delete success or not.
     */
    boolean deleteNode(String host, String path) throws NodeDeleteException;

    /* ####### CRUD ####### */

    /**
     * For: {@link ServerEventListener}
     * Block cache and refresh it.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    void refreshCache(String host);

    boolean containsConnection(String host);

    /**
     * For: {@link APIController}
     * Try create connection.
     *
     * @param host -
     * @return try is fail ==>> false
     */
    boolean createConnection(String host) throws WrongHostException;

//    /**
//     * For: ServerEventController
//     * Delete connection.
//     * <p>
//     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
//     *
//     * @param hostUrl -
//     */
//    default void deleteConnection(String hostUrl) {
//    // For future - then we can get cache but server have been close BEFORE we receive Request.
//    }

    /**
     * For: {@link ServerEventListener}
     * Delete connection and chase.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    void deleteConnectionAndCache(String host);

//    @Deprecated(since = "Unsure that it will need")
//    void reconnect(String host);

    /**
     * For: {@link APIController}
     * Check: is this hosts still alive?
     *
     * @param hosts -
     * @return map :
     * key - host.
     * val - alive OR close.
     */
    Map<String, Boolean> checkHostsHealth(List<String> hosts);

}

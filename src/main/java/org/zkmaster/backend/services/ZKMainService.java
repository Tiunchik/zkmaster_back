package org.zkmaster.backend.services;

import org.zkmaster.backend.controllers.ZKConnectionController;
import org.zkmaster.backend.controllers.ZKMController;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.NodeDeleteException;
import org.zkmaster.backend.exceptions.NodeExistsException;
import org.zkmaster.backend.exceptions.NodeUpdateException;
import org.zkmaster.backend.exceptions.WrongHostException;
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
 * @see ZKMController
 * @see ZKConnectionController
 * @see ServerEventListenerDefault
 */
public interface ZKMainService {

    /* ####### CRUD ####### */

    /**
     * For: {@link ZKMController}
     * Default CRUD - CREATE
     *
     * @param hostUrl -
     * @return Create success or not.
     */
    boolean createNode(String hostUrl, String path, String value) throws NodeExistsException;

    /**
     * For: {@link ZKMController}
     * Default CRUD - REED
     * <p>
     * !!! This method MUST wait while cache is refreshing.
     *
     * @param hostUrl -
     * @return Host value in format: Node(tree).
     */
    ZKNode getHostValue(String hostUrl);

    /**
     * For: {@link ZKMController}
     * Default CRUD - UPDATE
     *
     * @param host -
     * @param path -
     * @param value -
     * @return Create success or not.
     */
    boolean updateNode(String host, String path, String value) throws NodeUpdateException;

    /**
     * For: {@link ZKMController}
     * Default CRUD - DELETE
     *
     * @param hostUrl -
     * @param path -
     * @return Delete success or not.
     */
    boolean deleteNode(String hostUrl, String path) throws NodeDeleteException;

    /* ####### CRUD ####### */

    /**
     * For: {@link ServerEventListener}
     * Block cache and refresh it.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param hostUrl -
     */
    void refreshCache(String hostUrl);

    boolean containsConnection(String host);

    /**
     * For: {@link ZKConnectionController}
     * Try create connection.
     *
     * @param hostUrl -
     * @return try is fail ==>> false
     */
    boolean createConnection(String hostUrl) throws WrongHostException;

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
     * @param hostUrl -
     */
    void deleteConnectionAndCache(String hostUrl);

//    @Deprecated(since = "Unsure that it will need")
//    void reconnect(String host);

    /**
     * For: {@link ZKConnectionController}
     * Check: is this hosts still alive?
     *
     * @param hosts -
     * @return map :
     * key - host.
     * val - alive OR close.
     */
    Map<String, Boolean> checkHostsHealth(List<String> hosts);

}

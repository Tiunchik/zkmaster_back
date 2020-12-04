package org.zkmaster.backend.services;

import org.zkmaster.backend.controllers.ZKController;
import org.zkmaster.backend.entity.ZKNode;

/**
 * TODO - 1) Save cache then server have been close.
 * TODO - 2) Delete cache anyway if Front-end doesn't send approve that we still need this connection.
 * TODO - 2) * Now it realise like: server close >> delete caches.
 * <p>
 * Main service that provide API for using in controllers.
 *
 * @author Daniils Loputevs.
 * @see ZKController
 * @see org.zkmaster.backend.controllers.ServerEventController
 */
public interface ZkMainService {

    /**
     * For: RestController
     * Try create connection
     *
     * @param hostUrl -
     * @return try is fail ==>> false
     */
    boolean createConnection(String hostUrl);

    /**
     * For: RestController
     * Default CRUD - CREATE
     *
     * @param hostUrl -
     * @return Create success or not.
     */
    boolean createNode(String hostUrl, String path, String value);

    /**
     * For: RestController
     * Default CRUD - REED
     * <p>
     * !!! This method MUST wait while cache is refreshing.
     *
     * @param hostUrl -
     * @return Host value in format: Node(tree).
     */
    ZKNode getHostValue(String hostUrl);

    /**
     * For: RestController
     * Default CRUD - UPDATE
     *
     * @param hostUrl -
     * @return Create success or not.
     */
    boolean updateNode(String hostUrl, String path, String value);

    /**
     * For: RestController
     * Default CRUD - DELETE
     *
     * @param hostUrl -
     * @return Delete success or not.
     */
    boolean deleteNode(String hostUrl, String path);

    /**
     * For: ServerEventController
     * Block cache and refresh it.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param hostUrl -
     */
    void refreshCache(String hostUrl);

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
     * For: ServerEventController
     * Delete connection and chase.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param hostUrl -
     */
    void deleteConnectionAndCache(String hostUrl);


}

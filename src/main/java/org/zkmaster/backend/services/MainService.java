package org.zkmaster.backend.services;

import org.zkmaster.backend.annotations.Delegate;
import org.zkmaster.backend.controllers.APIController;
import org.zkmaster.backend.controllers.CRUDController;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.listeners.ServerEventListener;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.services.transform.TransformStrategy;

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
public interface MainService {

    /* ####### CRUD ####### */

    /**
     * For: {@link CRUDController}
     * Default CRUD - CREATE
     *
     * @param host -
     * @return Create success or not.
     */
    @Delegate(to = HostProvider.class)
    boolean createNode(String host, String path, String value) throws HostProviderNotFoundException, NodeExistsException, NodeCreateException;

    /**
     * For: {@link CRUDController}
     *
     * @param host -
     * @return Host-value as default {@link ZKNode}.
     * @implNote If other thread(controller) will ask cache, it MUST wait while cache is refreshing.
     */
    @Delegate(to = HostProvider.class)
    ZKNode getHostValue(String host) throws NodeReadException;

    /**
     * For: {@link CRUDController}
     * Default CRUD - UPDATE
     *
     * @param host  -
     * @param path  Node path.
     * @param name  new Node name.
     * @param value new Node value.
     * @return Rename success OR not.
     * @throws NodeSaveException ZooKeeper API - exception
     * @implNote Update && rename should be implements and use in this method.
     */
    @Delegate(to = HostProvider.class)
    boolean saveNode(String host, String path, String name, String value) throws NodeSaveException, HostProviderNotFoundException, NodeReadException;

    /**
     * For: {@link CRUDController}
     * Default CRUD - DELETE
     *
     * @param host -
     * @param path -
     * @return Delete success or not.
     */
    @Delegate(to = HostProvider.class)
    boolean deleteNode(String host, String path) throws NodeSaveException, NodeDeleteException, HostProviderNotFoundException, NodeReadException;

    /* ####### Advanced API ####### */

    /**
     * For: {@link ServerEventListener}
     * Block cache and refresh it.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    @Delegate(to = HostContext.class)
    void refreshCache(String host) throws NodeReadException;

    @Delegate(to = HostContext.class)
    boolean containsConnection(String host);

    /**
     * For: {@link APIController}
     * Try create connection.
     *
     * @param host -
     * @return try is fail ==>> false
     */
    @Delegate(to = HostContext.class)
    boolean createConnection(String host) throws WrongHostAddressException;

    /**
     * For: {@link ServerEventListener}
     * Delete connection and chase.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    @Delegate(to = HostContext.class)
    void deleteConnectionAndCache(String host);

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

    @Delegate(to = TransformStrategy.class)
    List<String> exportHost(String host, String type) throws NodeReadException;

    @Delegate(to = TransformStrategy.class)
    boolean importData(String host, String type, List<String> data) throws HostProviderNotFoundException, DataImportFailException;

}

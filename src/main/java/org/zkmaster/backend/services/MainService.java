package org.zkmaster.backend.services;

import org.zkmaster.backend.controllers.APIController;
import org.zkmaster.backend.controllers.CRUDController;
import org.zkmaster.backend.controllers.TransformController;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostCloseException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.listeners.ServerEventListener;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.repositories.HostProvider;

import java.util.List;
import java.util.Map;

/**
 * Main service that provide API for using in Controllers & other Out-work classes(see below).
 * * Delegate real code to other classes. You see it by look at methods docs.
 *
 * <p>{@param host} :: mean host address of real server. It use as pair <hostAddress & RealHost>
 *
 * @author Daniils Loputevs.
 * @see CRUDController
 * @see TransformController
 * @see APIController
 * @see ServerEventListenerDefault
 */
public interface MainService {

    /* ####### CRUD ####### */

    /**
     * Create Node on real-server by provided params.
     * For: {@link CRUDController}
     * <p>
     * Delegate business code: {@link HostProvider}.
     *
     * @param host  host for that it will create a new Node.
     * @param path  Node path.
     * @param value Node value.
     * @return Create connection success OR Throw Exception.
     * @throws HostProviderNotFoundException -
     * @throws NodeExistsException           -
     * @throws NodeCreateException           -
     */
    boolean createNode(String host, String path, String value) throws HostProviderNotFoundException, NodeExistsException, NodeCreateException;

    /**
     * Read actual host-value from real-server.
     * For: {@link CRUDController}
     * <p>
     * Delegate business code: {@link HostProvider}.
     *
     * @param host host for that it will read host-value.
     * @return Read host-value success OR Throw Exception.
     * @throws NodeReadException -
     * @implNote If other thread(controller) will ask host-value, it MUST wait while cache is refreshing.
     */
    ZKNode getHostValue(String host) throws NodeReadException;


    /**
     * Save any changes with node by provided params.
     * For: {@link CRUDController}
     * <p>
     * Delegate business code: {@link HostProvider}.
     *
     * @param host  host for that it will save node.
     * @param path  Node path.
     * @param name  new Node name.
     * @param value new Node value.
     * @return Save success OR throw Exception.
     * @throws NodeSaveException             -
     * @throws HostProviderNotFoundException -
     * @throws NodeReadException             -
     */
    boolean saveNode(String host, String path, String name, String value)
            throws NodeSaveException, HostProviderNotFoundException, NodeReadException;

    /**
     * Delete Node by provided params.
     * For: {@link CRUDController}
     * <p>
     * Delegate business code: {@link HostProvider}.
     *
     * @param host host for that it will delete.
     * @param path Node path.
     * @return Delete success OR throw Exception.
     * @throws NodeDeleteException           -
     * @throws HostProviderNotFoundException -
     * @throws NodeReadException             -
     */
    boolean deleteNode(String host, String path)
            throws NodeDeleteException, HostProviderNotFoundException, NodeReadException;

    /* ####### Advanced API ####### */

    /**
     * Refresh cache by provided params.
     * For: {@link ServerEventListener}
     * <p>
     * Delegate business code: {@link HostContext}.
     *
     * @param host host for that it will refresh.
     * @return refresh success OR throw Exception.
     * @throws NodeReadException -
     * @implNote This method MUST block cache and other threads MUST wait until refresh isn't finish.
     */
    boolean refreshCache(String host) throws NodeReadException;

    /**
     * Check contains host in program OR not.
     * For: {@link CRUDController}
     * <p>
     * Delegate business code: {@link HostContext}.
     *
     * @param host host for that it will check.
     * @return true if contains, else false.
     */
    boolean containsConnection(String host);

    /**
     * Try create connection by provided params.
     * For: {@link APIController}
     * <p>
     * Delegate business code: {@link HostContext}.
     *
     * @param host host for that it will create.
     * @return Create connection success OR throw Exception.
     * @throws HostWrongAddressException -
     */
    boolean createConnection(String host) throws HostWrongAddressException;

    /**
     * IMPORTANT: Need to discuss about "how we delete and keep connections"
     * <p>
     * Delete connection and chase.
     * For: {@link ServerEventListener}
     * <p>
     * Delegate business code: {@link HostContext}.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    void deleteConnectionAndCache(String host) throws HostCloseException;

    /**
     * IMPORTANT: Need to discuss about "how we delete and keep connections"
     * <p>
     * Check: is this hosts still alive?
     * For: {@link APIController}
     * <p>
     * Delegate business code: {@link HostContext}.
     *
     * @param hosts -
     * @return map :
     * key - host.
     * val - alive OR close.
     */
    @Deprecated(since = "not use in program, Maybe in future.")
    Map<String, Boolean> checkHostsHealth(List<String> hosts);

}

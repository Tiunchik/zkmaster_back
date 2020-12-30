package org.zkmaster.backend.services;

import org.zkmaster.backend.annotations.Delegate;
import org.zkmaster.backend.controllers.APIController;
import org.zkmaster.backend.controllers.CRUDController;
import org.zkmaster.backend.controllers.TransformController;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.listeners.ServerEventListener;
import org.zkmaster.backend.listeners.ServerEventListenerDefault;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.services.injection.InjectionService;
import org.zkmaster.backend.services.transform.TransformStrategy;

import java.util.List;
import java.util.Map;

/**
 * Main service that provide API for using in Controllers & other Out-work classes(see below).
 * * Delegate real code to other classes. You see it by look at annotation {@link Delegate}.
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
     *
     * @param host  host for that it will create a new Node.
     * @param path  Node path.
     * @param value Node value.
     * @return Create connection success OR Throw Exception.
     * @throws HostProviderNotFoundException -
     * @throws NodeExistsException           -
     * @throws NodeCreateException           -
     */
    @Delegate(to = HostProvider.class)
    boolean createNode(String host, String path, String value) throws HostProviderNotFoundException, NodeExistsException, NodeCreateException;

    /**
     * Read actual host-value from real-server.
     * For: {@link CRUDController}
     *
     * @param host host for that it will read host-value.
     * @return Read host-value success OR Throw Exception.
     * @throws NodeReadException -
     * @implNote If other thread(controller) will ask host-value, it MUST wait while cache is refreshing.
     */
    @Delegate(to = HostProvider.class)
    ZKNode getHostValue(String host) throws NodeReadException;


    /**
     * Save any changes with node by provided params.
     * For: {@link CRUDController}
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
    @Delegate(to = HostProvider.class)
    boolean saveNode(String host, String path, String name, String value)
            throws NodeSaveException, HostProviderNotFoundException, NodeReadException;

    /**
     * Delete Node by provided params.
     * For: {@link CRUDController}
     *
     * @param host host for that it will delete.
     * @param path Node path.
     * @return Delete success OR throw Exception.
     * @throws NodeDeleteException           -
     * @throws HostProviderNotFoundException -
     * @throws NodeReadException             -
     */
    @Delegate(to = HostProvider.class)
    boolean deleteNode(String host, String path)
            throws NodeDeleteException, HostProviderNotFoundException, NodeReadException;

    /* ####### Advanced API ####### */

    /**
     * Refresh cache by provided params.
     * For: {@link ServerEventListener}
     *
     * @param host host for that it will refresh.
     * @return refresh success OR throw Exception.
     * @throws NodeReadException -
     * @implNote This method MUST block cache and other threads MUST wait until refresh isn't finish.
     */
    @Delegate(to = HostContext.class)
    boolean refreshCache(String host) throws NodeReadException;

    /**
     * Check contains host in program OR not.
     * For: {@link CRUDController}
     *
     * @param host host for that it will check.
     * @return true if contains, else false.
     */
    @Delegate(to = HostContext.class)
    boolean containsConnection(String host);

    /**
     * Try create connection by provided params.
     * For: {@link APIController}
     *
     * @param host host for that it will create.
     * @return Create connection success OR throw Exception.
     * @throws WrongHostAddressException -
     */
    @Delegate(to = HostContext.class)
    boolean createConnection(String host) throws WrongHostAddressException;

    /**
     * IMPORTANT: Need to discuss about "how we delete and keep connections"
     * <p>
     * For: {@link ServerEventListener}
     * Delete connection and chase.
     * <p>
     * !!! This method MUST block cache and other threads MUST wait until refresh isn't finish.
     *
     * @param host -
     */
    @Deprecated(since = "not use in program, Maybe in future.")
    @Delegate(to = HostContext.class)
    void deleteConnectionAndCache(String host);

    /**
     * IMPORTANT: Need to discuss about "how we delete and keep connections"
     * <p>
     * For: {@link APIController}
     * Check: is this hosts still alive?
     *
     * @param hosts -
     * @return map :
     * key - host.
     * val - alive OR close.
     */
    @Deprecated(since = "not use in program, Maybe in future.")
    @Delegate(to = HostContext.class)
    Map<String, Boolean> checkHostsHealth(List<String> hosts);

    @Delegate(to = InjectionService.class)
    boolean injectFromTo(InjectionDTO dto) throws InjectionFailException;

    /**
     * Export host-value by provided params.
     * For: {@link TransformController}.
     *
     * @param host host for that it will export host-value.
     * @param type export type.
     * @return Export success OR throw Exception.
     * @throws NodeReadException -
     */
    @Delegate(to = TransformStrategy.class)
    List<String> exportHost(String host, String type) throws NodeReadException;

    /**
     * Export host-value by provided params.
     * For: {@link TransformController}.
     *
     * @param host host for that it will data import.
     * @param type export type.
     * @param data nodes for import.
     * @return Import success OR throw Exception.
     * @throws HostProviderNotFoundException -
     * @throws DataImportFailException       -
     */
    @Delegate(to = TransformStrategy.class)
    boolean importData(String host, String type, List<String> data)
            throws HostProviderNotFoundException, DataImportFailException;

}

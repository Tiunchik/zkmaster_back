package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.services.MainService;

import java.util.List;
import java.util.Map;

/**
 * Work with: cache of real-server && host providers{@link HostProvider}.
 * Execute API that delegated by {@link MainService}.
 *
 * {@param host} :: mean host address of real server. It use as pair <hostAddress & RealHost>
 */
public interface HostContext {

    ZKNode getActualHostValue(String host) throws NodeReadException;

    /**
     * Get hostProvider by {@param host}
     *
     * @param host -
     * @return -
     * @throws HostProviderNotFoundException -
     */
    HostProvider getHostProvider(String host) throws HostProviderNotFoundException;

    /**
     * Try to create connection with real-server that have address == {@param host}.
     *
     * @param host - address of real-serve, for try create connection.
     * @return Create connection success OR Throw Exception.
     * @throws HostWrongAddressException -
     */
    boolean createHost(String host) throws HostWrongAddressException;

    /**
     * Refresh inner cache for {@param host}.
     *
     * @param host -
     * @throws NodeReadException -
     */
    boolean refreshCache(String host) throws NodeReadException;

    /**
     * Check contains host or not.
     * Default {@link Map#containsKey(Object)}
     *
     * @param host -
     * @return -
     */
    boolean containsHost(String host);

    /**
     * Need to discuss about "how we delete and keep connections"
     */
    @Deprecated(since = "not use in program, Maybe in future.")
    void deleteHostAndCache(String host);

    /**
     * Need to discuss about "how we delete and keep connections"
     */
    @Deprecated(since = "not use in program, Maybe in future.")
    Map<String, Boolean> containsHostAll(List<String> hosts);

    /**
     * Process {@param dto}. Inject/copy Node from one place into other place.
     *
     * @param dto -
     * @return Success OR throw {@link InjectionFailException}.
     * @throws InjectionFailException -
     */
    boolean injectFromTo(InjectionDTO dto) throws InjectionFailException;

}

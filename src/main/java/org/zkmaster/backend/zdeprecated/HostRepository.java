package org.zkmaster.backend.zdeprecated;

import org.zkmaster.backend.repositories.HostProvider;

import java.util.List;
import java.util.Map;

/**
 * wrap of Map<String, HostProvider>
 */
@Deprecated
public interface HostRepository {
    void put(String host, HostProvider repository);

    HostProvider get(String host);

    void remove(String host);

    boolean contains(String host);

    /**
     * check connection with real serve by host.
     *
     * @param hosts -
     * @return map:
     * key - host.
     * val - have connection OR not.
     */
    Map<String, Boolean> containsByHosts(List<String> hosts);

    int size();
}

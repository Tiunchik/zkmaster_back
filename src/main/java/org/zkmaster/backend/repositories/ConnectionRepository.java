package org.zkmaster.backend.repositories;

import java.util.List;
import java.util.Map;

public interface ConnectionRepository {

    void put(String host, ZKNodeRepository repository);

    ZKNodeRepository get(String host);

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

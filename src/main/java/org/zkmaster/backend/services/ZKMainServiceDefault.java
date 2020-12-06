package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.repositories.CacheRepository;
import org.zkmaster.backend.repositories.ConnectionRepository;
import org.zkmaster.backend.repositories.ZKNodeRepository;

import java.util.List;
import java.util.Map;

@Service
public class ZKMainServiceDefault implements ZKMainService {

    /**
     * Actual repository of real server facade.
     * key - String                     - hostUrl.
     * val - {@link ZKNodeRepository}   - CRUD rep.
     */
    private final ConnectionRepository connections;

    /**
     * Actual cache of real server value.
     * key - String           - hostUrl.
     * val - {@link ZKNode}   - hostValue.
     */
    private final CacheRepository cache;

    private final ZKConnectionFactory zkFactory;

    @Autowired
    public ZKMainServiceDefault(ConnectionRepository connections,
                                CacheRepository cache,
                                ZKConnectionFactory zkFactory) {
        this.connections = connections;
        this.cache = cache;
        this.zkFactory = zkFactory;
    }


    @Override
    public boolean createNode(String hostUrl, String path, String value) {
        return connections.get(hostUrl).create(path, value);
    }

    /**
     * TODO - Try to use {@link java.util.Optional}.
     * Default CRUD - REED
     * <p>
     * ! If other thread(controller) will ask cache, it MUST wait while cache is refreshing.
     * </p>
     * STEP 1: ask cache - (have cache for host)
     * true >> out method.
     * false >> continue.
     * STEP 2: ask connections - (have rep for host)
     * true >> get "host value" and out method.
     *
     * @param hostUrl -
     * @return Host value in format: Node(tree). OR null.
     */
    @Override
    public ZKNode getHostValue(String hostUrl) {
        ZKNode hostValue = cache.get(hostUrl); // value or null
        if (hostValue == null) { // if cache is null
            hostValue = connections.get(hostUrl).getHostValue();
            cache.put(hostUrl, hostValue);
        }
        return hostValue;
    }

    @Override
    public boolean updateNode(String hostUrl, String path, String value) {
        return connections.get(hostUrl).set(path, value);
    }

    @Override
    public boolean deleteNode(String hostUrl, String path) {
        return connections.get(hostUrl).delete(path);
    }

    @Override
    public void refreshCache(String hostUrl) {
        ZKNode hostValue = connections.get(hostUrl).getHostValue();
        cache.put(hostUrl, hostValue);
    }

    @Override
    public boolean createConnection(String hostUrl) {
        if (!connections.contains(hostUrl)) {
            var connection = zkFactory.makeConnectionByHost(hostUrl);
            connections.put(hostUrl, connection);
        }
        return true;
    }

    @Override
    @Deprecated(since = "see in interface: ZKMainService")
    public void reconnect(String host) {
        // don't need it yet
    }

    @Override
    public void deleteConnectionAndCache(String hostUrl) {
        connections.remove(hostUrl);
        cache.remove(hostUrl);
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        return connections.containsByHosts(hosts);
    }

}

package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.repositories.ZKNodeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * TODO Feature: - Add here blocking while: cachesRefresh() && deleteConnectionAndCache()
 */
@Service
public class ZkMainServiceDefault implements ZkMainService {
    /**
     * Manager for work with a facade of real server.
     * * Map structure, contains connection(facade) with real server.
     * key - String           - hostUrl.
     * val - {@link ZKServer} - facade of server.
     */
    private final ZKConnectionManager connectionManager;

    /**
     * Actual repository of real server facade.
     * key - String                     - hostUrl.
     * val - {@link ZKNodeRepository}   - CRUD rep.
     */
    private final Map<String, ZKNodeRepository> repositories = new HashMap<>();

    /**
     * Actual cache of real server value.
     * key - String           - hostUrl.
     * val - {@link ZKNode}   - hostValue.
     */
    private final Map<String, ZKNode> cache = new HashMap<>();

    @Autowired
    public ZkMainServiceDefault(ZKConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }




    @Override
    public boolean createNode(String hostUrl, String path, String value) {
        ZKNodeRepository rep = repositories.get(hostUrl);
        if (rep != null) {
            return rep.create(path, value);
        }
        throw new IllegalArgumentException();
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
     * STEP 2: ask repositories - (have rep for host)
     * true >> get "host value" and out method.
     * false >> continue.
     * STEP 3: aks connectionManager - (have connect for host)
     * true >> create rep, then get "host value" and out method.
     * false >> return disconnect.(Is it possible?)
     *
     * @param hostUrl -
     * @return Host value in format: Node(tree). OR null.
     */
    @Override
    public ZKNode getHostValue(String hostUrl) {
        ZKNode rsl = cache.get(hostUrl); // value or null

        if (rsl == null) { // if cache is null
            rsl = getHostValueIfNotCache(hostUrl, repositories::get);

            if (rsl == null) { // if repository is null
                rsl = getHostValueIfNotCache(hostUrl, connectionManager::createConnection);
            }
        }
        return rsl;
    }

    private ZKNode getHostValueIfNotCache(String hostUrl, Function<String, ZKNodeRepository> function) {
        ZKNode rsl = null;
        ZKNodeRepository hostRep = function.apply(hostUrl);
        if (hostRep != null) {
            rsl = hostRep.getFullNode("/");
            cache.put(hostUrl, rsl);
        }
        return rsl;
    }

    @Override
    public boolean updateNode(String hostUrl, String path, String value) {
        ZKNodeRepository rep = repositories.get(hostUrl);
        if (rep != null) {
            return rep.set(path, value);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public boolean deleteNode(String hostUrl, String path) {
        ZKNodeRepository rep = repositories.get(hostUrl);
        if (rep != null) {
            return rep.delete(path);
        }
        throw new IllegalArgumentException();
    }

    @Override
    public void refreshCache(String hostUrl) {
        ZKNode rsl = null;
        ZKNodeRepository rep = repositories.get(hostUrl);
        if (rep != null) {
            rsl = rep.getFullNode("/");
        }
        cache.put(hostUrl, rsl);
    }

    @Override
    public void deleteConnectionAndCache(String hostUrl) {
        repositories.remove(hostUrl);
        cache.remove(hostUrl);
        connectionManager.deleteConnection(hostUrl);
    }

    @Override
    public boolean createConnection(String hostUrl) {
        ZKNodeRepository rep = connectionManager.createConnection(hostUrl);
        if (rep != null) {
            repositories.put(hostUrl, rep);
            return true;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        Map<String, Boolean> rsl = new HashMap<>(hosts.size());
        for (var host : hosts) {
            rsl.put(host, repositories.containsKey(host));
        }
        return rsl;
    }
}

package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.repositories.ZKNodeRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * TODO - Add here blocking while: cachesRefresh() && deleteConnectionAndCache()
 * <p>
 * ja Zakomentil kod, na slu4aj "esli slomaetsja" to mozno bilo legko otkatitj.
 */
@Service
public class ZkMainServiceDefault implements ZkMainService {
    /**
     * Map structure, contains connection(facade) with real server.
     * key - String   - hostUrl.
     * val - ZKServer - connection(facade) to server.
     */
    private final ZKConnectionManager connectionManager;
    private final Map<String, ZKNodeRepository> repositories = new HashMap<>();
    private final Map<String, ZKNode> cache = new HashMap<>();

    @Autowired
    public ZkMainServiceDefault(ZKConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }


    @Override
    public boolean createConnection(String hostUrl) {
        ZKNodeRepository rep = connectionManager.createConnection(hostUrl);
        if (rep != null) {
            repositories.put(hostUrl, rep);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createNode(String hostUrl, String path, String value) {
        return crudOperationCore(hostUrl, "CREATE", path, value);
//        boolean rsl = true;
//        ZKNodeRepository rep = repositories.get(hostUrl);
//        if (rep != null) {
//            rep.create(path, value);
//        } else {
//            rsl = false;
//        }
//        return rsl;
    }

    /**
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
        return crudOperationCore(hostUrl, "UPDATE", path, value);
//        boolean rsl = true;
//        ZKNodeRepository rep = repositories.get(hostUrl);
//        if (rep != null) {
//            rep.set(path, value);
//        } else {
//            rsl = false;
//        }
//        return rsl;
    }

    @Override
    public boolean deleteNode(String hostUrl, String path) {
        return crudOperationCore(hostUrl, "DELETE", path, null);
//        boolean rsl = true;
//        ZKNodeRepository rep = repositories.get(hostUrl);
//        if (rep != null) {
//            rep.delete(path);
//        } else {
//            rsl = false;
//        }
//        return rsl;
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

    private boolean crudOperationCore(String hostUrl, String crudMethod,
                                      String path, String value) {
        boolean rsl = false;
        ZKNodeRepository rep = repositories.get(hostUrl);
        if (rep != null) {
            if ("CREATE".equals(crudMethod)) {
                rsl = rep.create(path, value);  // <<-- should set {true}
            } else if ("UPDATE".equals(crudMethod)) {
                rsl = rep.set(path, value);  // <<-- should set {true}
            } else if ("DELETE".equals(crudMethod)) {
                rsl = rep.delete(path);  // <<-- should set {true}
            }
        }
        return rsl;
    }

    @Override
    public void deleteConnectionAndCache(String hostUrl) {
        repositories.remove(hostUrl);
        cache.remove(hostUrl);
    }
}

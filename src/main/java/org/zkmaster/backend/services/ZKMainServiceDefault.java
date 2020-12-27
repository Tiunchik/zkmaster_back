package org.zkmaster.backend.services;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.exceptions.*;
import org.zkmaster.backend.repositories.CacheRepository;
import org.zkmaster.backend.repositories.ConnectionRepository;
import org.zkmaster.backend.repositories.ZKNodeRepository;
import org.zkmaster.backend.services.export.ExportStrategy;

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
    private final ExportStrategy exportStrategy;

    @Autowired
    public ZKMainServiceDefault(ConnectionRepository connections, CacheRepository cache,
                                ZKConnectionFactory zkFactory, ExportStrategy exportStrategy) {
        this.connections = connections;
        this.cache = cache;
        this.zkFactory = zkFactory;
        this.exportStrategy = exportStrategy;
    }


    @Override
    @Log
    public boolean createNode(String host, String path, String value) throws NodeExistsException {
        return connections.get(host).create(path, value);
    }

    /**
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
     * @param host -
     * @return Host value in format: Node(tree) OR null, if host isn't saved. (something logic problem O_O???)
     */
    @Override
    @Log
    public ZKNode getHostValue(String host) {
        ZKNode hostValue = cache.get(host); // value or null
        if (hostValue == null) { // if cache is null
            ZKNodeRepository temp = connections.get(host);
            if (temp != null) {
                hostValue = temp.getHostValue();
                cache.put(host, hostValue);
            }
        }
        return hostValue;
    }

    @Override
    @Log
    public boolean saveNode(String host, String path, String name, String value)
            throws NodeSaveException, NodeRenameException {
        String oldName = ZKNodes.extractNodeName(path);
        return (oldName.equals(name))
                ? updateNode(host, path, value)
                : renameNode(host, path, name, value);
    }

    @Override
    @Log
    public boolean deleteNode(String host, String path) throws NodeDeleteException {
        var temp = connections.get(host);
        if (temp == null) {
            throw new NodeDeleteException(host, path);
        }
        return temp.delete(path);
    }

    @Log
    private boolean renameNode(String host, String path, String name, String value) throws NodeRenameException {
        var temp = connections.get(host);
        boolean rsl;
        if (temp == null) {
            throw new NodeRenameException(host, path, value);
        }
        try {
            rsl = temp.rename(path, name, value, cache.get(host));
        } catch (KeeperException | InterruptedException | NodeExistsException e) {
            e.printStackTrace();
            throw new NodeRenameException(host, path, value);
        }
        return rsl;
    }

    @Log
    private boolean updateNode(String host, String path, String value) throws NodeSaveException {
        var temp = connections.get(host);
        if (temp == null) {
            throw new NodeSaveException(host, path, value);
        }
        return temp.set(path, value);
    }

    @Override
    @Log
    public void refreshCache(String host) {
        ZKNode hostValue = connections.get(host).getHostValue();
        cache.put(host, hostValue);
    }

    @Override
    public boolean containsConnection(String host) {
        return connections.contains(host);
    }

    @Override
    @Log
    public boolean createConnection(String host) throws WrongHostException {
        if (!connections.contains(host)) {
            connections.put(host, zkFactory.makeConnectionByHost(host));
        }
        return true;
    }

    @Override
    @Log
    public void deleteConnectionAndCache(String host) {
        connections.remove(host);
        cache.remove(host);
    }

    @Override
    @Log
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        return connections.containsByHosts(hosts);
    }

    @Override
    @Log
    public List<String> export(String host, String type) {
        return exportStrategy.get(type).export(this.getHostValue(host));
    }

}

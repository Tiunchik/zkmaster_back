package org.zkmaster.backend.zdeprecated;

import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.*;
import org.zkmaster.backend.factories.HostFactory;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.services.ZKMainService;
import org.zkmaster.backend.services.transform.TransformStrategy;

import java.util.List;
import java.util.Map;

@Service
@Deprecated
public class ZKMainServiceDefault implements ZKMainService {
    /**
     * Actual repository of real server facade.
     * key - String                     - hostUrl.
     * val - {@link HostProvider}   - CRUD rep.
     */
    private final HostServiceDep connections;
    /**
     * Actual cache of real server value.
     * key - String           - hostUrl.
     * val - {@link ZKNode}   - hostValue.
     */
    private final CacheRepository cache;
    private final HostFactory zkFactory;
    private final TransformStrategy transformStrategy;

    @Autowired
    public ZKMainServiceDefault(HostServiceDep connections, CacheRepository cache,
                                HostFactory zkFactory, TransformStrategy transformStrategy) {
        this.connections = connections;
        this.cache = cache;
        this.zkFactory = zkFactory;
        this.transformStrategy = transformStrategy;
    }


    @Override
    @Log
    public boolean createNode(String host, String path, String value) throws NodeExistsException {
        return connections.get(host).createNode(path, value);
    }

    /**
     * @implSpec explain how it work:
     * STEP 1: ask cache in store - (get host-value OR null)
     * (cache != null):
     * -- true >> out method.
     * -- false >> continue.
     * STEP 2: ask connections in store - (get repository OR null)
     * (repository != null):
     * -- true >> get "host value" and out method.
     * -- false >> Something Wrong! Check this case!
     */
    @Override
    @Log
    public ZKNode getHostValue(String host) {
        ZKNode hostValue = cache.get(host); // value or null
        if (hostValue == null) { // if cache is null
            HostProvider temp = connections.get(host);
            if (temp != null) {
                hostValue = temp.readHostValue();
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
        return temp.deleteNode(path);
    }

    @Log
    private boolean renameNode(String host, String path, String name, String value) throws NodeRenameException {
        var temp = connections.get(host);
        boolean rsl;
        if (temp == null) {
            throw new NodeRenameException(host, path, value);
        }
//        try {
//            rsl = temp.rename(path, name, value, cache.get(host));
//        } catch (KeeperException | InterruptedException | NodeExistsException e) {
//            e.printStackTrace();
//            throw new NodeRenameException(host, path, value);
//        }
//        return rsl;
        return false;
    }

    @Log
    private boolean updateNode(String host, String path, String value) throws NodeSaveException {
//        var temp = connections.get(host);
//        if (temp == null) {
//            throw new NodeSaveException(host, path, value);
//        }
//        return temp.set(path, value);
        return false;
    }

    @Override
    @Log
    public void refreshCache(String host) {
        ZKNode hostValue = connections.get(host).readHostValue();
        cache.put(host, hostValue);
    }

    @Override
    public boolean containsConnection(String host) {
        return connections.contains(host);
    }

    @Override
    @Log
    public boolean createConnection(String host) throws WrongHostAddressException {
        if (!connections.contains(host)) {
            connections.put(host, zkFactory.makeHostProvider(host));
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
    public List<String> exportHost(String host, String type) {
        return transformStrategy.get(type).exportHost(this.getHostValue(host));
    }

    @Override
    @Log
    public boolean importData(String host, String type, List<String> data) throws ImportFailException {
        ZKTransaction transaction = connections.get(host).transaction();
        transformStrategy.get(type).importData(data, transaction);
        try {
            transaction.commit();
        } catch (InterruptedException | KeeperException e) {
            System.err.println("IMPORT FAIL: ");
            e.printStackTrace();
            throw new ImportFailException(host, type, data.size());
        }
        return false;
    }

}

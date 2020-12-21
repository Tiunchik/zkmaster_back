package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.NodeDeleteException;
import org.zkmaster.backend.exceptions.NodeExistsException;
import org.zkmaster.backend.exceptions.NodeUpdateException;
import org.zkmaster.backend.exceptions.WrongHostException;
import org.zkmaster.backend.repositories.CacheRepository;
import org.zkmaster.backend.repositories.ConnectionRepository;
import org.zkmaster.backend.repositories.ZKNodeRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    @Autowired
    public ZKMainServiceDefault(ConnectionRepository connections,
                                CacheRepository cache,
                                ZKConnectionFactory zkFactory) {
        this.connections = connections;
        this.cache = cache;
        this.zkFactory = zkFactory;
    }


    @Override
    @Log
    public boolean createNode(String hostUrl, String path, String value) throws NodeExistsException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = connections.get(hostUrl).create(path, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
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
     * @param hostUrl -
     * @return Host value in format: Node(tree) OR null, if host isn't saved. (something logic problem O_O???)
     */
    @Override
    @Log
    public ZKNode getHostValue(String hostUrl) {
        readWriteLock.readLock().lock();
        ZKNode hostValue;
        try {
            hostValue = cache.get(hostUrl); // value or null
            if (hostValue == null) { // if cache is null
                ZKNodeRepository temp = connections.get(hostUrl);
                if (temp != null) {
                    hostValue = temp.getHostValue();
                    cache.put(hostUrl, hostValue);
                }
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
        return hostValue;
    }

    @Override
    @Log
    public boolean updateNode(String host, String path, String value) throws NodeUpdateException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            var temp = connections.get(host);
            if (temp == null) {
                throw new NodeUpdateException(host, path, value);
            }
            rsl = temp.set(path, value);
        } catch (Exception e) {
            throw new NodeUpdateException(host, path, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    @Log
    public boolean deleteNode(String host, String path) throws NodeDeleteException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            var temp = connections.get(host);
            if (temp == null) {
                throw new NodeDeleteException(host, path);
            }
            rsl = temp.delete(path);
        } catch (Exception e) {
            throw new NodeDeleteException(host, path);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    @Log
    public void refreshCache(String hostUrl) {
        readWriteLock.writeLock().lock();
        ZKNode hostValue = connections.get(hostUrl).getHostValue();
        cache.put(hostUrl, hostValue);
        readWriteLock.writeLock().unlock();
    }

    @Override
    public boolean containsConnection(String host) {
        return connections.contains(host);
    }

    @Override
    @Log
    public boolean createConnection(String hostUrl) throws WrongHostException {
        readWriteLock.writeLock().lock();
        if (!connections.contains(hostUrl)) {
            ZKNodeRepository connection = zkFactory.makeConnectionByHost(hostUrl);
            connections.put(hostUrl, connection);
        }
        readWriteLock.writeLock().unlock();
        return true;
    }

//    @Override
//    @Deprecated(since = "see in interface: ZKMainService")
//    public void reconnect(String host) {
//        // don't need it yet
//    }

    @Override
    @Log
    public void deleteConnectionAndCache(String hostUrl) {
        readWriteLock.writeLock().lock();
        connections.remove(hostUrl);
        cache.remove(hostUrl);
        readWriteLock.writeLock().unlock();
    }

    @Override
    @Log
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        readWriteLock.readLock().lock();
        Map<String, Boolean> rsl = connections.containsByHosts(hosts);
        readWriteLock.readLock().unlock();
        return rsl;
    }

}

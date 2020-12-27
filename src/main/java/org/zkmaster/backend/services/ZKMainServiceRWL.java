package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.NodeDeleteException;
import org.zkmaster.backend.exceptions.NodeExistsException;
import org.zkmaster.backend.exceptions.NodeSaveException;
import org.zkmaster.backend.exceptions.WrongHostException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Proxy for Read-Write-Lock(RWL) of {@link ZKMainServiceDefault}.
 */
@Service
public class ZKMainServiceRWL implements ZKMainService {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final ZKMainService zkMainService;

    @Autowired
    public ZKMainServiceRWL(@Qualifier("ZKMainServiceDefault") ZKMainService zkMainService) {
        this.zkMainService = zkMainService;
    }

    @Override
    public boolean createNode(String host, String path, String value) throws NodeExistsException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = zkMainService.createNode(host, path, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public ZKNode getHostValue(String host) {
        readWriteLock.readLock().lock();
        ZKNode hostValue;
        try {
            hostValue = zkMainService.getHostValue(host);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return hostValue;
    }

    @Override
    public boolean saveNode(String host, String path, String name, String value) throws NodeSaveException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = zkMainService.saveNode(host, path, name, value);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("ZKM EXCEPTION: NodeSaveException: ");
            throw new NodeSaveException(host, path, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public boolean deleteNode(String host, String path) throws NodeDeleteException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = zkMainService.deleteNode(host, path);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NodeDeleteException(host, path);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public void refreshCache(String host) {
        readWriteLock.writeLock().lock();
        zkMainService.refreshCache(host);
        readWriteLock.writeLock().unlock();
    }

    @Override
    public boolean containsConnection(String host) {
        readWriteLock.readLock().lock();
        boolean rsl = zkMainService.containsConnection(host);
        readWriteLock.readLock().unlock();
        return rsl;
    }

    @Override
    public boolean createConnection(String host) throws WrongHostException {
        readWriteLock.writeLock().lock();
        boolean rsl = zkMainService.createConnection(host);
        readWriteLock.writeLock().unlock();
        return rsl;
    }

    @Override
    public void deleteConnectionAndCache(String host) {
        readWriteLock.writeLock().lock();
        zkMainService.deleteConnectionAndCache(host);
        readWriteLock.writeLock().unlock();
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        readWriteLock.readLock().lock();
        Map<String, Boolean> rsl = zkMainService.checkHostsHealth(hosts);
        readWriteLock.readLock().unlock();
        return rsl;
    }

    @Override
    public List<String> export(String host, String type) {
        readWriteLock.readLock().lock();
        List<String> rsl;
        try {
            rsl = zkMainService.export(host, type);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

}

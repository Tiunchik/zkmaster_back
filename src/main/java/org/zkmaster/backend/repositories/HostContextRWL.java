package org.zkmaster.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostNotFoundException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class HostContextRWL implements HostContext {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final HostContext ctx;

    @Autowired
    public HostContextRWL(@Qualifier("hostContextDefault") HostContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public ZKNode getActualHostValue(String host) {
        readWriteLock.readLock().lock();
        ZKNode rsl;
        try {
            rsl = ctx.getActualHostValue(host);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

    @Override
    public HostProvider getHostService(String host) throws HostNotFoundException {
        readWriteLock.readLock().lock();
        HostProvider rsl;
        try {
            rsl = ctx.getHostService(host);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

    @Override
    public boolean createHost(String host) throws WrongHostAddressException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = ctx.createHost(host);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public void refreshCache(String host) {
        readWriteLock.writeLock().lock();
        try {
            ctx.refreshCache(host);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public boolean containsHost(String host) {
        readWriteLock.readLock().lock();
        boolean rsl;
        try {
            rsl = ctx.containsHost(host);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

    @Override
    public void deleteHostAndCache(String host) {
        readWriteLock.writeLock().lock();
        try {
            ctx.deleteHostAndCache(host);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        readWriteLock.readLock().lock();
        Map<String, Boolean> rsl;
        try {
            rsl = ctx.checkHostsHealth(hosts);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

}
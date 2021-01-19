package org.zkmaster.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.ContextStateInfo;
import org.zkmaster.backend.exceptions.HostCloseException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.factories.HostFactory;

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
    public ZKNode getActualHostValue(String host) throws NodeReadException {
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
    public HostProvider getHostProvider(String host) throws HostProviderNotFoundException {
        readWriteLock.readLock().lock();
        HostProvider rsl;
        try {
            rsl = ctx.getHostProvider(host);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

    @Override
    public boolean createHost(String host) throws HostWrongAddressException {
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
    public boolean refreshCache(String host) throws NodeReadException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = ctx.refreshCache(host);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
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
    public void deleteHostAndCache(String host) throws HostCloseException {
        readWriteLock.writeLock().lock();
        try {
            ctx.deleteHostAndCache(host);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public Map<String, Boolean> containsHostAll(List<String> hosts) {
        readWriteLock.readLock().lock();
        Map<String, Boolean> rsl;
        try {
            rsl = ctx.containsHostAll(hosts);
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }
    
    @Override
    public ContextStateInfo getState() throws NodeReadException {
        readWriteLock.readLock().lock();
       ContextStateInfo rsl;
        try {
            rsl = ctx.getState();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }
    
    @Override
    public void setHostFactory(HostFactory hostFactory) {
        readWriteLock.writeLock().lock();
        try {
            ctx.setHostFactory(hostFactory);
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void clearContext() {
        readWriteLock.writeLock().lock();
        try {
            ctx.clearContext();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    
   
}

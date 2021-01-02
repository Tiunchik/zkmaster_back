package org.zkmaster.backend.repositories;

import org.springframework.beans.factory.annotation.Qualifier;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class HostProviderRWL implements HostProvider {
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final HostProvider provider;


    public HostProviderRWL(@Qualifier("hostProviderDefault") HostProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean createNode(String path, String value) throws NodeExistsException, NodeCreateException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = provider.createNode(path, value);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public ZKNode readHostValue() throws NodeReadException {
        readWriteLock.readLock().lock();
        ZKNode rsl;
        try {
            rsl = provider.readHostValue();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

    @Override
    public boolean saveNode(String path, String name, String value, ZKNode actualCache) throws NodeSaveException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = provider.saveNode(path, name, value, actualCache);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public boolean deleteNode(String path, ZKNode actualCache) throws NodeDeleteException, NodeReadException {
        readWriteLock.writeLock().lock();
        boolean rsl;
        try {
            rsl = provider.deleteNode(path, actualCache);
        } finally {
            readWriteLock.writeLock().unlock();
        }
        return rsl;
    }

    @Override
    public ZKTransaction transaction() {
        readWriteLock.readLock().lock();
        ZKTransaction rsl;
        try {
            rsl = provider.transaction();
        } finally {
            readWriteLock.readLock().unlock();
        }
        return rsl;
    }

}

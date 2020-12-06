package org.zkmaster.backend.repositories;

import org.springframework.stereotype.Repository;
import org.zkmaster.backend.entity.ZKNode;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO !!! MUST TO HAVE READ-WRITE LOCK!!!
 * <p>
 * TODO Feature: - Add here blocking while: cachesRefresh() && deleteConnectionAndCache()
 */
@Repository
public class CacheRepositoryDefault implements CacheRepository {
    /**
     * Actual host-value of real server.
     * key - String           - hostUrl.
     * val - {@link ZKNode}   - hostValue.
     */
    private final Map<String, ZKNode> cache = new HashMap<>();


    @Override
    public ZKNode get(String host) {
        return cache.get(host);
    }

    @Override
    public void put(String host, ZKNode hostValue) {
        cache.put(host, hostValue);
    }

    @Override
    public boolean remove(String host) {
        cache.remove(host);
        return true;
    }
}
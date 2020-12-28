package org.zkmaster.backend.zdeprecated;

import org.springframework.stereotype.Repository;
import org.zkmaster.backend.entity.ZKNode;

import java.util.HashMap;
import java.util.Map;

@Deprecated
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

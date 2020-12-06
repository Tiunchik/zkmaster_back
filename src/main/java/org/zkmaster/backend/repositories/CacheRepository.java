package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;

/**
 * Delegate part of API from ZKMainService.
 */

public interface CacheRepository {
    ZKNode get(String host);

    void put(String host, ZKNode hostValue);

    boolean remove(String host);
}

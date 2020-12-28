package org.zkmaster.backend.zdeprecated;

import org.zkmaster.backend.entity.ZKNode;

/**
 * Delegate part of API from ZKMainService.
 */

@Deprecated
public interface CacheRepository {
    ZKNode get(String host);

    void put(String host, ZKNode hostValue);

    boolean remove(String host);
}

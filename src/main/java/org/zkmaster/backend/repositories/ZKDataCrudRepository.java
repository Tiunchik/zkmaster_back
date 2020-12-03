package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;

public interface ZKDataCrudRepository {

    /**
     * !!! Maybe will be better that it will be line ZKNude constructor.
     *
     * @return {@param node}.
     */
    ZKNode create(String path, String value);

    /**
     * @return ZKNude without children, only "nude" info.
     */
    ZKNode getNude(String path);

    /**
     * @return ZKNude with all children(sub-nudes) OR null.
     */
    ZKNode getFullNudeOrNull(String path);

    boolean setData(String path, String value);

    /**
     * @param path nude path
     * @return success delete OR not.
     */
    boolean deleteNude(String path);
}

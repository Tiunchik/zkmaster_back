package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;

/**
 * CRUD interface for {@link ZKNode}.
 * ZKNodeRepository == connection with real server.
 */
public interface ZKNodeRepository {

    /**
     * !!! Maybe it would better if that it will accept {@link ZKNode}???
     *
     */
    boolean create(String path, String value);

    /**
     * Simple "Node-value" from real server, packed in {@link ZKNode}.
     *
     * @return {@link ZKNode} without children, only info OR null.
     */
    @Deprecated(since = "since front == null")
    ZKNode getSimpleNode(String path);

    /**
     * Full "host-value" from real server, packed in {@link ZKNode}.
     *
     * @return {@link ZKNode} with all children(sub-nodes) OR null.
     */
    ZKNode getHostValue();

    boolean set(String path, String value);

    /**
     * Delete "Node" in real server.
     *
     * @param path Node path.
     * @return Delete success OR not.
     */
    boolean delete(String path);

}

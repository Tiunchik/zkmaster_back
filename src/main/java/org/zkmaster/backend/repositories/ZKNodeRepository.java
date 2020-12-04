package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;

/**
 * CRUD interface for {@link ZKNode}.
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
    ZKNode getSimpleNode(String path);

    /**
     * Full "host-value" from real server, packed in {@link ZKNode}.
     *
     * @return {@link ZKNode} with all children(sub-nodes) OR null.
     */
    ZKNode getFullNode(String path);

    boolean set(String path, String value);

    /**
     * Delete "Node" in real server.
     *
     * @param path Node path.
     * @return Delete success OR not.
     */
    boolean delete(String path);

    /**
     * ????????????????????????
     *
     * @param node - ???????
     * @return ?????????
     * @deprecated Za4em on tebe, esli v samom {@link ZKNode} uze estj full path?
     * Ili ti ne bude6j sobiratj full path, a budet toljko imja???
     */
    @Deprecated
    String getFullPath(ZKNode node);

}

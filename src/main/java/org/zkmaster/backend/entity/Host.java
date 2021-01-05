package org.zkmaster.backend.entity;

import org.apache.zookeeper.ZooKeeper;
import org.zkmaster.backend.exceptions.node.*;

import java.util.List;

/**
 * Pattern Facade of {@link ZooKeeper real server}.
 * Interface for Warp ZooKeeper API into comfortable API.
 *
 * @see HostDefault
 */
public interface Host extends AutoCloseable {

    /**
     * Create new Node.
     *
     * @param path  Node path for create node on real-server.
     * @param value Node value.
     * @return Create Success OR throw Exception.
     */
    boolean create(String path, String value) throws NodeExistsException, NodeCreateException;

    /**
     * Read Node value.
     *
     * @param path Node path for read.
     * @return String value by nude.
     * @throws NodeReadException -
     */
    String read(String path) throws NodeReadException;

    /**
     * Update Node value by params.
     *
     * @param path  Node path.
     * @param value new Node name.
     * @return Update Success OR throw Exception.
     */
    boolean setData(String path, String value) throws NodeSaveException;

    /**
     * /**
     * Delete node by {@param path}.
     *
     * @param path Node path.
     * @return Delete Success OR throw Exception.
     * @throws NodeDeleteException -
     */
    boolean delete(String path) throws NodeDeleteException;

    /**
     * Get children names
     *
     * @param path Node path.
     * @return List of children name.
     * @throws NodeReadException -
     */
    List<String> getChildrenNames(String path) throws NodeReadException;

    /**
     * Get children paths
     *
     * @param path Node path.
     * @return List of children paths.
     * @throws NodeReadException -
     */
    List<String> getChildrenPaths(String path) throws NodeReadException;

    /**
     * Check has Node with path{@param path} children or not.
     *
     * @param path Node path.
     * @return Check Success OR throw Exception.
     * @throws NodeReadException -
     */
    boolean hasChildren(String path) throws NodeReadException;

    /**
     * Read Node.
     * IMPORTANT for Implementations: Node must have:
     * path, value, name.
     * IGNORE information of children!
     * Read Node must haven't any link and info of children.
     *
     * @param path node path.
     * @return Node by {@param path}.
     * @throws NodeReadException -
     */
    ZKNode readNode(String path) throws NodeReadException;

    /**
     * Get New transaction for future committing into this Host.
     *
     * @return transaction for committing.
     */
    ZKTransaction transaction();

    /**
     * Getter for this Host address.
     *
     * @return get host address.
     */
    String getHostAddress();

    /**
     * Just implement {@link ZooKeeper} interfaces: {@link AutoCloseable}.
     *
     * @throws Exception -
     */
    @Override
    void close() throws Exception;

}

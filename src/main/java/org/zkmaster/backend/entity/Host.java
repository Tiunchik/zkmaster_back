package org.zkmaster.backend.entity;

import org.apache.zookeeper.ZooKeeper;
import org.zkmaster.backend.exceptions.node.*;

import java.util.List;

/**
 * Interface for Warp {@link ZooKeeper} API into comfortable API.
 *
 * @see HostDefault
 */
public interface Host extends AutoCloseable {

    boolean create(String path, String value) throws NodeExistsException, NodeCreateException;

    String read(String path) throws NodeReadException;

    boolean setData(String path, String value) throws NodeSaveException;

    boolean delete(String path) throws NodeDeleteException;

    List<String> getChildren(String path) throws NodeReadException;

    boolean hasChildren(String path) throws NodeReadException;

    ZKNode readNode(String path) throws NodeReadException;

    ZKTransaction transaction();

    String getHostAddress();

    @Override
    void close() throws Exception;

}

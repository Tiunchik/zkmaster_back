package org.zkmaster.backend.entity;

import org.apache.zookeeper.ZooKeeper;

import java.util.List;

/**
 * Warp default ZooKeeper API into comfortable API.
 * <p>
 * If you need more API from original ZooKeeper,
 * declare it here then {@link Override} it in Inheritor class.
 */
public abstract class ZKServerDecorator implements AutoCloseable {
    protected final String hostUrl;
    protected final ZooKeeper zoo;

    protected ZKServerDecorator(String hostUrl, ZooKeeper zoo) {
        this.hostUrl = hostUrl;
        this.zoo = zoo;
    }

    /* Declaration of abstract methods */

    public abstract void create(String path, String value);

    public abstract String read(String path);

    public abstract boolean setData(String path, String value);

    public abstract boolean delete(String path);

    public abstract List<String> getChildren(String path);

    /* public methods */

    public String getHostUrl() {
        return hostUrl;
    }

    @Override
    public void close() throws Exception {
        zoo.close();
    }
}

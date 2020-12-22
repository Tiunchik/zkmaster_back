package org.zkmaster.backend.entity;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Transaction;
import org.apache.zookeeper.ZooDefs;

/**
 * Warp default {@link Transaction} API into comfortable API.
 * <p>
 * If you need more API from original {@link Transaction},
 * decorate it in this class.
 */
public class ZKTransaction {
    private final Transaction transaction;

    public ZKTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public ZKTransaction create(final String path, String value) {
        transaction.create(path,
                value.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        return this;
    }

    public ZKTransaction delete(final String path) {
        transaction.delete(path, -1);
        return this;
    }

    public ZKTransaction setData(final String path, String value) {
        transaction.setData(path, value.getBytes(), -1);
        return this;
    }

    public void commit() throws InterruptedException, KeeperException {
        transaction.commit();
    }

}

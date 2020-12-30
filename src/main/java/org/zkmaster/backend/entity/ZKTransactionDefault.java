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
public class ZKTransactionDefault implements ZKTransaction {
    private final Transaction transaction;

    public ZKTransactionDefault(Transaction transaction) {
        this.transaction = transaction;
    }

    public ZKTransactionDefault create(final String path, String value) {
        transaction.create(path,
                value.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.PERSISTENT);
        return this;
    }

    public ZKTransactionDefault delete(final String path) {
        transaction.delete(path, -1);
        return this;
    }

    public ZKTransactionDefault update(final String path, String value) {
        transaction.setData(path, value.getBytes(), -1);
        return this;
    }

    /**
     * @param errMsg    msg for print, if transaction failed.
     * @param exception outer exception.
     * @param <E>       outer exception type.
     * @return true, if transaction success, else throw {@param exception}.
     * @throws E {@param exception}.
     */
    public <E extends Exception> boolean commit(String errMsg, E exception) throws E {
        try {
            transaction.commit();
        } catch (InterruptedException | KeeperException origException) {
            System.err.println(errMsg);
            origException.printStackTrace();
            throw exception;
        }
        return true;
    }

    @Deprecated(since = "use overload for more comfortable code API")
    public void commit() throws InterruptedException, KeeperException {
        transaction.commit();
    }

}

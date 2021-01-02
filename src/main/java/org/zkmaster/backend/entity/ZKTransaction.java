package org.zkmaster.backend.entity;

import org.apache.zookeeper.Transaction;

/**
 * Warp default {@link Transaction} API into comfortable API.
 * <p>
 * If you need more API from original {@link Transaction},
 * decorate it in this class.
 */
public interface ZKTransaction {

    ZKTransaction create(final String path, String value);

    ZKTransaction delete(final String path);

    ZKTransaction update(final String path, String value);

    /**
     * @param errMsg    msg for print, if transaction failed.
     * @param exception outer exception.
     * @param <E>       outer exception type.
     * @return true, if transaction success, else throw {@param exception}.
     * @throws E {@param exception}.
     */
    <E extends Exception> boolean commit(String errMsg, E exception) throws E;

}

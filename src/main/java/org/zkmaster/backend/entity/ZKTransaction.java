package org.zkmaster.backend.entity;

import org.apache.zookeeper.Transaction;

import java.util.LinkedList;

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
     * Submit all early actions.
     *
     * @param errMsg    msg for print, if transaction failed.
     * @param exception outer exception.
     * @param <E>       outer exception type.
     * @return true, if transaction success, else throw {@param exception}.
     *
     * @throws E {@param exception}.
     */
    <E extends Exception> boolean commit(String errMsg, E exception) throws E;
    
    
    /* Utility static methods */
    
    
    /**
     * Reverse iterate on {@param deletePaths} and push each (Node path) into transaction.
     *
     * @param transaction -
     * @param deletePaths paths for delete.
     * @return {@param transaction}.
     */
    static ZKTransaction pushDeleteListReverse(ZKTransaction transaction, LinkedList<String> deletePaths) {
        while (!deletePaths.isEmpty()) {
            transaction.delete(deletePaths.removeLast());
        }
        return transaction;
    }
    
}

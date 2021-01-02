package org.zkmaster.backend.entity;

import java.util.LinkedList;

/**
 * Utility class for {@link ZKTransaction}.
 */
public class ZKTransactions {

    /**
     * Reverse iterate on {@param deletePaths} and push each (Node path) into transaction.
     *
     * @param transaction -
     * @param deletePaths paths for delete.
     * @return {@param transaction}.
     */
    public static ZKTransaction pushDeleteListReverse(ZKTransaction transaction, LinkedList<String> deletePaths) {
        while (!deletePaths.isEmpty()) {
            transaction.delete(deletePaths.removeLast());
        }
        return transaction;
    }

}

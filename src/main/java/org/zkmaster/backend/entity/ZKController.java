package org.zkmaster.backend.entity;

import org.zkmaster.backend.repositories.ZKDataCrudRepository;

/**
 * Container of components for working with a specific hostUrl{@link ZKServer}.
 *
 * Collecting in ZKControllerContext
 */
public class ZKController {
    private final ZKServer original;
    private final ZKConnection connection;
    private final ZKDataCrudRepository repository;

    public ZKController(ZKServer original, ZKConnection connection, ZKDataCrudRepository repository) {
        this.original = original;
        this.connection = connection;
        this.repository = repository;
    }

    public ZKServer getOriginal() {
        return original;
    }

    public ZKConnection getConnection() {
        return connection;
    }

    public ZKDataCrudRepository getRepository() {
        return repository;
    }
}

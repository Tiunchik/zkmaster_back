package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zkmaster.backend.services.MainServiceDefault;

/**
 * TODO - try to refactoring RWL(try-catch block).
 * TODO - split {@link MainServiceDefault} to several services.
 * TODO - test Expired event & timeout for ZooKeeper
 *
 * TODO - discuss about "Expired event".
 *
 * possible features:
 * - Save cache then server have been close.
 * - Delete cache anyway if Front-end doesn't send approve that we still need this connection.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.err.println("ZKM Back-end :: RUN");
    }

}

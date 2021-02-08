package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zkmaster.backend.services.MainServiceDefault;

/**
 * TODO - (must) add encode API for ZKNode value.
 * TODO - (must) Write colour log for: Watcher, DevLog, Exception handler.
 *
 * TODO - (low) try to refactoring RWL(try-catch block).
 * TODO - (low) split {@link MainServiceDefault} to several services.
 *
 * TODO - discus: how to use encode API.
 * TODO - discus: how to read encoded value.
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

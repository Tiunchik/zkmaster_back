package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.services.MainServiceDefault;

/**
 * TODO Feature - export into YAML.
 *
 * TODO - process IOException for business.
 * TODO - full-inform docs of {@link Host}.
 * TODO - full-inform docs of {@link ZKTransaction}.
 * TODO - split {@link MainServiceDefault} to several services.
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

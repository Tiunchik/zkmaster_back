package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.services.MainServiceDefault;

/**
 * TODO - process IOException for business.
 * TODO - full-inform docs of {@link ZKTransaction}.
 * TODO - split {@link MainServiceDefault} to several services.
 *
 * TODO - discuss about "how & then do deleteCacheAndConnection".
 * TODO - discuss about "Expired event & reconnect".
 * TODO - discuss about "injection API - new variant of InjectionDTO & how it must work in details".
 * TODO - discuss about "that ExceptionHandlers must throw to Front-end then catch exception".
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

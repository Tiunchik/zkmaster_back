package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO Feature - merge subNode in other real-server.
 * TODO Feature - export into YAML.
 * TODO Feature - cascade delete.
 * TODO Feature - getHostValue - re-write by List, not Recursion.
 *
 * TODO - process IOException for business.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.err.println("ZKM Back-end :: RUN");
    }

}

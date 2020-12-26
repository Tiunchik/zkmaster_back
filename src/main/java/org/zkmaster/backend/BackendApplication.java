package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * TODO Feature - Копирование из одного в другой
 * TODO Feature - export into YAML, JSON, ZKCommandsFile(?TXT || ?JSON)
 * TODO Feature - cascade delete.
 * TODO Feature - getHostValue - re-write by List, not Recursion.
 */
@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
        System.err.println("ZKM Back-end :: RUN");
    }


}

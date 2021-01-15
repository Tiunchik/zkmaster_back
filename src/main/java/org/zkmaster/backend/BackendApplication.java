package org.zkmaster.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.services.MainServiceDefault;

/**
 * TODO - full-inform docs of {@link ZKTransaction}.
 * TODO - split {@link MainServiceDefault} to several services.
 * TODO - injection: srcNode become trgNode child. re write code of this moment.
 *
 * TODO - discuss about "how & then do deleteCacheAndConnection".
 * TODO - discuss about "Expired event & reconnect".
 *
 * TODO - pathVariable(String nodePath) for import(export sub-node, not full hostValue).
 * TODO - rewrite import for new business requirements. (import into sub-node)
 * TODO - how to export sub-node's path? cut-off nodePath or stay ful???
 * original: /1/2-1/3-1 : v
 *           /1/2-1/3-1/4-1 : v
 *           /1/2-1/3-1/4-1 : v
 * export v1: /1/2-1/3-1 : v
 *            /1/2-1/3-1/4-1 : v
 *            /1/2-1/3-1/4-1 : v
 * export v2: 3-1 : v
 *            3-1/4-1 : v
 *            3-1/4-1 : v
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

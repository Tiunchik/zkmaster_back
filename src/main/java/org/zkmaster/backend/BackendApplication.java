package org.zkmaster.backend;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * TODO(Daniils): fix warning about slf4j init. I mean this, for start-up moment:
 * SLF4J: Class path contains multiple SLF4J bindings.
 * SLF4J: Found binding in [jar:file:/C:/Users/Admin/.m2/repository/ch/qos/logback/logback-classic/1.2.3/logback-classic-1.2.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
 * SLF4J: Found binding in [jar:file:/C:/Users/Admin/.m2/repository/org/slf4j/slf4j-log4j12/1.7.30/slf4j-log4j12-1.7.30.jar!/org/slf4j/impl/StaticLoggerBinder.class]
 * SLF4J: Found binding in [jar:file:/C:/Users/Admin/.m2/repository/org/apache/logging/log4j/log4j-slf4j-impl/2.13.3/log4j-slf4j-impl-2.13.3.jar!/org/slf4j/impl/StaticLoggerBinder.class]
 * SLF4J: See http://www.slf4j.org/codes.html#multiple_bindings for an explanation.
 * SLF4J: Actual binding is of type [ch.qos.logback.classic.util.ContextSelectorStaticBinder]
 */
@SpringBootApplication
public class BackendApplication {
    private static final Logger LOG = LoggerFactory.getLogger(BackendApplication.class);

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(BackendApplication.class, args);
        ZooKeeper zoo = context.getBean("zoo", ZooKeeper.class);
        try {
            List<String> children = zoo.getChildren("/", false);
            LOG.info("TRY");
            children.forEach(LOG::info);
            LOG.info("TRY");
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}

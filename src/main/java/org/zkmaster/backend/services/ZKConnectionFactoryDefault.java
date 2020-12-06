package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.repositories.ZKNodeRepository;
import org.zkmaster.backend.repositories.ZKNodeRepositoryDefault;

import java.io.IOException;

@Service
public class ZKConnectionFactoryDefault implements ZKConnectionFactory {
    private final ApplicationContext context;

    @Autowired
    public ZKConnectionFactoryDefault(ApplicationContext context) {
        this.context = context;
    }


    /**
     * TODO - process IOException for business.
     *
     * @param host -
     * @return facade.
     */
    @Override
    public ZKNodeRepository makeConnectionByHost(String host) {
        ZKServer facade = null;
        try {
            if (host != null) {
                var watcher = context.getBean("watcherDefault", ZKWatcherDefault.class);
                watcher.setHost(host);

                facade = new ZKServer(host, 1000 * 20, watcher);
            } else {
                System.err.println(EXCEPTION_FAIL_CREATE_CONN + null);
            }
        } catch (IOException e) {
            System.err.println(EXCEPTION_FAIL_CREATE_CONN + host);
            e.printStackTrace();
        }
        return new ZKNodeRepositoryDefault(facade);
    }

    private static final String EXCEPTION_FAIL_CREATE_CONN =
            "Fail to create connection with real ZooKeeper server!"
                    + "Connection details: " + System.lineSeparator()
                    + "host: ";
}

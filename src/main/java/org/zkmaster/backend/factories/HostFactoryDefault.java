package org.zkmaster.backend.factories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.repositories.HostProvider;
import org.zkmaster.backend.repositories.HostProviderDefault;
import org.zkmaster.backend.repositories.HostProviderRWL;

import java.io.IOException;

@Service
public class HostFactoryDefault implements HostFactory {
    private final ApplicationContext context;

    @Autowired
    public HostFactoryDefault(ApplicationContext context) {
        this.context = context;
    }


    @Override
    public HostProvider makeHostProvider(String host) throws WrongHostAddressException {
        HostProvider rsl;
        try {
            if (host == null) {
                System.err.println(EXCEPTION_FAIL_CREATE_CONN + null);
                throw new WrongHostAddressException(null);
            }
            var watcher = context.getBean("watcherDefault", ZKWatcherDefault.class);
            watcher.setHost(host);
            Host facade = new Host(host, 1000 * 20, watcher);
            rsl = new HostProviderRWL(new HostProviderDefault(facade));
        } catch (IOException e) {
            System.err.println(EXCEPTION_FAIL_CREATE_CONN + host);
            e.printStackTrace();
            throw new WrongHostAddressException(host);
        }
        return rsl;
    }

    private static final String EXCEPTION_FAIL_CREATE_CONN =
            "Fail to create connection with real ZooKeeper server!"
                    + "Connection details: " + System.lineSeparator()
                    + "host: ";
}

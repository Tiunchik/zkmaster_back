package org.zkmaster.backend.services;

import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.entity.ZKWatcherDefault;
import org.zkmaster.backend.repositories.ZKNodeRepository;
import org.zkmaster.backend.repositories.ZKNodeRepositoryDefault;

import java.io.IOException;

@Service
public class ZKFactoryDefault implements ZKFactory {
    @Autowired
    private ApplicationContext context;

    /**
     * TODO - process IOException for business.
     *
     * @param hostUrl -
     * @return facade.
     */
    @Override
    public ZKServer makeFacadeFromHost(String hostUrl) {
        ZKServer facade = null;
        try {
            var watcher = context.getBean("watcherDefault", ZKWatcherDefault.class);
            watcher.setHostUrl(hostUrl);

            facade = ZKServer.of(hostUrl,
                    new ZooKeeper(hostUrl, 4000, watcher)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return facade;
    }

    /**
     * @param facade -
     * @return repository for work with server data.
     */
    @Override
    public ZKNodeRepository makeRepFromFacade(ZKServer facade) {
        return new ZKNodeRepositoryDefault(facade);
    }

}

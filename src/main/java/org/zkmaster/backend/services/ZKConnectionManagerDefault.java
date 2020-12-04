package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.repositories.ZKNodeRepository;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: Что делать с мапой ?
 */
@Service
public class ZKConnectionManagerDefault implements ZKConnectionManager {

    private final ZKFactory zkFactory;
    private final Map<String, ZKServer> connections = new HashMap<>();

    @Autowired
    public ZKConnectionManagerDefault(ZKFactory zkFactory) {
        this.zkFactory = zkFactory;
    }

    @Override
    public ZKNodeRepository createConnection(String hostUrl) {
        var facade = zkFactory.makeFacadeFromHost(hostUrl);
        connections.put(hostUrl, facade);
        return zkFactory.makeRepFromFacade(facade);
    }

    @Override
    public ZKNodeRepository reconnect(String hostUrl) {
        return null;
    }

    /**
     * Not implement yet.
     *
     * @param hostUrl -
     */
    @Override
    public void deleteConnection(String hostUrl) {
        connections.remove(hostUrl);
    }
}

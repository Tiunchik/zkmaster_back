package org.zkmaster.backend.services;

import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.repositories.ZKNodeRepository;

public interface ZKFactory {
    ZKServer makeFacadeFromHost(String hostUrl);

    ZKNodeRepository makeRepFromFacade(ZKServer facade);


}

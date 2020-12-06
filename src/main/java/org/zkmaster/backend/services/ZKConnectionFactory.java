package org.zkmaster.backend.services;

import org.zkmaster.backend.repositories.ZKNodeRepository;

public interface ZKConnectionFactory {

    ZKNodeRepository makeConnectionByHost(String host);
}

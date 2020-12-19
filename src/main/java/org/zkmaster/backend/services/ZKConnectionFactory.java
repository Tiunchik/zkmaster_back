package org.zkmaster.backend.services;

import org.zkmaster.backend.exceptions.WrongHostException;
import org.zkmaster.backend.repositories.ZKNodeRepository;

public interface ZKConnectionFactory {

    ZKNodeRepository makeConnectionByHost(String host) throws WrongHostException;
}

package org.zkmaster.backend.factories;

import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.repositories.HostProvider;

public interface HostFactory {

    HostProvider makeHostProvider(String host) throws HostWrongAddressException;

}

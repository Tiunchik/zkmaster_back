package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostNotFoundException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;

import java.util.List;
import java.util.Map;

public interface HostContext {

    ZKNode getActualHostValue(String host);

    /**
     * ??? Must throw exception, if doesn't found searchable {@link HostProvider}.
     *
     * @param host -
     * @return -
     */
    HostProvider getHostService(String host) throws HostNotFoundException;

    boolean createHost(String host) throws WrongHostAddressException;

    void refreshCache(String host);

    boolean containsHost(String host);

    void deleteHostAndCache(String host);

    Map<String, Boolean> checkHostsHealth(List<String> hosts);

}

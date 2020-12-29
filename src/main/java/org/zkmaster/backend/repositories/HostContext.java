package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.exceptions.node.NodeReadException;

import java.util.List;
import java.util.Map;

public interface HostContext {

    ZKNode getActualHostValue(String host) throws NodeReadException;

    /**
     * ??? Must throw exception, if doesn't found searchable {@link HostProvider}.
     *
     * @param host -
     * @return -
     */
    HostProvider getHostProvider(String host) throws HostProviderNotFoundException;

    boolean createHost(String host) throws WrongHostAddressException;

    void refreshCache(String host) throws NodeReadException;

    boolean containsHost(String host);

    void deleteHostAndCache(String host);

    Map<String, Boolean> checkHostsHealth(List<String> hosts);

}

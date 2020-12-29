package org.zkmaster.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.factories.HostFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Work with: servers cache && map of HostProvider.
 */
@Service
public class HostContextDefault implements HostContext {
    /**
     * key: host, key: HostProvider
     */
    private final Map<String, HostProvider> providers = new HashMap<>();
    /**
     * Actual cache of real server value.
     */
    private final Map<String, ZKNode> caches = new HashMap<>();
    @Autowired
    private HostFactory zkFactory;


    @Override
    public ZKNode getActualHostValue(String host) throws NodeReadException {
        ZKNode hostValue = caches.get(host); // value or null
        if (hostValue == null) { // if cache is null
            hostValue = providers.get(host).readHostValue();
            caches.put(host, hostValue);
        }
        return hostValue;
    }

    @Override
    public HostProvider getHostProvider(String host) throws HostProviderNotFoundException {
        HostProvider rsl = providers.get(host);
        if (rsl == null) {
            throw new HostProviderNotFoundException(host);
        }
        return rsl;
    }

    @Override
    public boolean createHost(String host) throws WrongHostAddressException {
        if (!providers.containsKey(host)) {
            providers.put(host, zkFactory.makeHostProvider(host));
        }
        return true;
    }

    @Override
    public void refreshCache(String host) throws NodeReadException {
        caches.put(host, providers.get(host).readHostValue());
    }

    @Override
    public boolean containsHost(String host) {
        return providers.containsKey(host);
    }

    @Override
    public void deleteHostAndCache(String host) {
        providers.remove(host);
        caches.remove(host);
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        var rsl = new HashMap<String, Boolean>();
        hosts.forEach(host -> rsl.put(host, providers.containsKey(host)));
        return rsl;
    }

}

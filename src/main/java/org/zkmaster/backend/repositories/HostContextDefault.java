package org.zkmaster.backend.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.ContextStateInfo;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.exceptions.HostCloseException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.node.NodeReadException;
import org.zkmaster.backend.factories.HostFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private HostFactory hostFactory;

    @Autowired
    @Qualifier("hostFactoryDefault")
//    @Qualifier("hostFactoryFake")
    @Override
    public void setHostFactory(HostFactory hostFactory) {
        this.hostFactory = hostFactory;
    }

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
    public boolean createHost(String host) throws HostWrongAddressException {
        if (!providers.containsKey(host)) {
            providers.put(host, hostFactory.makeHostProvider(host));
        }
        return true;
    }

    @Override
    public boolean refreshCache(String host) throws NodeReadException {
        caches.put(host, providers.get(host).readHostValue());
        return true;
    }

    @Override
    public boolean containsHost(String host) {
        return providers.containsKey(host);
    }

    @Override
    public void deleteHostAndCache(String host) throws HostCloseException {
        providers.remove(host).close();
        caches.remove(host);
    }
    
    @Override
    public ContextStateInfo getState() throws NodeReadException {
        Map<String, Integer> hostsAndNodeCount = new HashMap<>();
        for (String eachHost : providers.keySet()) {
            hostsAndNodeCount.put(eachHost, ZKNodes.treeNodeCount(getActualHostValue(eachHost)));
        }
        return new ContextStateInfo(providers.size(), hostsAndNodeCount);
    }
    
    @Override
    public Map<String, Boolean> containsHostAll(List<String> hosts) {
        var rsl = new HashMap<String, Boolean>();
        hosts.forEach(host -> rsl.put(host, providers.containsKey(host)));
        return rsl;
    }

    @Override
    public void clearContext() {
        providers.clear();
        caches.clear();
    }

}

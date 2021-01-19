package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostCloseException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.HostWrongAddressException;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.repositories.HostContext;

import java.util.List;
import java.util.Map;

@Service
public class MainServiceDefault implements MainService {
    HostContext ctx;

    @Autowired
    public MainServiceDefault(@Qualifier("hostContextRWL") HostContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean createNode(String host, String path, String value) throws NodeExistsException, HostProviderNotFoundException, NodeCreateException {
        return ctx.getHostProvider(host).createNode(path, value);
    }

    @Override
    public ZKNode getHostValue(String host) throws NodeReadException {
        return ctx.getActualHostValue(host);
    }

    @Override
    public boolean saveNode(String host, String path, String name, String value) throws NodeReadException, HostProviderNotFoundException, NodeSaveException {
        return ctx.getHostProvider(host).saveNode(path, name, value, ctx.getActualHostValue(host));
    }

    @Override
    public boolean deleteNode(String host, String path) throws NodeDeleteException, HostProviderNotFoundException, NodeReadException {
        return ctx.getHostProvider(host).deleteNode(path, ctx.getActualHostValue(host));
    }

    @Override
    public boolean refreshCache(String host) throws NodeReadException {
        return ctx.refreshCache(host);
    }

    @Override
    public boolean containsConnection(String host) {
        return ctx.containsHost(host);
    }

    @Override
    public boolean createConnection(String host) throws HostWrongAddressException {
        return ctx.createHost(host);
    }

    @Override
    public void deleteConnectionAndCache(String host) throws HostCloseException {
        ctx.deleteHostAndCache(host);
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        return ctx.containsHostAll(hosts);
    }

}

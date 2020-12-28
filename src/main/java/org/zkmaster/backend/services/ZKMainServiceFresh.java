package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.*;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.services.transform.TransformStrategy;

import java.util.List;
import java.util.Map;

@Service
public class ZKMainServiceFresh implements ZKMainService {
    HostContext ctx;
    TransformStrategy transformStrategy;

    @Autowired
    public ZKMainServiceFresh(@Qualifier("hostContextRWL") HostContext ctx,
                              TransformStrategy transformStrategy) {
        this.ctx = ctx;
        this.transformStrategy = transformStrategy;
    }

    @Override
    public boolean createNode(String host, String path, String value) throws NodeExistsException, HostNotFoundException {
        return ctx.getHostService(host).createNode(path, value);
    }

    @Override
    public ZKNode getHostValue(String host) {
        return ctx.getActualHostValue(host);
    }

    @Override
    public boolean saveNode(String host, String path, String name, String value) throws NodeSaveException, NodeRenameException, HostNotFoundException {
        return ctx.getHostService(host).saveNode(path, name, value, ctx.getActualHostValue(host));
    }

    @Override
    public boolean deleteNode(String host, String path) throws NodeDeleteException, HostNotFoundException {
        return ctx.getHostService(host).deleteNode(path);
    }

    @Override
    public void refreshCache(String host) {
        ctx.refreshCache(host);
    }

    @Override
    public boolean containsConnection(String host) {
        return ctx.containsHost(host);
    }

    @Override
    public boolean createConnection(String host) throws WrongHostAddressException {
        return ctx.createHost(host);
    }

    @Override
    public void deleteConnectionAndCache(String host) {
        ctx.deleteHostAndCache(host);
    }

    @Override
    public Map<String, Boolean> checkHostsHealth(List<String> hosts) {
        return ctx.checkHostsHealth(hosts);
    }

    @Override
    public List<String> exportHost(String host, String type) {
        return transformStrategy.exportHost(ctx.getActualHostValue(host), type);
    }

    @Override
    public boolean importData(String host, String type, List<String> data) throws ImportFailException, HostNotFoundException {
        return transformStrategy.importData(ctx.getHostService(host).transaction(), host, type, data);
    }

}

package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.DataImportFailException;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.WrongHostAddressException;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.repositories.HostContext;
import org.zkmaster.backend.services.transform.TransformStrategy;

import java.util.List;
import java.util.Map;

@Service
public class MainServiceFresh implements MainService {
    HostContext ctx;
    TransformStrategy transformStrategy;

    @Autowired
    public MainServiceFresh(@Qualifier("hostContextRWL") HostContext ctx,
                            TransformStrategy transformStrategy) {
        this.ctx = ctx;
        this.transformStrategy = transformStrategy;
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
    public boolean injectFromTo(InjectionDTO dto) throws InjectionFailException {
        return ctx.injectFromTo(dto);
    }

    @Override
    public List<String> exportHost(String host, String type) throws NodeReadException {
        return transformStrategy.exportHost(ctx.getActualHostValue(host), type);
    }

    @Override
    public boolean importData(String host, String type, List<String> data) throws DataImportFailException, HostProviderNotFoundException {
        return transformStrategy.importData(ctx.getHostProvider(host).transaction(), host, type, data);
    }

}

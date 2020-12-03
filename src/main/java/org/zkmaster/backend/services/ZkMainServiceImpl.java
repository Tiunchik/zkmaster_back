package org.zkmaster.backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKFactory;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.repositories.ZKDataRepository;

import java.util.HashMap;
import java.util.Map;

@Service
public class ZkMainServiceImpl {

    private Map<String, ZKDataRepository> repositoryMap = new HashMap<>();

    @Autowired
    private ZKFactory factory;

    public ZKNode getAll(String host, String path) {
        ZKNode node = null;
        if (repositoryMap.containsKey(host)) {
            node = repositoryMap.get(host).getNodes;
        } else {
            var newHost = factory.getOrCreateController(host);
            node = newHost.getRepository().getAllNodes(path);
            repositoryMap.put(host, ...);
        }
        return node;
    }

}

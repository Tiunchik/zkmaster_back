package org.zkmaster.backend.services.injection;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;

import java.util.List;

/**
 * Inject Node from one real-server into other real-serve, by provided params.
 */
public interface InjectionService {
    
    void injection(List<ZKNode> createNodeList, List<ZKNode> updateNodeList, String trgHost)
            throws InjectionFailException, HostProviderNotFoundException;

}

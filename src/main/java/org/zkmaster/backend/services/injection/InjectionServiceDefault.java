package org.zkmaster.backend.services.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.HostProviderNotFoundException;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.repositories.HostContext;

import java.util.List;

@Service
public class InjectionServiceDefault implements InjectionService {
    final HostContext ctx;
    
    @Autowired
    public InjectionServiceDefault(@Qualifier("hostContextRWL") HostContext ctx) {
        this.ctx = ctx;
    }
    
    
    @Override
    public void injection(List<ZKNode> createNodeList, List<ZKNode> updateNodeList, String trgHost)
            throws InjectionFailException, HostProviderNotFoundException {
        
        ZKTransaction trgHostTrans = ctx.getHostProvider(trgHost).transaction();
        createNodeList.forEach(node -> trgHostTrans.create(node.getPath(), node.getValue()));
        updateNodeList.forEach(node -> trgHostTrans.update(node.getPath(), node.getValue()));
        
        trgHostTrans.commit("Injection failed: Transaction failed!",
                new InjectionFailException(createNodeList, updateNodeList, trgHost));
    }
    
}

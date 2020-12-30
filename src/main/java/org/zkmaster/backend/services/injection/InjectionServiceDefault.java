package org.zkmaster.backend.services.injection;

import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.InjectionFailException;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InjectionServiceDefault implements InjectionService {

    @Override
    public boolean injectFromTo(ZKNode srcInjectionNode,
                                Set<String> trgHostPaths,
                                ZKTransaction trgHostTransaction,
                                InjectionDTO dto) throws InjectionFailException {
        final String srcPath = dto.getSourceNodePath();
        final String trgPath = dto.getTargetNodePath();
        var isTransactionEmpty = new AtomicBoolean(true);

        var corePath = ZKNodes.extractNodePathWithoutName(srcPath);
        boolean isSrcPathRootSubNode = corePath.equals("/");
        final String corePathFinal = (isSrcPathRootSubNode) ? " " : corePath;

        ZKNodes.treeIterateWidthList(srcInjectionNode, (node -> {
            var newNodePath = (isSrcPathRootSubNode)
                    ? trgPath + node.getPath()
                    : node.getPath().replace(corePathFinal, trgPath + "/");
            if (trgHostPaths.contains(newNodePath)) {
                if (dto.isUpdOldValues()) {
                    trgHostTransaction.update(newNodePath, node.getValue());
                    isTransactionEmpty.set(false);
                }
            } else {
                trgHostTransaction.create(newNodePath, node.getValue());
                isTransactionEmpty.set(false);
            }
        }));
        // NPE throw if transaction is empty and it was commited.
        if (!isTransactionEmpty.get()) {
            trgHostTransaction.commit("Injection failed: Transaction failed!",
                    new InjectionFailException(dto));
        }
        return true;
    }

}

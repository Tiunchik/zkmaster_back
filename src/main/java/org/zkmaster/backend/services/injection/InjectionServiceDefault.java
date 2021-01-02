package org.zkmaster.backend.services.injection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.node.NodeExistsException;
import org.zkmaster.backend.repositories.HostContext;

import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
public class InjectionServiceDefault implements InjectionService {
    final HostContext ctx;

    @Autowired
    public InjectionServiceDefault(@Qualifier("hostContextRWL") HostContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean injectFromTo(InjectionDTO dto) throws InjectionFailException {
        boolean rsl;
        try {
            rsl = injectFromTo(
                    ZKNodes.getSubNode(ctx.getActualHostValue(dto.getSourceHost()), dto.getSourceNodePath()),
                    ZKNodes.collectAllPaths(ctx.getActualHostValue(dto.getTargetHost())),
                    ctx.getHostProvider(dto.getTargetHost()).transaction(),
                    dto
            );
        } catch (Exception e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
            throw new InjectionFailException(dto);
        }
        return rsl;
    }

    /**
     * @param srcInjectionNode   Injectable Node.
     * @param trgHostPaths       {@link Set} of all targetHost Nodes path.
     *                           * Require for prevent {@link NodeExistsException},
     *                           by checking newNodePath in this Set.
     * @param trgHostTransaction targetHost transaction for create new nodes in target real-server.
     * @param dto                {@link InjectionDTO}, - special DTO for this API.
     * @return Injection success OR Throw Exception.
     * @throws InjectionFailException -
     */
    private boolean injectFromTo(ZKNode srcInjectionNode,
                                 Set<String> trgHostPaths,
                                 ZKTransaction trgHostTransaction,
                                 InjectionDTO dto) throws InjectionFailException {
        final String srcPath = dto.getSourceNodePath();
        final String trgPath = dto.getTargetNodePath();
        var isTransactionEmpty = new AtomicBoolean(true);

        var corePath = ZKNodes.pathWithoutName(srcPath);
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
        // NPE throw if transaction is empty and it was committed.
        if (!isTransactionEmpty.get()) {
            trgHostTransaction.commit("Injection failed: Transaction failed!",
                    new InjectionFailException(dto));
        }
        return true;
    }

}

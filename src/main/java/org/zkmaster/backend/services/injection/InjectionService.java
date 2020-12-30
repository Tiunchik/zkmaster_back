package org.zkmaster.backend.services.injection;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.dto.InjectionDTO;
import org.zkmaster.backend.exceptions.InjectionFailException;
import org.zkmaster.backend.exceptions.node.NodeExistsException;

import java.util.Set;

public interface InjectionService {

    /**
     * Inject Node from one real-server into other real-serve.
     *
     * @param srcInjectionNode   Injectable Node.
     * @param trgHostPaths       {@link Set} of all targetHost Nodes path.
     *                           * Require for prevent {@link NodeExistsException},
     *                           by checking newNodePath in this Set.
     * @param trgHostTransaction targetHost transaction for create new nodes in target real-server.
     * @param dto                {@link InjectionDTO}, - special DTO for this API.
     * @return Injection success OR Throw Exception.
     * @throws InjectionFailException -
     */
    boolean injectFromTo(ZKNode srcInjectionNode,
                         Set<String> trgHostPaths,
                         ZKTransaction trgHostTransaction,
                         InjectionDTO dto) throws InjectionFailException;

}

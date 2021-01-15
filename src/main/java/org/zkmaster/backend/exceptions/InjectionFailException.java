package org.zkmaster.backend.exceptions;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.dto.InjectionDTO;

import java.util.List;

/**
 * Meaning: Injection failed.
 * Possible: Any problems with real-serve or something wrong with transaction.
 */
public class InjectionFailException extends Exception {
    
    public InjectionFailException(InjectionDTO dto) {
        super("ZKM EXCEPTION: InjectionFailException:" + System.lineSeparator()
                + " sourceHost=" + dto.getSourceHost() + System.lineSeparator()
                + " sourceNodePath=" + dto.getSourceNodePath() + System.lineSeparator()
                + " targetHost=" + dto.getTargetHost() + System.lineSeparator()
                + " targetNodePath=" + dto.getSourceNodePath() + System.lineSeparator()
                + " saveOldValues=" + dto.isUpdOldValues() + System.lineSeparator()
                + " injection is failed.");
    }
    
    public InjectionFailException(List<ZKNode> createNodeLost, List<ZKNode> updateNodeLost,
                                  String trgHost) {
        super("ZKM EXCEPTION: InjectionFailException:" + System.lineSeparator()
                + "transaction host(" + trgHost + ')' + System.lineSeparator()
                + prettyArgs("createNodeLost", createNodeLost) + System.lineSeparator()
                + prettyArgs("updateNodeLost", updateNodeLost) + System.lineSeparator()
                + " injection is failed.");
    }
    
    /**
     * This thing {@code static}, - cause super constructor MUST be first thing in code,
     * and it happens before we can use non-static methods.
     */
    private static String prettyArgs(String name, List<ZKNode> nodeList) {
        var sb = new StringBuilder("content(" + name + ')');
        nodeList.forEach(node -> sb.append(node.getPath()).append(" : ").append(node.getValue()));
        return sb.toString();
    }
    
}

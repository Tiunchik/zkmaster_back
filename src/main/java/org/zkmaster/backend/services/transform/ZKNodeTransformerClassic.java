package org.zkmaster.backend.services.transform;

import org.springframework.stereotype.Component;
import org.zkmaster.backend.devutil.DevLog;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.utils.ZKNodes;

import java.util.LinkedList;
import java.util.List;

@Component("TXT")
public class ZKNodeTransformerClassic implements ZKNodeTransformer {
    
    /**
     * @implSpec Tree walk: deep && iterate.
     */
    @Override
    public List<String> exportHost(ZKNode exportNode) {
        DevLog.print("ZKNodeTransformerClassic", "method run");
        DevLog.print("ZKNodeTransformerClassic", "exportNode", exportNode);
        List<String> rsl = new LinkedList<>();
    
        String parentPath = ZKNodes.parentNodePath(exportNode.getPath());
        DevLog.print("ZKNodeTransformerClassic", "parentPath", parentPath);
    
        ZKNodes.treeIterateWidthList(exportNode, (currentNode -> {
            String currPath = currentNode.getPath().replace(parentPath, "");
            rsl.add(currPath + " : " + currentNode.getValue());
        }));
    
        DevLog.print("ZKNodeTransformerClassic", "method END");
        return rsl;
    }

    @Override
    public void importData(String nodePath, List<String> content, ZKTransaction transaction) {
        nodePath = ("/".equals(nodePath)) ? "/" : nodePath;
        for (String each : content) {
            var pathAndValue = each.split(" : ");
            transaction.create(nodePath + pathAndValue[0], pathAndValue[1]);
        }
    }
    

}

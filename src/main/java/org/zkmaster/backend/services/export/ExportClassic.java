package org.zkmaster.backend.services.export;

import org.springframework.stereotype.Component;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;

import java.util.LinkedList;
import java.util.List;

@Component
public class ExportClassic implements Export {

    /**
     * Tree walk: in deep by iterate.
     *
     * @param root hostValue.
     * @return Nodes in String format.
     */
    @Override
    public List<String> export(ZKNode root) {
        List<String> rsl = new LinkedList<>();

        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.addFirst(root);

        while (!treeWalkList.isEmpty()) {
            var currentNode = treeWalkList.removeFirst();

            if (ZKNodes.hasChildren(currentNode)) {
                currentNode.getChildren().forEach(treeWalkList::addFirst);
            }
            rsl.add(currentNode.getPath() + " : " + currentNode.getValue());
        }
        return rsl;

    }

}

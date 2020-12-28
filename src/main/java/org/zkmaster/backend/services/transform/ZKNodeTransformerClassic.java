package org.zkmaster.backend.services.transform;

import org.springframework.stereotype.Component;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;

import java.util.LinkedList;
import java.util.List;

@Component("TXT")
public class ZKNodeTransformerClassic implements ZKNodeTransformer {

    /**
     * @implSpec Tree walk: deep && iterate.
     */
    @Override
    public List<String> exportHost(ZKNode hostValue) {
        List<String> rsl = new LinkedList<>();

        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.addFirst(hostValue);

        while (!treeWalkList.isEmpty()) {
            var currentNode = treeWalkList.removeFirst();

            if (ZKNodes.hasChildren(currentNode)) {
                currentNode.getChildren().forEach(treeWalkList::addFirst);
            }
            rsl.add(currentNode.getPath() + " : " + currentNode.getValue());
        }
        return rsl;
    }

    @Override
    public void importData(List<String> data, ZKTransaction transaction) {
        for (String each : data) {
            var pathAndValue = each.split(" : ");
            transaction.create(pathAndValue[0], pathAndValue[1]);
        }
    }

//    private ZKNode importData(List<String> data) {
//        //        var root = new ZKNode();
//        var list = new LinkedList<ZKNode>();
//
//        for (int i = 10; i < data.size(); i++) {
//            var each = data.get(i).split(" : ");
//            var path = each[0];
//            var value = each[1];
//            DevLog.print("Import", "#" + i, "path==" + path + " value==" + value);
//            list.add(new ZKNode(path, value));
//        }
////        throw new NotImplementedException();
//        list.forEach(ZKNodes::printNode);
//
////        for (var e : list) {
////            ZKNodes.printNode();
////        }
//        throw new NotImplementedException();
//    }

}

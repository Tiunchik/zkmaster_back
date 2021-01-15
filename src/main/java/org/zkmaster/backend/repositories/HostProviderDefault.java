package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.exceptions.node.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class HostProviderDefault implements HostProvider {
    private final Host host;

    public HostProviderDefault(Host host) {
        this.host = host;
    }


    @Override
    public boolean createNode(String path, String value) throws NodeExistsException, NodeCreateException {
        return host.create(path, value);
    }

//    @Override
//    public ZKNode readHostValue() throws NodeReadException {
//        return readHostValue("/");
//    }
//
//    private ZKNode readHostValue(String path) throws NodeReadException {
////        DevLog.print("ReadHostVal", "arg^path", path);
//        String nodeValue = host.read(path);
//        List<ZKNode> children = new LinkedList<>();
//        List<String> childrenNames = host.getChildrenNames(path);
////        DevLog.print("ReadHostVal", "names", childrenNames);
//        if (childrenNames != null && !childrenNames.isEmpty()) {
//            for (var childName : childrenNames) {
//                String childPath = ("/".equals(path)) // is it root
//                        ? path + childName            // true
//                        : path + "/" + childName;     // false
////                DevLog.print("ReadHostVal", "childPath", childPath);
//                children.add(readHostValue(childPath)); // <<-- Recursion
//            }
//        }
//        return new ZKNode(path, nodeValue, children);
//    }

    @Override
    public ZKNode readHostValue() throws NodeReadException {
        ZKNode root = null;
        Map<String, ZKNode> mccp = new HashMap<>(); // short-cut of: multiChildrenParentPool

        Deque<String> iteratePaths = new LinkedList<>();
        iteratePaths.add("/");
        ZKNode prevNode = null;
        boolean firstIterate = true;

        while (!iteratePaths.isEmpty()) {
            String currPath = iteratePaths.removeLast();
            String parentPath = ZKNodes.parentNodePath(currPath);

            ZKNode currNode = host.readNode(currPath);
            prevNode = mccp.getOrDefault(parentPath, prevNode); // new parent || prev parent
            List<String> childrenPaths = host.getChildrenPaths(currPath);

            if (childrenPaths.size() >= 2) {
                mccp.put(currPath, currNode);
            }
            iteratePaths.addAll(childrenPaths);

            if (firstIterate) {
                firstIterate = false;
                root = currNode;
            } else {
                prevNode.addChildFirst(currNode);
            }
            prevNode = currNode;
        }
        return root;
    }

    /**
     * @implSpec Info:
     * * (Node name == {@param name}) >> Update Node value.
     * * (Node name != {@param name}) >> Rename Node and all sub-nodes.
     */
    @Override
    public boolean saveNode(String path, String name, String value, ZKNode actualCache)
            throws NodeSaveException {
        String oldName = ZKNodes.nameFromPath(path);
        return (oldName.equals(name))
                ? host.setData(path, value)
                : rename(name, value, ZKNodes.getSubNode(actualCache, path));
    }

    @Override
    public boolean deleteNode(String path, ZKNode actualCache) throws NodeDeleteException, NodeReadException {
        return (host.hasChildren(path))
                ? cascadeDelete(path, ZKNodes.getSubNode(actualCache, path))
                : host.delete(path);
    }

    @Override
    public ZKTransaction transaction() {
        return host.transaction();
    }


    /**
     * Iterate by {@param targetNode} with {@link ZKNodes#treeIterateWidthList(ZKNode, Consumer)}
     * and collect transaction for:
     * - delete old Nodes. (targetNode & all sub-nodes).
     * - create new Nodes. (targetNode with new Name and all sub Nodes with correct path).
     *
     * @param name       new Node Name.
     * @param value      (possible new) Node value.
     * @param targetNode Node for rename.(with inner nodes).
     * @return Rename success OR throw Exception.
     * @throws NodeSaveException -
     */
    private boolean rename(String name, String value, ZKNode targetNode)
            throws NodeSaveException {
        if (targetNode == null) {
            System.err.println("Rename failed: Transaction failed!");
            throw new NodeSaveException(host.getHostAddress(), null, name);
        }
        final ZKTransaction transaction = host.transaction();
        var deleteNodePaths = new LinkedList<String>();
        var firstIteration = new AtomicBoolean(true);
        var renameNodeName = targetNode.getName();

        ZKNodes.treeIterateWidthList(targetNode, currentNode -> {
            String rslValue = (firstIteration.getAndSet(false))
                    ? value : currentNode.getValue();
//            DevLog.print("Rename", "currentNode.name", currentNode.getName());
//            DevLog.print("Rename", "new name", name);
//            DevLog.print("Rename", "new currentNode", currentNode);
//            DevLog.print("Rename", "getPath", currentNode.getPath());
//            DevLog.print("Rename", "renameNodeName", renameNodeName);
            String newPath = currentNode.getPath().replace(renameNodeName, name);
//            DevLog.print("Rename", "newPath", newPath);
//            DevLog.print("Rename", "rslValue", rslValue);
            transaction.create(newPath, rslValue);
            deleteNodePaths.add(currentNode.getPath());
        });
        ZKTransaction.pushDeleteListReverse(transaction, deleteNodePaths);
        return transaction.commit("Rename failed: Transaction failed!",
                new NodeSaveException(host.getHostAddress(), targetNode.getPath(), name));
    }

    /**
     * Iterate by {@param targetNode} with {@link ZKNodes#treeIterateWidthList(ZKNode, Consumer)}
     * and collect transaction for: delete targetNode & all inner Nodes.
     */
    private boolean cascadeDelete(String path, ZKNode targetNode) throws NodeDeleteException {
        ZKTransaction transaction = host.transaction();
        var deleteNodePaths = new LinkedList<String>();

        ZKNodes.treeIterateWidthList(targetNode, currentNode ->
                deleteNodePaths.add(currentNode.getPath())
        );
        ZKTransaction.pushDeleteListReverse(transaction, deleteNodePaths);
        return transaction.commit("Delete failed: Transaction failed!",
                new NodeDeleteException(host.getHostAddress(), path));
    }

}

package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.*;
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
            boolean MCPPContainsParentPath = mccp.containsKey(parentPath);
            prevNode = (MCPPContainsParentPath) ? mccp.get(parentPath) : prevNode; // new parent || prev parent
            List<String> childrenPaths = host.getChildren(currPath);

            if (childrenPaths.size() >= 2) mccp.put(currPath, currNode);
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

//    /**
//     * @implSpec Tree walk: width && recursion.
//     */
//    @Override
//    @Deprecated(since = "recursion, risk to catch SOF")
//    public ZKNode readHostValue() throws NodeReadException {
//        return readHostValue("/", "/");
//    }
//
//    /**
//     * @implSpec Tree walk: width && recursion.
//     */
//    @Deprecated(since = "recursion, risk to catch SOF")
//    private ZKNode readHostValue(String path, String nodeName) throws NodeReadException {
//        String nodeValue = host.read(path);
//        List<ZKNode> children = new LinkedList<>();
//        List<String> childrenNames = host.getChildren(path);
//        if (childrenNames != null && !childrenNames.isEmpty()) {
//            for (var childName : childrenNames) {
//                String childPath = ("/".equals(path)) // is it root
//                        ? path + childName            // true
//                        : path + "/" + childName;     // false
//                children.add(readHostValue(childPath, childName)); // <<-- Recursion
//            }
//        }
//        return new ZKNode(path, nodeValue, nodeName, children);
//    }


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
        final ZKTransaction transaction = host.transaction();
        var deleteNodePaths = new LinkedList<String>();
        var firstIteration = new AtomicBoolean(true);

        ZKNodes.treeIterateWidthList(targetNode, currentNode -> {
            String rslValue = (firstIteration.getAndSet(false))
                    ? value : currentNode.getValue();
            String newPath = currentNode.getPath().replace(currentNode.getName(), name);
            transaction.create(newPath, rslValue);
            deleteNodePaths.add(currentNode.getPath());
        });
        ZKTransactions.pushDeleteListReverse(transaction, deleteNodePaths);
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
        ZKTransactions.pushDeleteListReverse(transaction, deleteNodePaths);
        return transaction.commit("Delete failed: Transaction failed!",
                new NodeDeleteException(host.getHostAddress(), path));
    }

}

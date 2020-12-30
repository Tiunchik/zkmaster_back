package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * TODO Сделать обход в ширину без рекурсии при собрании getAll в репозитории,
 * TODO у Макса никогда руки не дойдут, и так фронт нихрена не делает.
 */

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
        return readHostValue("/", "/");
    }

//    /**
//     * TODO weak - write tree-walk by list walk, not recursion.
//     * @param rootPath -
//     * @param rootName -
//     * @return -
//     */
//    private ZKNode readHostValue(String rootPath, String rootName) {
//        String rootValue = host.read(rootPath);
//
//
//        LinkedList<ZKNode> searchList = new LinkedList<>();
//        List<ZKNode> treeWalkList = new LinkedList<>();
//        searchList.add(new ZKNode(rootPath, host.read(rootPath)));
//        ZKNode currentNode;
//        while (!searchList.isEmpty()) {
//            currentNode = searchList.getFirst();
////            List<String> childrenPaths =
//        }
//        return new ZKNode(rootPath, rootValue, rootName, chi);
//    }

    /**
     * @implSpec Tree walk: width && recursion.
     */
    private ZKNode readHostValue(String path, String nodeName) throws NodeReadException {
        String nodeValue = host.read(path);
        List<ZKNode> children = new LinkedList<>();
        List<String> childrenNames = host.getChildren(path);
        if (childrenNames != null && !childrenNames.isEmpty()) {
            for (var childName : childrenNames) {
                String childPath = ("/".equals(path)) // is it root
                        ? path + childName            // true
                        : path + "/" + childName;     // false
                children.add(readHostValue(childPath, childName)); // <<-- Recursion
            }
        }
        return new ZKNode(path, nodeValue, nodeName, children);
    }


    /**
     * @implSpec Info:
     * * (Node name == {@param name}) >> Update Node value.
     * * (Node name != {@param name}) >> Rename Node and all sub-nodes.
     */
    @Override
    public boolean saveNode(String path, String name, String value, ZKNode actualCache)
            throws NodeSaveException {
        String oldName = ZKNodes.extractNodeName(path);
        return (oldName.equals(name))
                ? host.setData(path, value)
                : rename(name, value, ZKNodes.getSubNode(actualCache, path));
    }

    @Override
    public boolean deleteNode(String path, ZKNode actualCache) throws NodeDeleteException {
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
        while (!deleteNodePaths.isEmpty()) {
            transaction.delete(deleteNodePaths.removeLast());
        }
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
        while (!deleteNodePaths.isEmpty()) {
            transaction.delete(deleteNodePaths.removeLast());
        }
        return transaction.commit("Delete failed: Transaction failed!",
                new NodeDeleteException(host.getHostAddress(), path));
    }

}

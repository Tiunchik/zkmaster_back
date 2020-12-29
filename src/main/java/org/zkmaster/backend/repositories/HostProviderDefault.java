package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;

import java.util.LinkedList;
import java.util.List;

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

    @Override
    public ZKNode getSubNode(ZKNode root, String path) {
        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.add(root);
        ZKNode rsl = null;

        while (!treeWalkList.isEmpty()) {
            var current = treeWalkList.removeFirst();
            if (current.getPath().equals(path)) {
                rsl = current;
                break;
            } else if (ZKNodes.hasChildren(current)) {
                treeWalkList.addAll(current.getChildren());
            }
        }
        return rsl;
    }

    @Override
    public boolean saveNode(String path, String name, String value, ZKNode actualCache)
            throws NodeSaveException {
        String oldName = ZKNodes.extractNodeName(path);
        return (oldName.equals(name))
                ? set(path, value)
                : rename(path, name, value, getSubNode(actualCache, path));
    }

    @Override
    public boolean deleteNode(String path) throws NodeDeleteException {
        return host.delete(path);
    }

    @Override
    public ZKTransaction transaction() {
        return host.transaction();
    }

    private boolean set(String path, String value) throws NodeSaveException {
        return host.setData(path, value);
    }

    /**
     * Spec: Tree walk: width && iterate.
     */
    private boolean rename(String path, String name, String value, ZKNode targetNode)
            throws NodeSaveException {
        ZKTransaction transaction = host.transaction();

        var deleteNodePaths = new LinkedList<String>();
        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.add(targetNode);

        var corePathWithoutName = path.substring(0, path.lastIndexOf('/') + 1);
        var oldName = path.substring(path.lastIndexOf('/') + 1);
        boolean firstIteration = true;

        while (!treeWalkList.isEmpty()) {
            ZKNode currentZKNode = treeWalkList.removeFirst();
            String rslValue = (firstIteration) ? value : currentZKNode.getValue();
            firstIteration = false;

            if (ZKNodes.hasChildren(currentZKNode)) {
                treeWalkList.addAll(currentZKNode.getChildren());
            }
            deleteNodePaths.add(currentZKNode.getPath());
            transaction.create(
                    ZKNodes.replacePath(
                            currentZKNode.getPath(),
                            corePathWithoutName, name, oldName),
                    rslValue
            );
        }

        while (!deleteNodePaths.isEmpty()) {
            transaction.delete(deleteNodePaths.removeLast());
        }

        return transaction.commit("Rename failed: Transaction failed!",
                new NodeSaveException(host.getHostAddress(), path, name));
    }

}

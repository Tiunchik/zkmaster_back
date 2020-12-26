package org.zkmaster.backend.repositories;

import org.apache.zookeeper.KeeperException;
import org.zkmaster.backend.aop.Log;
import org.zkmaster.backend.devutil.DevLog;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.NodeExistsException;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO Сделать обход в ширину без рекурсии при собрании getAll в репозитории,
 * TODO у Макса никогда руки не дойдут, и так фронт нихрена не делает.
 */
public class ZKNodeRepositoryDefault implements ZKNodeRepository {
    private final ZKServer zkServer;

    public ZKNodeRepositoryDefault(ZKServer zkServer) {
        this.zkServer = zkServer;
    }


    @Override
    public boolean create(String path, String value) throws NodeExistsException {
        zkServer.create(path, value);
        return true;
    }

    @Override
    public ZKNode getHostValue() {
        return getHostValue("/", "/");
    }

//    /**
//     * TODO weak - write tree-walk by list walk, not recursion.
//     * @param path -
//     * @param nodeName -
//     * @return -
//     */
//    private ZKNode getHostValueList(String path, String nodeName) {
//        LinkedList<ZKNode> searchList = new LinkedList<>();
//        List<ZKNode> foundList = new ArrayList<>(500);
//        searchList.add(new ZKNode(path, zkServer.read(path)));
//        ZKNode currentNode;
//        while (!searchList.isEmpty()) {
//            currentNode = searchList.getFirst();
////            List<String> childrenPaths =
//        }
//        return null;
//    }

    /**
     * RECURSION!!!
     *
     * @param path     -
     * @param nodeName -
     * @return -
     */
//    @Deprecated(since = "recursion")
    private ZKNode getHostValue(String path, String nodeName) {
        String nodeValue = zkServer.read(path);
        List<ZKNode> children = new LinkedList<>();
        List<String> childrenNames = zkServer.getChildren(path);
        if (childrenNames != null && !childrenNames.isEmpty()) {
            for (var childName : childrenNames) {
                String childPath = ("/".equals(path)) // is it root
                        ? path + childName            // true
                        : path + "/" + childName;     // false
                children.add(getHostValue(childPath, childName)); // <<-- Recursion
            }
        }
        return new ZKNode(path, nodeValue, nodeName, children);
    }

    /**
     * @param root - start node. Default=hostValue(root).
     * @param path - absolute path of searching {@link ZKNode}.
     * @return result || null ==>> if searching {@link ZKNode} doesn't found.
     * @implNote Tree-travel: width by List.
     */
    @Override
    public ZKNode getSubNode(ZKNode root, String path) {
        var nodeList = new LinkedList<ZKNode>();
        nodeList.add(root);
        ZKNode rsl = null;

        while (!nodeList.isEmpty()) {
            var current = nodeList.removeFirst();

            if (current.getPath().equals(path)) {
                rsl = current;
                DevLog.print("Sub Node", "break, current", rsl);
                break;
            } else if (ZKNodes.hasChildren(current)) {
                nodeList.addAll(current.getChildren());
            }
        }
        return rsl;
    }

    @Override
    public boolean set(String path, String value) {
        return zkServer.setData(path, value);
    }

    @Override
    public boolean delete(String path) {
        return zkServer.delete(path);
    }

    @Override
    @Log
    public boolean rename(String path, String name, String value, ZKNode hostValue)
            throws KeeperException, InterruptedException {
        ZKTransaction transaction = zkServer.transaction();

        ZKNode targetNode = getSubNode(hostValue, path);
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
        transaction.commit();
        return true;
    }

}

package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;
import org.zkmaster.backend.exceptions.NodeExistsException;

import java.util.LinkedList;
import java.util.List;

/**
 * TODO Сделать обход в ширину без рекурсии при собрании getAll в репозитории,
 * TODO у Макса никогда руки не дойдут, и так фронт нихрена не делает
 */
public class ZKNodeRepositoryDefault implements ZKNodeRepository {
    private final ZKServer zkServer;

    public ZKNodeRepositoryDefault(ZKServer zkServer) {
        this.zkServer = zkServer;
    }


    /**
     * @param path  -
     * @param value -
     * @return -
     */
    @Override
    public boolean create(String path, String value) throws NodeExistsException {
        zkServer.create(path, value);
        return true;
    }

    /**
     * TODO -Method: ^_^ Ja zdju svoego Ljubimogo Maksju!!!
     */
    @Override
    @Deprecated
    public ZKNode getSimpleNode(String path) {
        return null;
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
    @Deprecated(since = "recursion")
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

    @Override
    public boolean set(String path, String value) {
        return zkServer.setData(path, value);
    }

    @Override
    public boolean delete(String path) {
        return zkServer.delete(path);
    }

}

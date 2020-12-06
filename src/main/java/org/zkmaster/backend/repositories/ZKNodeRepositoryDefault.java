package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO Сделать обход в ширину без рекурсии при собрании getAll в репозитории, у Макса никогда руки не дойдут, и так фронт нихрена не делает
 */
public class ZKNodeRepositoryDefault implements ZKNodeRepository {
    private final ZKServer zkServer;

    public ZKNodeRepositoryDefault(ZKServer zkServer) {
        this.zkServer = zkServer;
    }


    /**
     * @param path  -
     * @param value -
     * @return {true} - unless any exception is thrown.
     */
    @Override
    public boolean create(String path, String value) {
        zkServer.create(path, value);
        return true;
    }

    /**
     * TODO -Method: ^_^ Ja zdju svoego Ljubimogo Maksju!!!
     */
    @Override
    public ZKNode getSimpleNode(String path) {
        return null;
    }

    /**
     * TODO -Method: ^_^ Ja zdju svoego Ljubimogo Maksju!!!
     */
    @Override
    public ZKNode getFullNode(String path) {
//
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
//    @Deprecated // recursion
        String nodeValue = zkServer.read(path);
        List<ZKNode> children = null;
        var getChildren = zkServer.getChildren(path);
        if (getChildren != null && !getChildren.isEmpty()) {
            children = new ArrayList<>();
            for (var childName : getChildren) {

                String childPath = ("/".equals(path))
                        ? path + childName
                        : path + "/" + childName;
                children.add(getFullNode(childPath)); // <<-- Recursion
            }
        }
        return new ZKNode(path, nodeValue, children);
    }

    @Override
    public boolean set(String path, String value) {
        return zkServer.setData(path, value);
    }

    @Override
    public boolean delete(String path) {
        return zkServer.delete(path);
    }

    /**
     * {@link ZKNodeRepository} - gljanj tam vsju docu dlja tebja.
     *
     * @param node - ???????
     * @return ???????
     */
    @Override
    public String getFullPath(ZKNode node) {
        String fullPath = "";
//        while
        return null;
    }
}


package org.zkmaster.backend.repositories;

import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
     * TODO - Maksja, tebe tut stradatj nemnogno. ;)
     *
     * @param path -
     * @return -
     */
    @Override
    public ZKNode getSimpleNode(String path) {
        return null;
    }

    /**
     * TODO - Maksja, tebe tut stradatj nemnogno. ;)
     *
     * @param path -
     * @return -
     */
    @Override
    public ZKNode getFullNode(String path) {

        LinkedList<ZKNode> searchList = new LinkedList<>();
        List<ZKNode> foundList = new ArrayList<>(500);
        searchList.add(new ZKNode(path, zkServer.read(path)));
        ZKNode currentNode;
        while (!searchList.isEmpty()) {
            currentNode = searchList.getFirst();
//            List<String> childrenPaths =
        }
        return null;
    }

//    @Deprecated // recursion
//    private ZKNode getFullTree(String path) {
//        try {
//            String nodeValue = zkServer.read(path);
//
//            List<ZKNode> children = null;
//            var getChildren = zkServer.getChildren(path);
//            if (getChildren != null && !getChildren.isEmpty()) {
//                children = new ArrayList<>();
//                for (var childName : getChildren) {
//
//                    String childPath = ("/".equals(path))
//                            ? path + childName
//                            : path + "/" + childName;
//                    children.add(getFullTree(childPath)); // <<-- Recursion
//                }
//            }
//            return new ZKNode(path, nodeValue, children);
//        } catch (InterruptedException | KeeperException e) {
//            System.err.println("nude doesn't find!");
//            e.printStackTrace();
//            return null;
//        }
//    }

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


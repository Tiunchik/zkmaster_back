package org.zkmaster.backend.repositories;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ZKDataRepository implements ZKDataCrudRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ZKDataRepository.class);

    private final ZKServer zkServer;

    public ZKDataRepository(ZKServer zkServer) {
        this.zkServer = zkServer;
    }


    @Override
    public ZKNode createNode(String path, String value) {
        zkServer.create(path, value);
        return null;
    }

    @Override
    public ZKNode getNode(String path) {
        return null;
    }


    @Override
    public ZKNode getAllNodes(String path) {

        LinkedList<ZKNode> searchList = new LinkedList<>();
        List<ZKNode> foundList = new ArrayList<>(500);
        searchList.add(new ZKNode(path, zkServer.read(path)));
        ZKNode currentNode;
        while (!searchList.isEmpty()) {
            currentNode = searchList.getFirst();
//            List<String> childrenPaths =
        }
//        try {
////        print("path(current)", path);
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
//                    children.add(getFullNudeOrNull(childPath)); // <<-- Recursion
//                }
//            }
//            return new ZKNode(path, nodeValue, children);
//        } catch (InterruptedException | KeeperException e) {
//            LOG.error("nude doesn't find!");
//            e.printStackTrace();
//            return null;
//        }
    }

    @Override
    public boolean setData(String path, String value) {
        return false;
    }

    @Override
    public boolean deleteNode(String path) {
        return false;
    }

    private String getFullPath(ZKNode node) {
        String fullPath = "";
        while
    }
}


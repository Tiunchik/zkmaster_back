package org.zkmaster.backend.repositories;

import org.apache.zookeeper.KeeperException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKServer;

import java.util.ArrayList;
import java.util.List;

public class ZKDataRepository implements ZKDataCrudRepository {
    private static final Logger LOG = LoggerFactory.getLogger(ZKDataRepository.class);

    private final ZKServer zkServer;

    public ZKDataRepository(ZKServer zkServer) {
        this.zkServer = zkServer;
    }


    @Override
    public ZKNode create(String path, String value) {
        zkServer.create(path, value);
        return null;
    }

    @Override
    public ZKNode getNude(String path) {
        return null;
    }


    @Override
    public ZKNode getFullNudeOrNull(String path) {
        try {
//        print("path(current)", path);
            String nodeValue = zkServer.read(path);

            List<ZKNode> children = null;
            var getChildren = zkServer.getChildren(path);
            if (getChildren != null && !getChildren.isEmpty()) {
                children = new ArrayList<>();
                for (var childName : getChildren) {

                    String childPath = ("/".equals(path))
                            ? path + childName
                            : path + "/" + childName;
                    children.add(getFullNudeOrNull(childPath)); // <<-- Recursion
                }
            }
            return new ZKNode(path, nodeValue, children);
        } catch (InterruptedException | KeeperException e) {
            LOG.error("nude doesn't find!");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean setData(String path, String value) {
        return false;
    }

    @Override
    public boolean deleteNude(String path) {
        return false;
    }
}

package org.zkmaster.backend.entity;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Warp default ZooKeeper API into comfortable API.
 * <p>
 * Default warping.
 */
public class ZKServer extends ZKServerDecorator {

    public ZKServer(String hostUrl, ZooKeeper zoo) {
        super(hostUrl, zoo);
    }

    public static ZKServer of(String hostUrl, ZooKeeper zoo) {
        return new ZKServer(hostUrl, zoo);
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path  -
     * @param value -
     */
    @Override
    public void create(String path, String value) {
        try {
            zoo.create(path,
                    value.getBytes(),
                    ZooDefs.Ids.OPEN_ACL_UNSAFE,
                    CreateMode.PERSISTENT);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
    }

    /**
     * Default wrap. Don't use Watcher and State.
     *
     * @param path -
     * @return String value by nude.
     */
    @Override
    public String read(String path) {
        String rsl = null;
        try {
            rsl = new String(zoo.getData(path, false, null), StandardCharsets.UTF_8);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * !!! If the given version is -1, it matches any node's versions.
     *
     * @param path  -
     * @param value -
     * @return - update complete success or not.
     */
    @Override
    public boolean setData(String path, String value) {
        boolean rsl = true;
        try {
            zoo.setData(path, value.getBytes(), -1);
        } catch (KeeperException | InterruptedException e) {
            rsl = false;
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * !!! If the given version is -1, it matches any node's versions.
     *
     * @param path -
     * @return - delete complete success or not.
     */
    @Override
    public boolean delete(String path) {
        boolean rsl = true;
        try {
            zoo.delete(path, -1);
        } catch (InterruptedException | KeeperException e) {
            rsl = false;
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * Default wrap, doesn't use watch and State.
     *
     * @param path -
     * @return children names OR null.
     */
    @Override
    public List<String> getChildren(String path) {
        List<String> rsl = null;
        try {
            rsl = zoo.getChildren(path, false, null);
        } catch (KeeperException | InterruptedException e) {
            System.err.println("Something Wrong! Check it.");
            e.printStackTrace();
        }
        return rsl;
    }

}

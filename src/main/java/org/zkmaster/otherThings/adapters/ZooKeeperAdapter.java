package org.zkmaster.otherThings.adapters;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * 2 reasons why it exists:
 * - extra documentation.
 * - (would be) addition features.
 */
public class ZooKeeperAdapter {
    private final ZooKeeper original;

    public ZooKeeperAdapter(ZooKeeper original) {
        this.original = original;
    }

    /**
     * @param path    node path in server.
     * @param watcher ?????????????????????????????????????????
     * @param stat    can be null, or empty {@link Stat}.
     *                it's buffer object and method will copy data state into {@param state}
     * @return bytes[] :: String - How to decode:
     * {@code String value = new String(bytes, StandardCharsets.UTF_8); }
     *
     * <p>
     * Workable StandardCharsets (only):
     * StandardCharsets.UTF_8
     * StandardCharsets.US_ASCII
     * StandardCharsets.ISO_8859_1
     * </>
     * @throws KeeperException      -
     * @throws InterruptedException -
     */
    public byte[] getData(final String path, Watcher watcher, Stat stat)
            throws KeeperException, InterruptedException {

        return original.getData(path, watcher, stat);
    }

    /**
     * @return {@link ZooKeeper.States} - Enum with session state.
     * <p>meaning:
     * CONNECTING
     * ASSOCIATING
     * CONNECTED
     * CONNECTEDREADONLY
     * CLOSED
     * AUTH_FAILED
     * NOT_CONNECTED
     * </>
     */
    public ZooKeeper.States getState() {
        return original.getState();
    }

}

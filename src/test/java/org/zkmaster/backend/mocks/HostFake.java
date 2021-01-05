package org.zkmaster.backend.mocks;

import org.apache.zookeeper.Watcher;
import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.entity.utils.ZKNodes;
import org.zkmaster.backend.exceptions.node.*;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class HostFake implements Host {
    private final String hostAddress;
    private final ZKNode root;
    private Watcher watcherFake;
    private final EventFactory eventFactory = new EventFactory();


    public HostFake(String hostAddress, ZKNode root) {
        this.hostAddress = hostAddress;
        this.root = root;
    }

    /**
     * TODO - repair for CRUD tests.
     */
    @Override
    public boolean create(String path, String value) throws NodeExistsException, NodeCreateException {
        String parentNodePath = ZKNodes.parentNodePath(path);
        var subNode = ZKNodes.getSubNode(root, parentNodePath);
        subNode.addChildFirst(new ZKNode(path, value));
        watcherFake.process(eventFactory.makeNodeChildrenChangedEvent(parentNodePath));
        return true;
    }

    @Override
    public String read(String path) throws NodeReadException {
        return ZKNodes.getSubNode(root, path).getValue();
    }

    /**
     * Проверить CRUD - Update - Update Event
     */
    @Override
    public boolean setData(String path, String value) throws NodeSaveException {
        ZKNodes.getSubNode(root, path).setValue(value);
        final String parentPath = ZKNodes.parentNodePath(path);
        watcherFake.process(eventFactory.makeNodeChildrenChangedEvent(parentPath));
        watcherFake.process(eventFactory.makeNodeDeletedEvent(path));
        return true;
    }

    @Override
    public boolean delete(String path) throws NodeDeleteException {
        final String parentPath = ZKNodes.parentNodePath(path);
        ZKNodes.getSubNode(root, parentPath).getChildren()
                .removeIf(node -> path.equals(node.getPath()));

        watcherFake.process(eventFactory.makeNodeChildrenChangedEvent(parentPath));
        watcherFake.process(eventFactory.makeNodeDeletedEvent(path));
        return true;
    }

    @Override
    public List<String> getChildrenNames(String path) {
        return ZKNodes.getSubNode(root, path).getChildren().stream()
                .map(ZKNode::getPath).collect(Collectors.toList());
    }

    @Override
    public List<String> getChildrenPaths(String path) throws NodeReadException {
        return ZKNodes.getSubNode(root, path).getChildren().stream()
                .map(ZKNode::getPath).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren(String path) {
        return ZKNodes.getSubNode(root, path).hasChildren();
    }

    /**
     * @implSpec Nullify children, cause default API say
     * that with method can't return Node with children.
     */
    @Override
    public ZKNode readNode(String path) {
        ZKNode rsl = ZKNodes.getSubNode(root, path);
        return new ZKNode(rsl.getPath(), rsl.getValue(), new LinkedList<>());
    }

    @Override
    public ZKTransaction transaction() {
        return new ZKTransactionFake(this);
    }

    @Override
    public String getHostAddress() {
        return this.hostAddress;
    }

    public void setWatcherFake(Watcher watcherFake) {
        this.watcherFake = watcherFake;
        watcherFake.process(eventFactory.makeConnectionCreateEvent());
    }

    @Override
    public void close() throws Exception {
        System.out.println("HostFake :: method=close() - invoked");
    }


    private static class EventFactory {
        private org.apache.zookeeper.WatchedEvent makeConnectionCreateEvent() {
            return new org.apache.zookeeper.WatchedEvent(
                    Watcher.Event.EventType.None,
                    Watcher.Event.KeeperState.SyncConnected,
                    null
            );
        }

        private org.apache.zookeeper.WatchedEvent makeNodeChildrenChangedEvent(String nodePath) {
            return new org.apache.zookeeper.WatchedEvent(
                    Watcher.Event.EventType.NodeChildrenChanged,
                    Watcher.Event.KeeperState.SyncConnected,
                    nodePath
            );
        }

        private org.apache.zookeeper.WatchedEvent makeNodeDeletedEvent(String nodePath) {
            return new org.apache.zookeeper.WatchedEvent(
                    Watcher.Event.EventType.NodeDeleted,
                    Watcher.Event.KeeperState.SyncConnected,
                    nodePath
            );
        }

    }

}

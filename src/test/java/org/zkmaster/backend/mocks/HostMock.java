package org.zkmaster.backend.mocks;

import org.zkmaster.backend.entity.Host;
import org.zkmaster.backend.entity.ZKNode;
import org.zkmaster.backend.entity.ZKNodes;
import org.zkmaster.backend.entity.ZKTransaction;
import org.zkmaster.backend.exceptions.node.*;
import org.zkmaster.backend.zexprun.mocks.ZKTransactionFake;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Work by ZKNode, not List
 * TODO : TEST IT!!!
 */
public class HostMock implements Host {
    private String hostAddress;
    private ZKNode root;

    public HostMock(String hostAddress) {
//        this.hostAddress = hostAddress;
//        this.root = new ZKNode("/", "");
    }

    public HostMock(String hostAddress, ZKNode root) {
        this.hostAddress = hostAddress;
        this.root = root;
    }

    @Override
    public boolean create(String path, String value) throws NodeExistsException, NodeCreateException {
        var temp = ZKNodes.getSubNode(root, ZKNodes.parentNodePath(path));
        return false;
    }

    @Override
    public String read(String path) throws NodeReadException {
        return ZKNodes.getSubNode(root, path).getValue();
    }

    @Override
    public boolean setData(String path, String value) throws NodeSaveException {
        ZKNodes.getSubNode(root, path).setValue(value);
        return true;
    }

    @Override
    public boolean delete(String path) throws NodeDeleteException {
        final String parentPath = path.substring(0, path.lastIndexOf('/'));
        var temp = ZKNodes.getSubNode(root, parentPath).getChildren();
        for (int i = 0; i < temp.size(); i++) {
            if (path.equals(temp.get(i).getPath())) {
                temp.remove(i);
                break;
            }
        }
        return true;
    }

    @Override
    public List<String> getChildrenNames(String path) {
//        List<String> paths = new LinkedList<>();
//        for (ZKNode each : ZKNodes.getSubNode(root, path).getChildren()) {
//            paths.add(each.getName());
//        }
//        return paths;
        return ZKNodes.getSubNode(root, path).getChildren().stream()
                .map(ZKNode::getName).collect(Collectors.toList());
    }

    @Override
    public List<String> getChildrenPaths(String path) throws NodeReadException {
        return ZKNodes.getSubNode(root, path).getChildren().stream()
                .map(ZKNode::getPath).collect(Collectors.toList());
    }

    @Override
    public boolean hasChildren(String path) {
        return ZKNodes.hasChildren(ZKNodes.getSubNode(root, path));
    }

    /**
     * @implSpec Nullify children, cause default API say
     * that with method can't return Node with children.
     */
    @Override
    public ZKNode readNode(String path) {
        ZKNode rsl = ZKNodes.getSubNode(root, path);
        return new ZKNode(rsl.getPath(), rsl.getValue(), rsl.getName(), new LinkedList<>());
    }

    @Override
    public ZKTransaction transaction() {
        return new ZKTransactionFake(this);
    }

    @Override
    public String getHostAddress() {
        return this.hostAddress;
    }

    @Override
    public void close() throws Exception {
        System.out.println("HostFake :: method=close() - invoked");
    }


}

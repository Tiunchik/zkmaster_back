package org.zkmaster.backend.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * <p> Main Entity for work with ZooKeeper. It's a tree data structure.
 * It contains information like one ZooKeeper node AND children of this Node.
 *
 * <p> As it looks like a tree-collection, it have util methods for work with it: {@link ZKNodes}.
 *
 * <p> It could be:
 * # host-value :: meaning that Node is: full view of real-server(root with all sub-nodes).
 * # sub-node   :: meaning that Node is: inner node, not a Root. (May have children).
 * # leaf       :: meaning that Node is: no children Node.
 *
 * @see ZKNodes - Util class for work with this entity.
 */
public class ZKNode {

    /**
     * This Node absolute path.
     */
    private String path;

    /**
     * This Node data value. Converted from bytes(default by UTF-8).
     */
    private String value;

    /**
     * This Node name.
     */
    private String name;

    /**
     * This Node children.
     */
    private List<ZKNode> children = new LinkedList<>();

    public ZKNode() {
    }

    public ZKNode(String path, String value) {
        this.path = path;
        this.value = value;
        this.name = ZKNodes.nameFromPath(path);
        this.children = new LinkedList<>();
    }

    public ZKNode(String path, String value, List<ZKNode> children) {
        this.path = path;
        this.value = value;
        this.name = ZKNodes.nameFromPath(path);
        if (children instanceof LinkedList) {
            this.children = children;
        } else {
            this.children.addAll(children);
        }
    }

    public void addChildFirst(ZKNode child) {
        var temp = (LinkedList<ZKNode>) this.children;
        temp.addFirst(child);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<ZKNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZKNode> children) {
        this.children = children;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        ZKNode zkNode = (ZKNode) object;
        return Objects.equals(path, zkNode.path)
                && Objects.equals(value, zkNode.value)
                && Objects.equals(name, zkNode.name)
                && Objects.equals(children, zkNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, value, name, children);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ZKNode.class.getSimpleName() + "[", "]")
                .add("path='" + path + "'")
                .add("value='" + value + "'")
                .add("name='" + name + "'")
                .add("children=" + children)
                .toString();
    }

}

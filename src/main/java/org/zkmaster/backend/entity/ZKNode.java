package org.zkmaster.backend.entity;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ZKNode {

    /**
     * Full path of "this" "node" on ZK server.
     */
    private String path;

    /**
     * data value from ZK.
     * * converted from bytes. (default by UTF-8)
     */
    private String value;

    /**
     * name of node.
     */
    private String name;

    /**
     * List of children "node".
     */
    private List<ZKNode> children = new LinkedList<>();

    public ZKNode() {
    }

    public ZKNode(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public ZKNode(String path, String value, String name, List<ZKNode> children) {
        this.path = path;
        this.value = value;
        this.name = name;
        this.children = children;
    }

    public ZKNode(String path, String value, List<ZKNode> children) {
        this.path = path;
        this.value = value;
        this.children = children;
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

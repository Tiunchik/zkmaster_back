package org.zkmaster.backend.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
     * father node, if null that is root
     */
    private ZKNode father;

    /**
     * List of children "node".
     */
    private List<ZKNode> children = new ArrayList<>(100);

    public ZKNode() {
    }

    public ZKNode(String path, String value) {
        this.path = path;
        this.value = value;
    }

    public ZKNode(String path, String value, ZKNode father, List<ZKNode> children) {
        this.path = path;
        this.value = value;
        this.father = father;
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

    public ZKNode getFather() {
        return father;
    }

    public void setFather(ZKNode father) {
        this.father = father;
    }

    public List<ZKNode> getChildren() {
        return children;
    }

    public void setChildren(List<ZKNode> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ZKNode zkNode = (ZKNode) o;
        return Objects.equals(path, zkNode.path)
                && Objects.equals(value, zkNode.value)
                && Objects.equals(father, zkNode.father)
                && Objects.equals(children, zkNode.children);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path, value, father, children);
    }

    @Override
    public String toString() {
        return "ZKNode{"
                + "path='" + path + '\''
                + ", value='" + value + '\''
                + ", father=" + father.getPath()
                + '}';
    }
}

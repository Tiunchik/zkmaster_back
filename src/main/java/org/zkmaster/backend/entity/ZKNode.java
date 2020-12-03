package org.zkmaster.backend.entity;

import java.util.List;

public class ZKNode {

    /**
     * Full path of "this" "node" on ZK server.
     */
    private String path;

    /**
     * data value from ZK.
     * * converted from bytes.
     */
    private String value;

    /**
     * List of children "node".
     */
    private List<ZKNode> children;

    public ZKNode(
            String path,
            String value,
            List<ZKNode> children) {
        this.path = path;
        this.value = value;
        this.children = children;
    }

    @Override
    public String toString() {
        String rslValue = (value == null) ? "" : value;
        StringBuilder childrenToString = new StringBuilder();

        if (children != null) {
            for (var each : children) {
                childrenToString.append(each.toString());
            }
        }

        return "" + path + " : " + rslValue + "\r\n"
                + childrenToString.toString();
    }

}

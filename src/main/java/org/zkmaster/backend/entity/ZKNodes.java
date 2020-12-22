package org.zkmaster.backend.entity;

/**
 * Utility class for {@link ZKNode}.
 */
public class ZKNodes {

    // TODO : TEST
    public static ZKNode getSubZKNodeByFullPath(ZKNode node, String path) {


        return null;
    }

    public static ZKNode replaceParentName(ZKNode node) {

        return node;
    }

    public static boolean hasChildren(ZKNode node) {
        return node.getChildren().size() != 0;
    }
}

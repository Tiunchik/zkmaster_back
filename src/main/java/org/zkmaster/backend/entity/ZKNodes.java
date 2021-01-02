package org.zkmaster.backend.entity;

import org.zkmaster.backend.devutil.DevLog;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Utility class for {@link ZKNode}.
 */
public class ZKNodes {

    /**
     * Check {@param node} has children or not? Just pretty API
     */
    public static boolean hasChildren(ZKNode node) {
        return !node.getChildren().isEmpty();
    }

    /**
     * Extract Node name from Node path.
     *
     * @return Example:
     * "/1/2/3/4" : value" ->> "4"
     * "/" : value" ->> "/"
     */
    public static String nameFromPath(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Substring from path with deleted name.
     *
     * @return Example: "/1/2/3/4 : value" ->> "/1/2/3/"
     */
    public static String pathWithoutName(String path) {
        return path.substring(0, path.lastIndexOf('/') + 1);
    }

    /**
     * Extract parent path from Node path.
     *
     * @return Example:
     * "/1/2/3/4" : value" ->> "/1/2/3"
     * "/" : value" ->> "/"
     */
    public static String parentNodePath(String nodePath) {
        return (nodePath.lastIndexOf('/') == 0)
                ? "/" : nodePath.substring(0, nodePath.lastIndexOf('/'));
    }

    /**
     * Iterate by Node as tree and collect all Node path is {@link Set}.
     *
     * @param node root for iterate.
     * @return {@link Set} with paths of {@param node} and all inner sub-Nodes and etc.
     */
    public static Set<String> collectAllPaths(ZKNode node) {
        Set<String> rsl = new HashSet<>();
        treeIterateWidthList(node, (each) -> rsl.add(each.getPath()));
        return rsl;
    }

    /**
     * Select sub-node from {@param root} by {@param path}.
     * !!! Return null, if doesn't find.
     *
     * @param root - start node (could be root or any other).
     * @param path - absolute path of searching {@link ZKNode}.
     * @return Node with path with == {@param path} with all sub-nodes OR
     * null ==>> if searching {@link ZKNode} doesn't found.
     * @implNote Tree traversal should be: for width by List, Not recursion.
     * * Cause we don't know how deep tree is.(Argument for List)
     */
    public static ZKNode getSubNode(ZKNode root, String path) {
        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.add(root);
        ZKNode rsl = null;
        while (!treeWalkList.isEmpty()) {
            var current = treeWalkList.removeFirst();
            if (current.getPath().equals(path)) {
                rsl = current;
//                DevLog.print("getSubNode", "children", rsl.getChildren());
//                rsl.setChildren(new LinkedList<>());
                break;
            } else if (ZKNodes.hasChildren(current)) {
                treeWalkList.addAll(current.getChildren());
            }
        }
        DevLog.print("getSubNode", "rsl", rsl);
        return rsl;
    }

    /**
     * For each by full tree for {@param node}.
     * * IMPORTANT: you should use Collections & Atomic wraps in your lambda func!
     *
     * @param node        node for iterate. (could be root or any other).
     * @param forEachFunc - forEach function.
     */
    public static void treeIterateWidthList(ZKNode node, Consumer<ZKNode> forEachFunc) {
        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.add(node);
        while (!treeWalkList.isEmpty()) {
            ZKNode currentZKNode = treeWalkList.removeFirst();
            forEachFunc.accept(currentZKNode);
            if (hasChildren(currentZKNode)) {
                treeWalkList.addAll(currentZKNode.getChildren());
            }
        }
    }

    /**
     * Default using of print {@link ZKNode}.
     *
     * @see #printNode(ZKNode, boolean, boolean, boolean)
     */
    public static void printNode(ZKNode node) {
        printNode(node, false, true, true);
    }

    /**
     * Print {@link ZKNode} with all children as pretty table.
     *
     * @param node  print source.
     * @param name  turn on/off print: name.
     * @param path  turn on/off print: path.
     * @param value turn on/off print: value.
     */
    public static void printNode(ZKNode node, boolean name, boolean path, boolean value) {
        var sb = new StringBuilder("Print ZKNode format: " + System.lineSeparator());
        final String dataSeparator = " :: ";
        sb.append((name) ? "name " : "");
        sb.append((name && path) ? dataSeparator : "");
        sb.append((path) ? "path" : "");
        sb.append((path && value) ? dataSeparator : "");
        sb.append((value) ? "value" : "");
        sb.append(System.lineSeparator());

        treeIterateWidthList(node, each -> {
            sb.append((name) ? each.getName() : "");
            sb.append((name && path) ? dataSeparator : "");
            sb.append((path) ? each.getPath() : "");
            sb.append((path && value) ? dataSeparator : "");
            sb.append((value) ? each.getValue() : "");
            sb.append(System.lineSeparator());
        });
        System.out.println(sb.toString());
    }

}
package org.zkmaster.backend.entity.utils;

import org.zkmaster.backend.entity.ZKNode;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

/**
 * Utility class for {@link ZKNode}.
 */
public class ZKNodes {


    /* Methods for work with Node path */


    /**
     * Extract Node name from Node path.
     *
     * @return Example:
     * "/1/2/3/4" : value" ->> "4"
     * "/" : value" ->> "/"
     */
    public static String nameFromPath(String path) {
        return ("/".equals(path))
                ? "/" : path.substring(path.lastIndexOf('/') + 1);
    }

    /**
     * Substring from path with deleted name.
     *
     * @return Example:
     * "/1/2/3/4 : value" ->> "/1/2/3/"
     * "/" : value" ->> "/"
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
        return ("/".equals(nodePath) || nodePath.lastIndexOf('/') == 0)
                ? "/" : nodePath.substring(0, nodePath.lastIndexOf('/'));
    }


    /* Methods for work as tree */


    /**
     * Iterate by Node as tree and collect all Node paths is {@link Set}.
     *
     * @param node root for iterate.
     * @return {@link Set} with paths of {@param node} and all sub-Nodes and etc.
     */
    public static Set<String> collectAllPaths(ZKNode node) {
        Set<String> rsl = new HashSet<>();
        treeIterateWidthList(node, (each) -> rsl.add(each.getPath()));
        return rsl;
    }

    /**
     * Search sub-node in {@param root} tree with equals Node path that {@param path}.
     * IMPORTANT: !!! Return null, if doesn't find.
     *
     * @param root - start node (could be root or any Node).
     * @param path - absolute path of searching {@link ZKNode}.
     * @return Node with path with == {@param path} with all sub-nodes OR
     * null ==>> if searching {@link ZKNode} doesn't found.
     * @implSpec Tree traversal is width with list.
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
                break;
            } else if (current.hasChildren()) {
                treeWalkList.addAll(current.getChildren());
            }
        }
        return rsl;
    }

    /**
     * For each for full {@param node} tree.
     * * IMPORTANT: you should use Collections & Atomic wraps in your lambda func!
     *
     * @param node        Node for iterate. (could be root or any Node).
     * @param forEachFunc forEach function.
     */
    public static void treeIterateWidthList(ZKNode node, Consumer<ZKNode> forEachFunc) {
        var treeWalkList = new LinkedList<ZKNode>();
        treeWalkList.add(node);
        while (!treeWalkList.isEmpty()) {
            ZKNode currentZKNode = treeWalkList.removeFirst();
            forEachFunc.accept(currentZKNode);
            if (currentZKNode.hasChildren()) {
                treeWalkList.addAll(currentZKNode.getChildren());
            }
        }
    }

    /**
     * Count how much Node is in {@param node} tree.
     * * Root count as well.
     *
     * @param node root for counting.
     * @return Node tree size.
     */
    public static int treeNodeCount(ZKNode node) {
        AtomicInteger rsl = new AtomicInteger();
        treeIterateWidthList(node, each -> rsl.getAndIncrement());
        return rsl.get();
    }

    /**
     * Default using of print {@link ZKNode}.
     *
     * @see #printNode(ZKNode, boolean, boolean, boolean, Consumer)
     */
    public static void printNode(ZKNode node) {
        printNode(node, false, true, true, System.out::println);
    }

    public static void printNode(ZKNode node, Consumer<String> output) {
        printNode(node, false, true, true, output);
    }

    /**
     * Print {@link ZKNode} with all children as pretty table.
     *
     * @param node  print source.
     * @param name  turn on/off print: name.
     * @param path  turn on/off print: path.
     * @param value turn on/off print: value.
     */
    public static void printNode(ZKNode node, boolean name, boolean path, boolean value,
                                 Consumer<String> output) {
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
        output.accept(sb.toString());
//        System.out.println(sb.toString());
    }


}
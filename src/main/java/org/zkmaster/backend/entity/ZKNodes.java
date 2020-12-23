package org.zkmaster.backend.entity;

import java.util.LinkedList;

/**
 * Utility class for {@link ZKNode}.
 */
public class ZKNodes {

    /**
     * Selector of inner {@link ZKNode}. Tree-width walk by List.
     *
     * @param root - start node.
     * @param path - absolute path of searching {@link ZKNode}.
     * @return result || null, if searching {@link ZKNode} doesn't found.
     */
    public static ZKNode getSubZKNodeByFullPath(ZKNode root, String path) {
        var nodeList = new LinkedList<ZKNode>();
        nodeList.add(root);
        ZKNode rsl = null;

        while (!nodeList.isEmpty()) {
            var current = nodeList.removeFirst();
            if (current.getPath().equals(path)) {
                rsl = current;
                break;
            } else if (hasChildren(current)) {
                nodeList.addAll(current.getChildren());
            }
        }
        return rsl;
    }

    /**
     * Info for any change in code:
     * /1/1/1 :: oldPath
     * /1/    :: corePathWithoutName
     * set    :: newPathValue
     * 1      :: oldName
     * <p>
     * /1/set/1 :: return
     */
    public static String replacePath(String oldPath, String corePathWithoutName,
                                     String newPathValue, String oldName) {
//        System.out.println("DEV :: method: Replace START");
//        System.out.println("DEV :: corePath==" + corePath);
//        System.out.println("DEV :: oldPath==" + oldPath);
//        System.out.println("DEV :: corePathWithoutName==" + corePathWithoutName);
//        System.out.println("DEV :: newPathValue==" + newPathValue);
//        System.out.println("DEV :: oldName==" + oldName);
//        System.out.println("DEV :: method: Replace RUN");

        var corePathWithoutNameLength = corePathWithoutName.length();
        var secondPart = oldPath.substring(corePathWithoutNameLength - 1 + oldName.length() + 1);

        return corePathWithoutName + newPathValue + secondPart;
    }

    /**
     * Print {@link ZKNode} with all children as pretty table.
     *
     * @param node  print source.
     * @param name  turn on print: name.
     * @param path  turn on print: path.
     * @param value turn on print: value.
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

        var nodeList = new LinkedList<ZKNode>();
        nodeList.add(node);
        while (!nodeList.isEmpty()) {
            var current = nodeList.removeFirst();

            sb.append((name) ? current.getName() : "");
            sb.append((name && path) ? dataSeparator : "");
            sb.append((path) ? current.getPath() : "");
            sb.append((path && value) ? dataSeparator : "");
            sb.append((value) ? current.getValue() : "");
            sb.append(System.lineSeparator());

            if (hasChildren(current)) {
                nodeList.addAll(current.getChildren());
            }
        }
        System.out.println(sb.toString());
    }

    public static boolean hasChildren(ZKNode node) {
        return node.getChildren().size() != 0;
    }


    /* getSubZKNodeByFullPath - testing*/

//    public static void main(String[] args) {
//        var root = new ZKNode("/", "v", "/", List.of(
//                new ZKNode("/a1", "v", "a1", List.of(
//                        new ZKNode("/a1/a2", "v", "a2", List.of(
//                                new ZKNode("/a1/a2/a3-1", "v", "a3-1", List.of()),
//                                new ZKNode("/a1/a2/a3-2", "v", "a3-2", List.of())
//                        ))
//                )),
//                new ZKNode("/2", "v", "2", List.of())
//        ));
//
//        var rsl = getSubZKNodeByFullPath(root, "/a1/a2");
//        printNode(rsl, false, true, true);
//    }

}

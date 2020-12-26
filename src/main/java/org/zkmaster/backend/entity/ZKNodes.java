package org.zkmaster.backend.entity;

import java.util.LinkedList;

/**
 * Utility class for {@link ZKNode}.
 */
public class ZKNodes {

    /**
     * Info for any change in code:
     * /1/1/1 :: oldPath
     * /1/    :: corePathWithoutName
     * set    :: newName
     * 1      :: oldName
     * <p>
     * /1/set/1 :: return
     */
    public static String replacePath(String oldPath, String corePathWithoutName,
                                     String newName, String oldName) {
//        System.out.println("DEV :: method: Replace START");
//        System.out.println("DEV :: corePath==" + corePath);
//        System.out.println("DEV :: oldPath==" + oldPath);
//        System.out.println("DEV :: corePathWithoutName==" + corePathWithoutName);
//        System.out.println("DEV :: newName==" + newName);
//        System.out.println("DEV :: oldName==" + oldName);
//        System.out.println("DEV :: method: Replace RUN");

        var corePathWithoutNameLength = corePathWithoutName.length();
        var endPart = oldPath.substring(corePathWithoutNameLength + oldName.length());
        return corePathWithoutName + newName + endPart;
    }

    public static boolean hasChildren(ZKNode node) {
        return node.getChildren().size() != 0;
    }

    public static String extractNodeName(String path) {
        return path.substring(path.lastIndexOf('/') + 1);
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

}

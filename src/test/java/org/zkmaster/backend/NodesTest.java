package org.zkmaster.backend;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

public class NodesTest {
    class TreeNode<T> {

        T data;
        TreeNode<T> parent;
        List<TreeNode<T>> children;

        public TreeNode(T data) {
            this.data = data;
            this.children = new LinkedList<TreeNode<T>>();
        }

        public TreeNode<T> addChild(T child) {
            TreeNode<T> childNode = new TreeNode<T>(child);
            childNode.parent = this;
            this.children.add(childNode);
            return childNode;
        }

        // other features ...

    }

    @Test
    public void TestRun() {

    }

    @Test
    public void simpleTreeSearch() {
//        TreeNode<String> root = new TreeNode<String>("root");
//        {
//            TreeNode<String> node0 = root.addChild("node0");
//            TreeNode<String> node1 = root.addChild("node1");
//            TreeNode<String> node2 = root.addChild("node2");
//            {
//                TreeNode<String> node20 = node2.addChild(null);
//                TreeNode<String> node21 = node2.addChild("node21");
//                {
//                    TreeNode<String> node210 = node20.addChild("node210");
//                }
//            }
//        }
    }
}

package com.lilong.algorithm.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.concurrent.LinkedBlockingQueue;

public class Tree extends BaseStructure {

    public static void main(String[] args) {

        //------------------原始二叉树------------------------
        TreeNode root = buildTree();
        // 打印二叉树
        displayAsTree(levelOrderTraversal(root), 2);

        //-------------------二叉树的遍历---------------------
        // 前序遍历，递归
        root = buildTree();
        System.out.println("前序遍历，递归:");
        preOrderTraversal(root);

        // 前序遍历，非递归
        // TODO
        root = buildTree();
        System.out.println("前序遍历，非递归:");
//        preOrderTraversalNonRecursive(root);

        // 中序遍历，非递归
        // TODO
        root = buildTree();
        System.out.println("中序遍历，递归:");
        inOrderTraversal(root);

        // 中序遍历，非递归
        root = buildTree();
        System.out.println("中序遍历，非递归:");
//        inOrderTraversalNonRecursive(root);

        // 后序遍历，递归
        root = buildTree();
        System.out.println("后序遍历，递归:");
        postOrderTraversal(root);

        // 层序遍历，非递归
        root = buildTree();
        System.out.println("层序遍历，非递归:");
        levelOrderTraversalSimple(root);

        //--------------------------------------------------
        root = buildTree();
        // 用最简单的方法非递归反转二叉树
        System.out.println("------------------------");
        reverseBinaryTreeNonRecursive(root);


        root = buildTree();
        // 获取二叉树的最小深度
        System.out.println("minDepth is " + findMinDepth(root));

        root = buildTree();
        // 二叉树有多少条路径的和为指定值
        int targetSum = 7;
        System.out.println("there are " + countPathsWithSum(root, targetSum) + " paths with sum of " + targetSum);

        root = buildTree();
        // 是否为对称树
        System.out.println("is symmetric : " + isSymmetricTree(root));

        root = buildTree();
        // 从下向上层序遍历树
        levelOrderTraversalBottomUp(root);
    }

    /**
     * 找到树的最小深度
     */
    public static int findMinDepth(TreeNode root) {
        HashMap<String, Integer> minDepth = new HashMap<>();
        minDepth.put("minDepth", Integer.MAX_VALUE);
        traversalTreeRecursiveForMinDepth(root, 1, minDepth);
        return minDepth.get("minDepth");
    }

    /**
     * 中序遍历树，找到最小深度
     */
    public static void traversalTreeRecursiveForMinDepth(TreeNode root, int curDepth, HashMap minDepth) {

        if (root.left == null && root.right == null) {
            if (curDepth < (int) minDepth.get("minDepth")) {
                minDepth.put("minDepth", curDepth);
            }
            return;
        }

        if (root.left != null) {
            traversalTreeRecursiveForMinDepth(root.left, curDepth + 1, minDepth);
        }
        if (root.right != null) {
            traversalTreeRecursiveForMinDepth(root.right, curDepth + 1, minDepth);
        }
    }

    /**
     * 从树根到树叶，由多少条路线使得路线上的值的总和为指定值
     */
    public static int countPathsWithSum(TreeNode root, int sum) {

        if (root.left == null && root.right == null) {
            return sum == root.value ? 1 : 0;
        }

        return (root.left == null ? 0 : countPathsWithSum(root.left, sum
                - root.value))
                + (root.right == null ? 0 : countPathsWithSum(root.right, sum
                - root.value));
    }

    /**
     * 是否为对称树
     */
    public static boolean isSymmetricTree(TreeNode root) {

        if (root == null) {
            return true;
        }

        try {
            isSymmetricTreeRecursive(root.left, root.right);
        } catch (TreeNodeNotSymmetricException e) {
            return false;
        }
        return true;
    }

    public static void isSymmetricTreeRecursive(TreeNode left, TreeNode right)
            throws TreeNodeNotSymmetricException {

        if (left == null && right == null) {
            return;
        }

        if (left == null && right != null) {
            throw new TreeNodeNotSymmetricException();
        }

        if (left != null && right == null) {
            throw new TreeNodeNotSymmetricException();
        }

        if (left.value != right.value) {
            throw new TreeNodeNotSymmetricException();
        }

        isSymmetricTreeRecursive(left.left, right.right);
        isSymmetricTreeRecursive(left.right, right.left);
    }

    /**
     * 非递归反转二叉树，最简单的代码，基于层序遍历
     */
    public static void reverseBinaryTreeNonRecursive(TreeNode root) {

        if (root == null) {
            return;
        }

        LinkedBlockingQueue<TreeNode> queue = new LinkedBlockingQueue<>();
        queue.add(root);

        while (!queue.isEmpty()) {

            TreeNode node = queue.poll();
            System.out.println(node.value);

            TreeNode temp = node.left;
            node.left = node.right;
            node.right = temp;

            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }


    /**
     * 无返回值的层序遍历，用最简单的代码
     */
    public static void levelOrderTraversalSimple(TreeNode root) {

        if (root == null) {
            return;
        }

        LinkedBlockingQueue<TreeNode> queue = new LinkedBlockingQueue<TreeNode>();
        queue.add(root);
        while (!queue.isEmpty()) {
            TreeNode node = queue.poll();
            System.out.println(node.value);
            if (node.left != null) {
                queue.add(node.left);
            }
            if (node.right != null) {
                queue.add(node.right);
            }
        }
    }

    /**
     * 层序遍历二叉树，遍历得到的节点值存入数组，空的节点以null表示
     */
    public static Integer[] levelOrderTraversal(TreeNode root) {

        ArrayList<TreeNode> untraversedList = new ArrayList<TreeNode>();
        ArrayList<Integer> traversedList = new ArrayList<Integer>();

        untraversedList.add(root);

        ArrayList<TreeNode> toBeAddedList = new ArrayList<TreeNode>();
        ArrayList<TreeNode> toBeRemovedList = new ArrayList<TreeNode>();

        while (!untraversedList.isEmpty()) {

            // 检测当前层的节点是否全是空节点，是的话不需要将空节点的叶子也视作空节点来遍历
            // 否则要将叶子视为空节点来遍历
            boolean isThisLevelAllEmpty = true;
            for (TreeNode node : untraversedList) {
                if (!node.isEmpty) {
                    isThisLevelAllEmpty = false;
                    break;
                }
            }

            for (TreeNode node : untraversedList) {

                if (node.isEmpty) {
                    traversedList.add(null);
                } else {
                    traversedList.add(node.value);
                }

                if (node.left == null && node.right == null) {
                    if (!isThisLevelAllEmpty) {
                        toBeAddedList.add(new TreeNode());
                        toBeAddedList.add(new TreeNode());
                    }
                } else if (node.left != null && node.right == null) {
                    toBeAddedList.add(node.left);
                    toBeAddedList.add(new TreeNode());
                } else if (node.left == null && node.right != null) {
                    toBeAddedList.add(new TreeNode());
                    toBeAddedList.add(node.right);
                } else if (node.left != null && node.right != null) {
                    toBeAddedList.add(node.left);
                    toBeAddedList.add(node.right);
                }

                toBeRemovedList.add(node);
            }

            untraversedList.removeAll(toBeRemovedList);
            untraversedList.addAll(toBeAddedList);

            toBeRemovedList.clear();
            toBeAddedList.clear();

        }

        return traversedList.toArray(new Integer[0]);
    }

    public static void levelOrderTraversalBottomUp(TreeNode root) {

        if (root == null) {
            System.out.println("");
            return;
        }

        ArrayList<TreeNode> list = new ArrayList<TreeNode>();
        list.add(root);
        Stack<ArrayList<TreeNode>> s = new Stack<ArrayList<TreeNode>>();

        ArrayList<TreeNode> toBeAddedList = new ArrayList<TreeNode>();

        while (!list.isEmpty()) {

            ArrayList<TreeNode> toBeRemovedList = new ArrayList<TreeNode>();

            for (TreeNode node : list) {

                toBeRemovedList.add(node);

                if (node.left != null) {
                    toBeAddedList.add(node.left);
                }

                if (node.right != null) {
                    toBeAddedList.add(node.right);
                }

            }

            list.addAll(toBeAddedList);
            toBeAddedList.clear();

            list.removeAll(toBeRemovedList);

            s.add(toBeRemovedList);
        }

        while (!s.isEmpty()) {

            ArrayList<TreeNode> level = s.pop();

            for (TreeNode node : level) {
                System.out.print(node.value + " ");
            }

            System.out.print("\n");
        }

    }

    /**
     * 前序遍历二叉树，递归写法
     */
    public static void preOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        System.out.println(root.value);
        preOrderTraversal(root.left);
        preOrderTraversal(root.right);
    }

    /**
     * 前序遍历二叉树，非递归写法
     */
    public static void preOrderTraversalNonRecursive(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        TreeNode node = root;
        while (!stack.isEmpty()) {
            if(node.left != null){
                System.out.println(node.value);
                node = node.left;
            }
        }
    }

    /**
     * 中序遍历二叉树，非递归写法
     */
    public static void inOrderTraversalNonRecursive(TreeNode root) {
        if (root == null) {
            return;
        }
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        TreeNode node = root;
        while (!stack.isEmpty()) {
            if (node.left != null) {
                if (node.right != null) {
                    stack.push(node.right);
                }
                stack.push(node);
                node = node.left;
            } else {
                System.out.println(node.value);
                node = stack.pop();
            }
        }
    }

    /**
     * 中序遍历二叉树，递归写法
     */
    public static void inOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        inOrderTraversal(root.left);
        System.out.println(root.value);
        inOrderTraversal(root.right);
    }

    /**
     * 后序遍历二叉树，递归写法
     */
    public static void postOrderTraversal(TreeNode root) {
        if (root == null) {
            return;
        }
        postOrderTraversal(root.left);
        postOrderTraversal(root.right);
        System.out.println(root.value);
    }


    public static class TreeNodeNotSymmetricException extends Exception {

        public TreeNodeNotSymmetricException() {

        }
    }

}




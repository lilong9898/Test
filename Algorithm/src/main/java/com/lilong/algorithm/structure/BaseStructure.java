package com.lilong.algorithm.structure;

import com.lilong.algorithm.sort.BaseSort;

import java.util.List;
import java.util.Map;

/**
 * 创建和打印各种数据结构
 */

public class BaseStructure extends BaseSort {

    /**
     * 链表节点
     */
    public static class ListNode {

        public ListNode next;
        public int value;

        public ListNode(int value) {
            this.value = value;
        }
    }

    /**
     * 创建链表
     */
    public static ListNode buildLinkedList() {

        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(2);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;

        return n1;
    }

    /**
     * 打印列表
     * */
    public static <T> void printList(List<T> list){
        for(int i = 0; i < list.size(); i++){
            System.out.print(list.get(i));
            if(i != list.size() - 1){
                System.out.print(" ");
            }
        }
        System.out.println();
    }

    /**
     * 打印链表
     */
    public static void printLinkedList(ListNode head) {

        if (head == null) {
            System.out.println("");
            return;
        }

        System.out.print(head.value);

        while (head.next != null) {
            System.out.print(" -> " + head.next.value);
            head = head.next;
        }

        System.out.println();
    }

    /**
     * 打印map的key
     * */
    public static <K,V> void printMapKeys(Map<K,V> map){
        System.out.print(map.getClass().getSimpleName() + "(" + map.size() + ") : ");
        for(K key : map.keySet()){
            System.out.print(key + " ");
        }
        System.out.println();
    }

    /**
     * 获取链表长度
     */
    public static int getLinkedListLength(ListNode head) {

        if (head == null) {
            return 0;
        }

        int length = 1;

        while (head.next != null) {
            length++;
            head = head.next;
        }

        return length;

    }

    /**
     * 二叉树的节点
     * 可以用来表示空节点, 即isEmpty为true
     */
    public static class TreeNode {

        public boolean isEmpty = false;

        public TreeNode() {
            isEmpty = true;
        }

        public TreeNode(int value) {
            this.value = value;
        }

        public TreeNode left;
        public TreeNode right;
        public int value;

    }

    /**
     * 创建二叉树
     */
    public static TreeNode buildTree() {

        TreeNode root = new TreeNode(1);
        TreeNode left11 = new TreeNode(2);
        TreeNode right12 = new TreeNode(3);
        root.left = left11;
        root.right = right12;
        TreeNode left21 = new TreeNode(4);
        TreeNode left22 = new TreeNode(5);
        left11.left = left21;
        left11.right = left22;
        TreeNode right23 = new TreeNode(6);
        TreeNode right24 = new TreeNode(7);
        right12.left = right23;
        right12.right = right24;

        return root;
    }

}


package com.example.structure;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * 链表
 */
public class LinkedList extends BaseStructure {

    public static void main(String[] args) {
        ListNode head = buildLinkedList();
        System.out.println("input:");
        printLinkedList(head);
        removeConsecutiveDuplicates(head);
        System.out.println("output:");
        printLinkedList(head);
    }

    /**
     * 去除链表中相邻的重复元素
     */
    public static void removeConsecutiveDuplicates(ListNode head) {

        if (head == null) {
            return;
        }

        ListNode n1;
        ListNode n2;

        n1 = head;
        n2 = head;

        while (n1 != null && n1.next != null) {
            while (n2.next != null && n2.next.value == n1.value) {
                n2 = n2.next;
            }
            n1.next = n2.next;
            n1 = n2.next;
            n2 = n1;
        }

    }

    public static void removeNthNodeFromLast() {

        ListNode head = buildLinkedList();

        int n = 1;

        removeNthNodeFromLast(head, n);
        removeNthNodeFromLastOnePass(head, n);
    }

    /**
     * 去掉倒数第n个链表节点，仅遍历一次，需要用stack来存储
     */
    public static void removeNthNodeFromLastOnePass(ListNode head, int n) {

        if (head == null || n <= 0) {
            printLinkedList(head);
            return;
        }

        Stack<ListNode> s = new Stack<ListNode>();
        ListNode cur = head;

        s.push(cur);
        while (cur.next != null) {
            s.push(cur.next);
            cur = cur.next;
        }

        ListNode nodeLastNth = null;
        ListNode nodeBeforeLastNth = null;
        ListNode nodeAfterLastNth = null;

        for (int i = 0; i < n - 1; i++) {
            try {
                nodeAfterLastNth = s.pop();
            } catch (EmptyStackException e) {
                nodeAfterLastNth = null;
                break;
            }
        }

        try {
            nodeLastNth = s.pop();
        } catch (EmptyStackException e) {
            nodeLastNth = null;
        }

        try {
            nodeBeforeLastNth = s.pop();
        } catch (EmptyStackException e) {
            nodeBeforeLastNth = null;
        }

        if (nodeBeforeLastNth == null && nodeAfterLastNth == null) {
            if (nodeLastNth == null) {
                printLinkedList(head);
            } else {
                printLinkedList(null);
            }
            return;
        } else if (nodeBeforeLastNth != null && nodeAfterLastNth == null) {
            nodeBeforeLastNth.next = null;
        } else if (nodeBeforeLastNth == null && nodeAfterLastNth != null) {
            head = nodeAfterLastNth;
        } else {
            nodeBeforeLastNth.next = nodeAfterLastNth;
        }

        printLinkedList(head);
    }

    /**
     * 去掉倒数第n个链表节点，遍历两次，第一次是获取链表长度
     */
    public static void removeNthNodeFromLast(ListNode head, int n) {

        int length = getLinkedListLength(head);

        if (n <= 0 || n > length) {
            printLinkedList(head);
            return;
        }

        if (n == length) {
            head = head.next;
            printLinkedList(head);
            return;
        }

        ListNode cur = head;
        ListNode nodeBeforeLastNth;
        ListNode nodeAfterLastNth;

        for (int i = 0; i < length - n - 1; i++) {
            cur = cur.next;
        }

        nodeBeforeLastNth = cur;
        nodeAfterLastNth = cur.next.next;

        nodeBeforeLastNth.next = nodeAfterLastNth;
        printLinkedList(head);

    }


}


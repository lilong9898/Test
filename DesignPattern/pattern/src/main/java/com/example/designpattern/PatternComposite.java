package com.example.designpattern;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * 组合模式
 * 一组实现同一接口的对象,任何对象都可以吸收其它对象作为子节点
 * 这样任何一个或一部分子节点的整体都实现了同一个接口,对外保持了特性一致
 */

public class PatternComposite {

    public static void main(String[] args) {

        AbstractNode node1 = new Node1();
        AbstractNode node2 = new Node2();
        AbstractNode node3 = new Node3();
        AbstractNode node4 = new Node4();
        AbstractNode node5 = new Node5();

        node1.addNode(node2);
        node1.addNode(node3);
        node2.addNode(node4);
        node2.addNode(node5);

        node1.onTraversed();
    }
}

/**
 * 抽象的node
 */
abstract class AbstractNode {

    private String name;
    private List<AbstractNode> nodes;

    private static Deque<AbstractNode> dequeForBFS = new ArrayDeque<>();

    public AbstractNode(String name) {
        this.name = name;
        this.nodes = new ArrayList<AbstractNode>();
    }

    public void addNode(AbstractNode node) {
        if (!nodes.contains(node)) {
            nodes.add(node);
        }
    }

    public void removeNode(AbstractNode node) {
        nodes.remove(node);
    }

    public AbstractNode getNode(int index) {
        return nodes.get(index);
    }

    public void onTraversed() {

        //DFS
//        System.out.println("node " + name);
//        for (AbstractNode node : nodes) {
//            node.onTraversed();
//        }

        // BFS
//        System.out.println(name);
//        for (AbstractNode node : nodes) {
//            dequeForBFS.addLast(node);
//        }
//        if (dequeForBFS.peek() != null) {
//            dequeForBFS.poll().onTraversed();
//        }

    }

}

class Node1 extends AbstractNode {
    public Node1() {
        super("node1");
    }
}

class Node2 extends AbstractNode {
    public Node2() {
        super("node2");
    }
}

class Node3 extends AbstractNode {
    public Node3() {
        super("node3");
    }
}

class Node4 extends AbstractNode {
    public Node4() {
        super("node4");
    }
}

class Node5 extends AbstractNode {
    public Node5() {
        super("node5");
    }
}

package com.hxy;

import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        BPTree bpTree = new BPTree(4);
        bpTree.add(5, "a");
        bpTree.add(6, "e");
        bpTree.add(7, "e");
        bpTree.add(8, "b");
        bpTree.add(9, "e");
        bpTree.add(10, "c");
        bpTree.add(15, "d");
        bpTree.add(16, "e");
        bpTree.add(17, "e");
        bpTree.add(18, "e");
        bpTree.add(19, "e");
        bpTree.add(20, "e");
        bpTree.add(21, "e");
        bpTree.add(22, "e");
//        bpTree.add(6, "f");
//        bpTree.add(7, "g");
//        bpTree.add(8, "h");
//        bpTree.add(9, "i");
//        bpTree.add(10, "j");

        bpTree.getTheTree();
        bpTree.getAllValue();
        System.out.println("---------------");

    }
}

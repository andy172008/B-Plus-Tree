//author:SX1916085 贺星宇
package com.hxy;

import java.io.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("初始化开始，请输入树的阶（M>2）");
        Scanner cin = new Scanner(System.in);
        int m = cin.nextInt();
        BPTree bpTree = new BPTree(m);
        System.out.println("初始化完成，请输入菜单数字选择功能");
        int code;
        int key;
        String value;
        String fileName;
        ObjectOutputStream objectOutputStream;
        ObjectInputStream objectInputStream;
        while (true) {
            System.out.println("");
            System.out.println("0：退出程序");
            System.out.println("1：插入数据");
            System.out.println("2：删除数据");
            System.out.println("3：查询数据");
            System.out.println("4：输出所有数据");
            System.out.println("5：输出整棵树");
            System.out.println("6：储存当前树");
            System.out.println("7：加载已储存的树");
            code = cin.nextInt();
            switch (code) {
                case 0:
                    return;
                case 1:
                    System.out.println("请输入关键字（int型）");
                    key = cin.nextInt();
                    System.out.println("请输入数据（String型）");
                    value = cin.next();
                    bpTree.add(key, value);
                    break;
                case 2:
                    System.out.println("请输入关键字（int型）");
                    key = cin.nextInt();
                    bpTree.delete(key);
                    break;
                case 3:
                    System.out.println("请输入关键字（int型）");
                    key = cin.nextInt();
                    if (bpTree.find(key) == null) {
                        System.out.println("树中没有这个关键字");
                    } else {
                        System.out.println("key: " + key + " value:" + bpTree.find(key));
                    }
                    break;
                case 4:
                    bpTree.getAllValue();
                    break;
                case 5:
                    bpTree.getTheTree();
                    break;
                case 6:
                    System.out.println("请输入要储存的文件名（包括后缀名）");
                    fileName = cin.next();
                    try {
                        objectOutputStream = new ObjectOutputStream(new FileOutputStream(fileName));
                        objectOutputStream.writeObject(bpTree);
                        objectOutputStream.close();
                        System.out.println("储存完成");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case 7:
                    System.out.println("请输入要读取的文件名（包括后缀名）");
                    fileName = cin.next();
                    try {
                        objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
                        bpTree = (BPTree) objectInputStream.readObject();
                        objectInputStream.close();
                        System.out.println("储存完成");
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    System.out.println("命令错误，请从新输入！");
                    break;
            }
        }

//        bpTree = new BPTree(5);
//        bpTree.add(1, "a");
//        bpTree.add(2, "b");
//        bpTree.add(3, "c");
//        bpTree.add(4, "d");
//        bpTree.add(5, "e");
//        bpTree.add(6, "f");
//        bpTree.add(7, "g");
//        bpTree.add(8, "h");
//        bpTree.add(9, "i");
//        bpTree.add(10, "j");
//        bpTree.add(11, "k");
//        bpTree.add(12, "l");
//        bpTree.add(13, "m");
//        bpTree.add(14, "n");
//        bpTree.add(15, "o");
//        bpTree.add(16, "p");
//        bpTree.add(17, "q");
//        bpTree.add(18, "r");
//        bpTree.add(19, "s");
//        bpTree.add(20, "t");
//        bpTree.add(21, "u");
//        bpTree.add(22, "v");
//        bpTree.add(23, "w");
//        bpTree.add(24, "x");
//        bpTree.add(25, "y");
//        bpTree.add(26, "z");
//
//
//        bpTree.getTheTree();
//        bpTree.getAllValue();
//        System.out.println("---------------");
//        ObjectOutputStream objectOutputStream = null;
//
//        try {
//            objectOutputStream = new ObjectOutputStream(new FileOutputStream("alphabet.db"));
//            objectOutputStream.writeObject(bpTree);
//            objectOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("alphabet.db"));
//            BPTree bpTree1 = (BPTree) objectInputStream.readObject();
//            bpTree1.getTheTree();
//            bpTree1.getAllValue();
//        } catch (IOException | ClassNotFoundException e) {
//            e.printStackTrace();
//        }


    }
}

package com.hxy;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

//author:SX1916085 贺星宇
//B+树的节点
//在这个B+树中，我将Key设为int，Value设为String
public class BPNode implements Serializable {
    //B+树的阶
    int m;
    //是否为叶节点
    boolean isLeaf;
    //是否为根节点
    boolean isRoot;
    //父节点
    BPNode father;
    //当前节点的上一个节点
    BPNode lastNode;
    //当前节点的下一个节点
    BPNode nextNode;
    //当前节点储存的关键字
    List<Integer> keys;
    //当前节点储存的子节点
    List<BPNode> chirdNode;
    //当前节点储存的值
    List<String> value;

    //初始函数
    //type的值：0表示根节点，1表示中间节点，2表示叶节点
    BPNode(int type) {
        if (type != 0 && type != 1 && type != 2) {
            System.out.println("输入的节点类型有误，请检查后输入");
            return;
        }
        if (type == 0) {
            isRoot = true;
            keys = new LinkedList<>();
            value = new LinkedList<>();
            chirdNode = new LinkedList<>();
        } else if (type == 2) {
            isLeaf = true;
            keys = new LinkedList<>();
            value = new LinkedList<>();
        } else {
            keys = new LinkedList<>();
            chirdNode = new LinkedList<>();
        }

    }
}

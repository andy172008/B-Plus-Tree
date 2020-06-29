package com.hxy;

public class BPTree {
    //B+树的阶
    int m;
    //B+树阶的一半（向上取整），也是节点中key个数的下限
    int halfM;
    //B+树的根节点
    BPNode root;
    //B+树的第一个叶节点
    BPNode firstLeaf;

    //B+树的初始函数
    BPTree(int m) {
        root = new BPNode(0);
        firstLeaf = root;
        setM(m);
    }

    //对树的阶进行设置
    void setM(int m) {
        if(m <= 2){
            System.out.println("阶必须大于2");
            return;
        }
        this.m = m;
        if (m % 2 == 0) {
            halfM = m / 2;
        } else {
            halfM = (m + 1) / 2;
        }
    }

    //遍历输出所有key和value
    void getAllValue() {
        BPNode temp = firstLeaf;
        if (temp.keys.size() == 0) {
            System.out.println("树中尚未有任何元素");
            return;
        }
        while (temp != null) {
            for (int i = 0; i < temp.keys.size(); i++) {
                System.out.println("key:" + temp.keys.get(i) + "  value:" + temp.value.get(i));
            }
            temp = temp.nextNode;
        }
    }

    //输出整棵树
    void getTheTree() {
        if (root.keys.size() == 0) {
            System.out.println("这是一棵空树");
            return;
        }
        if (root.chirdNode.size() == 0) {
            System.out.print("根节点：");
            for (int i = 0; i < root.keys.size(); i++) {
                System.out.print("key:" + root.keys.get(i) + " value:" + root.value.get(i) + " |  ");
            }
            System.out.println();
            return;
        }
        System.out.print("根节点：");
        for (int i = 0; i < root.keys.size(); i++) {
            System.out.print("key:" + root.keys.get(i) + " |  ");
        }
        System.out.println();

        BPNode currentNode = root.chirdNode.get(0);
        BPNode runNode;
        while (currentNode != null) {
            runNode = currentNode;
            while (runNode != null) {
                for (int i = 0; i < runNode.keys.size(); i++) {
                    System.out.print(" key:" + runNode.keys.get(i));
                }
                System.out.print(" | ");
                runNode = runNode.nextNode;
            }
            if (!currentNode.isLeaf) {
                currentNode = currentNode.chirdNode.get(0);
                System.out.println();
            } else {
                currentNode = null;
            }
        }
        System.out.println();

    }

    //向树中添加key和对应的value
    void add(int key, String value) {
        if (value == null) {
            System.out.println("value值不能为空");
            return;
        }
        //如果是这颗树只有一个根节点，该数据会往根节点当中插入
        if (firstLeaf == root) {
            insertKeyAndValue(root, key, value);
            if (root.keys.size() > m) {
                //现在应该创建两个子节点
                BPNode leftNode = new BPNode(2);
                BPNode rightNode = new BPNode(2);

                leftNode.father = root;
                rightNode.father = root;

                leftNode.lastNode = null;
                leftNode.nextNode = rightNode;

                rightNode.lastNode = leftNode;
                rightNode.nextNode = null;

                firstLeaf = leftNode;

                root.chirdNode.add(leftNode);
                root.chirdNode.add(rightNode);

                //前halfM个元素储存进第一个子节点
                for (int i = 0; i < halfM; i++) {
                    leftNode.keys.add(root.keys.get(i));
                    leftNode.value.add(root.value.get(i));
                }
                //剩余元素储存进第二个子节点
                for (int i = halfM; i < root.keys.size(); i++) {
                    rightNode.keys.add(root.keys.get(i));
                    rightNode.value.add(root.value.get(i));
                }

                root.keys.clear();
                root.value.clear();
                root.keys.add(leftNode.keys.get(leftNode.keys.size() - 1));
                root.keys.add(rightNode.keys.get(rightNode.keys.size() - 1));
            }
        }
        //走到了这一份，代表存在叶节点，那么我们需要往叶节点中插入数据
        else {
            BPNode currentNode = firstLeaf;
            int doneflag = 0;
            while (true) {
                for (int i = 0; i < currentNode.keys.size(); i++) {
                    //插入到当前叶节点的位置i处
                    if (currentNode.keys.get(i) > key) {
                        insertKeyAndValue(currentNode, key, value);
                        doneflag = 1;
                        if (currentNode.keys.size() > m) {
                            divideLeafNode(currentNode);
                        }
                        break;
                    }
                }
                if (doneflag == 1) {
                    break;
                }
                if (currentNode.nextNode != null) {
                    currentNode = currentNode.nextNode;
                } else {
                    break;
                }
            }
            //代表当前插入的key是最大的一个
            if (doneflag == 0) {
                insertKeyAndValue(currentNode, key, value);
                //修改父节点的信息
                updateInfo(currentNode);
//                insertKeyAndChild(currentNode.father, currentNode.keys.get(currentNode.keys.size() - 1), currentNode);
                if (currentNode.keys.size() > m) {
                    divideLeafNode(currentNode);
                }
            }
        }


    }

    //向节点中插入key,返回插入的位置
    int insertKey(BPNode node, int key) {
        for (int i = 0; i < node.keys.size(); i++) {
            if (node.keys.get(i) == key) {
                System.out.println("key值不能有重复的");
                return -1;
            }
            if (key < node.keys.get(i)) {
                node.keys.add(i, key);
                return i;
            }
        }
        node.keys.add(key);
        return node.keys.size() - 1;
    }

    //向节点中插入key和value
    void insertKeyAndValue(BPNode node, int key, String value) {
        int pos = insertKey(node, key);
        node.value.add(pos, value);
    }

    //向节点中插入key和child
    void insertKeyAndChild(BPNode node, int key, BPNode child) {
        int pos = insertKey(node, key);
        node.chirdNode.add(pos, child);
    }

    //返回该节点在父亲节点关键字List中的位置
    int getPosOfChird(BPNode childNode) {
        BPNode father = childNode.father;
        for (int i = 0; i < father.chirdNode.size(); i++) {
            //不要用关键字找，直接比较对象
            if (father.chirdNode.get(i) == childNode) {
                return i;
            }
        }
        return -1;
    }

    //删除一个节点在其父亲节点中的key和指针
    void deleteChildInfo(BPNode childNode) {
        int pos = getPosOfChird(childNode);
        childNode.father.keys.remove(pos);
        childNode.father.chirdNode.remove(pos);
    }

    //更新一个节点在其父亲节点中的信息，并依次往上更新状态，直到根节点
    //更新状态时，childNode这个结点还是存在的
    void updateInfo(BPNode childNode) {
        while (!childNode.isRoot) {
            deleteChildInfo(childNode);
            if (childNode.keys.size() >= 1) {
                insertKeyAndChild(childNode.father, childNode.keys.get(childNode.keys.size() - 1), childNode);
            }
            childNode = childNode.father;
        }
    }

    //删除一个节点在其父亲节点中的信息，并依次往上更新状态，直到根节点
    //更新状态时，childNode这个结点已经不存在了
    void deleteChildInfoAndUpdate(BPNode childNode) {
        deleteChildInfo(childNode);
        updateInfo(childNode.father);
    }


    //当叶节点中key过多时，调用这个函数对其进行分裂
    void divideLeafNode(BPNode leafNode) {
        if (leafNode.keys.size() <= m) {
            return;
        }
        //创建一个子节点，准备分裂
        BPNode anotherChild = new BPNode(2);
        deleteChildInfo(leafNode);

        anotherChild.lastNode = leafNode;
        anotherChild.nextNode = leafNode.nextNode;
        if (anotherChild.nextNode != null) {
            anotherChild.nextNode.lastNode = anotherChild;
        }
        leafNode.nextNode = anotherChild;
        anotherChild.father = leafNode.father;

        //向右侧节点转移key和value
        for (int i = halfM; i < leafNode.keys.size(); i++) {
            insertKeyAndValue(anotherChild, leafNode.keys.get(i), leafNode.value.get(i));
        }
        int tempsize = leafNode.keys.size();
        for (int i = halfM; i < tempsize; i++) {
            leafNode.keys.remove(halfM);
            leafNode.value.remove(halfM);
        }


        insertKeyAndChild(leafNode.father, leafNode.keys.get(leafNode.keys.size() - 1), leafNode);
        insertKeyAndChild(anotherChild.father, anotherChild.keys.get(anotherChild.keys.size() - 1), anotherChild);
        //检查父节点
        if (leafNode.father.keys.size() > m) {
            if (leafNode.father.isRoot) {
                divideRootNode(leafNode.father);
            } else {
                divideMiddleNode(leafNode.father);
            }
        }
    }

    //当中间节点中key过多时，调用这个函数对其进行分裂
    void divideMiddleNode(BPNode middleNode) {
        if (middleNode.keys.size() <= m) {
            return;
        }
        //创建另一个中间节点，准备分裂
        BPNode anotherMiddle = new BPNode(1);
        deleteChildInfo(middleNode);

        anotherMiddle.lastNode = middleNode;
        anotherMiddle.nextNode = middleNode.nextNode;
        if (anotherMiddle.nextNode != null) {
            anotherMiddle.nextNode.lastNode = anotherMiddle;
        }
        middleNode.nextNode = anotherMiddle;
        anotherMiddle.father = middleNode.father;

        //向右侧节点转移key和child
        for (int i = halfM; i < middleNode.keys.size(); i++) {
            insertKeyAndChild(anotherMiddle, middleNode.keys.get(i), middleNode.chirdNode.get(i));
        }
        int tempsize = middleNode.keys.size();
        for (int i = halfM; i < tempsize; i++) {
            middleNode.keys.remove(halfM);
            middleNode.chirdNode.remove(halfM);
            //todo 检查正确性
        }
        //收养了儿子后，一定要记得让儿子认爹
        for (int i = 0; i < anotherMiddle.chirdNode.size(); i++) {
            anotherMiddle.chirdNode.get(i).father = anotherMiddle;
        }


        insertKeyAndChild(middleNode.father, middleNode.keys.get(middleNode.keys.size() - 1), middleNode);
        insertKeyAndChild(anotherMiddle.father, anotherMiddle.keys.get(anotherMiddle.keys.size() - 1), anotherMiddle);
        //检查父节点
        if (middleNode.father.keys.size() > m) {
            if (middleNode.father.isRoot) {
                divideRootNode(middleNode.father);
            } else {
                divideMiddleNode(middleNode.father);
            }
        }
    }

    //当根节点key过多时，调用这个函数对其进行分裂，产生两个中间节点
    void divideRootNode(BPNode rootNode) {
        if (rootNode.keys.size() <= m) {
            return;
        }
        //现在应该创建两个子节点
        BPNode leftNode = new BPNode(1);
        BPNode rightNode = new BPNode(1);


        leftNode.father = root;
        rightNode.father = root;

        leftNode.lastNode = null;
        leftNode.nextNode = rightNode;

        rightNode.lastNode = leftNode;
        rightNode.nextNode = null;

        //前halfM个child储存进第一个子节点
        for (int i = 0; i < halfM; i++) {
            leftNode.keys.add(root.keys.get(i));
            leftNode.chirdNode.add(root.chirdNode.get(i));
        }
        //剩余元素储存进第二个子节点
        for (int i = halfM; i < root.keys.size(); i++) {
            rightNode.keys.add(root.keys.get(i));
            rightNode.chirdNode.add(root.chirdNode.get(i));
        }

        //让所有孩子指向父亲
        for (int i = 0; i < leftNode.chirdNode.size(); i++) {
            leftNode.chirdNode.get(i).father = leftNode;
        }
        for (int i = 0; i < rightNode.chirdNode.size(); i++) {
            rightNode.chirdNode.get(i).father = rightNode;
        }

        root.keys.clear();
        root.value.clear();
        root.chirdNode.clear();
        root.keys.add(leftNode.keys.get(leftNode.keys.size() - 1));
        root.keys.add(rightNode.keys.get(rightNode.keys.size() - 1));
        root.chirdNode.add(leftNode);
        root.chirdNode.add(rightNode);

    }

    void delete(int key) {
        if (find(key) == null) {
            System.out.println("树中没有这个关键字");
            return;
        }
        //此时整棵树只有一个根节点
        if (root.chirdNode.size() <= 0) {
            removeKeyAndValue(root, key);
            return;
        }
        //此时说明该key在叶节点
        BPNode currentNode = findNode(key);
        removeKeyAndValue(currentNode, key);
        updateInfo(currentNode);
        //对移除之后的节点进行检查
        if (currentNode.keys.size() < halfM) {
            combineLeafNode(currentNode);
        }


    }

    //当叶节点中key过少时，调用这个函数对其进行合并
    void combineLeafNode(BPNode leafNode) {
        if (leafNode.keys.size() >= halfM) {
            return;
        }
        //如果左兄弟有多余的数据，向其借一个
        if (isLeftBrotherHasSurplusNode(leafNode)) {
            BPNode leftNode = leafNode.lastNode;

            leafNode.keys.add(0, leftNode.keys.get(leftNode.keys.size() - 1));
            leafNode.value.add(0, leftNode.value.get(leftNode.value.size() - 1));
            leftNode.keys.remove(leftNode.keys.size() - 1);
            leftNode.value.remove(leftNode.value.size() - 1);

            updateInfo(leafNode);
//            deleteChildInfo(leafNode);
//            insertKeyAndChild(leafNode.father, leafNode.keys.get(leafNode.keys.size() - 1), leafNode);

            updateInfo(leftNode);
//            deleteChildInfo(leftNode);
//            insertKeyAndChild(leftNode.father, leftNode.keys.get(leftNode.keys.size() - 1), leftNode);
            return;
        }
        //如果右兄弟有多余的数据，向其借一个
        if (isRightBrotherHasSurplusNode(leafNode)) {
            BPNode rightNode = leafNode.nextNode;
            leafNode.keys.add(rightNode.keys.get(0));
            leafNode.value.add(rightNode.value.get(0));
            rightNode.keys.remove(0);
            rightNode.value.remove(0);

            updateInfo(leafNode);
//            deleteChildInfo(leafNode);
//            insertKeyAndChild(leafNode.father, leafNode.keys.get(leafNode.keys.size() - 1), leafNode);

            updateInfo(rightNode);
//            deleteChildInfo(rightNode);
//            insertKeyAndChild(rightNode.father, rightNode.keys.get(rightNode.keys.size() - 1), rightNode);
            return;
        }
        //此时左右兄弟都没有多余的节点，只能合并
        //当左兄弟不为空时，与左兄弟合并
        if (leafNode.lastNode != null) {
            BPNode leftNode = leafNode.lastNode;
            BPNode rightNode = leafNode.nextNode;

            leftNode.keys.addAll(leafNode.keys);
            leftNode.value.addAll(leafNode.value);

            deleteChildInfoAndUpdate(leafNode);

            updateInfo(leftNode);
//            deleteChildInfo(leftNode);
//            insertKeyAndChild(leftNode.father, leftNode.keys.get(leftNode.keys.size() - 1), leftNode);

            leftNode.nextNode = rightNode;
            if (rightNode != null) {
                rightNode.lastNode = leftNode;
            }
            //检查leafNode的父节点是否需要合并
            if (leafNode.father.isRoot) {
                if (leafNode.father.keys.size() < 2) {
                    combineRootNode(leafNode.father);
                }
            }
            //此时父亲是一个中间结点
            else if (leafNode.father.keys.size() < halfM) {
                combineMiddleNode(leafNode.father);
            }
            return;
        }
        //当右兄弟不为空时，与右兄弟合并
        if (leafNode.nextNode != null) {
            BPNode leftNode = leafNode.lastNode;
            BPNode rightNode = leafNode.nextNode;

            for (int i = 0; i < leafNode.keys.size(); i++) {
                insertKeyAndValue(rightNode, leafNode.keys.get(i), leafNode.value.get(i));
            }

            deleteChildInfoAndUpdate(leafNode);

            updateInfo(rightNode);
//            deleteChildInfo(rightNode);
//            insertKeyAndChild(rightNode.father, rightNode.keys.get(rightNode.keys.size() - 1), rightNode);

            rightNode.lastNode = leftNode;
            if (leftNode != null) {
                leftNode.nextNode = rightNode;
            }
            //检查leafNode的父节点是否需要合并
            if (leafNode.father.isRoot) {
                if (leafNode.father.keys.size() < 2) {
                    combineRootNode(leafNode.father);
                }
            }
            //此时父亲是一个中间结点
            else if (leafNode.father.keys.size() < halfM) {
                combineMiddleNode(leafNode.father);
            }
            return;
        }
    }

    //当中间节点中key过少时，调用这个函数对其进行合并
    void combineMiddleNode(BPNode middleNode) {
        if (middleNode.keys.size() >= halfM) {
            return;
        }
        //如果左兄弟有多余的数据，向其借一个
        if (isLeftBrotherHasSurplusNode(middleNode)) {
            BPNode leftNode = middleNode.lastNode;

            middleNode.keys.add(0, leftNode.keys.get(leftNode.keys.size() - 1));
            middleNode.chirdNode.add(0, leftNode.chirdNode.get(leftNode.chirdNode.size() - 1));
            leftNode.keys.remove(leftNode.keys.size() - 1);
            leftNode.chirdNode.remove(leftNode.chirdNode.size() - 1);

            updateInfo(middleNode);
//            deleteChildInfo(middleNode);
//            insertKeyAndChild(middleNode.father, middleNode.keys.get(middleNode.keys.size() - 1), middleNode);

            updateInfo(leftNode);
//            deleteChildInfo(leftNode);
//            insertKeyAndChild(leftNode.father, leftNode.keys.get(leftNode.keys.size() - 1), leftNode);
            return;
        }
        //如果右兄弟有多余的数据，向其借一个
        if (isRightBrotherHasSurplusNode(middleNode)) {
            BPNode rightNode = middleNode.nextNode;
            middleNode.keys.add(rightNode.keys.get(0));
            middleNode.chirdNode.add(rightNode.chirdNode.get(0));
            rightNode.keys.remove(0);
            rightNode.chirdNode.remove(0);

            updateInfo(middleNode);
//            deleteChildInfo(middleNode);
//            insertKeyAndChild(middleNode.father, middleNode.keys.get(middleNode.keys.size() - 1), middleNode);

            updateInfo(rightNode);
//            deleteChildInfo(rightNode);
//            insertKeyAndChild(rightNode.father, rightNode.keys.get(rightNode.keys.size() - 1), rightNode);
            return;
        }
        //当左兄弟不为空时，与左兄弟合并
        if (middleNode.lastNode != null) {
            BPNode leftNode = middleNode.lastNode;
            BPNode rightNode = middleNode.nextNode;

            leftNode.keys.addAll(middleNode.keys);
            leftNode.chirdNode.addAll(middleNode.chirdNode);

            deleteChildInfoAndUpdate(middleNode);
            updateInfo(leftNode);
//            deleteChildInfo(leftNode);
//            insertKeyAndChild(leftNode.father, leftNode.keys.get(leftNode.keys.size() - 1), leftNode);

            leftNode.nextNode = rightNode;
            if (rightNode != null) {
                rightNode.lastNode = leftNode;
            }
            //检查middleNode的父节点是否需要合并
            if (middleNode.father.isRoot) {
                if (middleNode.father.keys.size() < 2) {
                    combineRootNode(middleNode.father);
                }
            }
            //此时父亲是一个中间结点
            else if (middleNode.father.keys.size() < halfM) {
                combineMiddleNode(middleNode.father);
            }
            return;
        }
        //当右兄弟不为空时，与右兄弟合并
        if (middleNode.nextNode != null) {
            BPNode leftNode = middleNode.lastNode;
            BPNode rightNode = middleNode.nextNode;

            for (int i = 0; i < middleNode.keys.size(); i++) {
                insertKeyAndChild(rightNode, middleNode.keys.get(i), middleNode.chirdNode.get(i));
            }

            deleteChildInfoAndUpdate(middleNode);
            updateInfo(rightNode);
//            deleteChildInfo(rightNode);
//            insertKeyAndChild(rightNode.father, rightNode.keys.get(rightNode.keys.size() - 1), rightNode);

            rightNode.lastNode = leftNode;
            if (leftNode != null) {
                leftNode.nextNode = rightNode;
            }
            //检查middleNode的父节点是否需要合并
            if (middleNode.father.isRoot) {
                if (middleNode.father.keys.size() < 2) {
                    combineRootNode(middleNode.father);
                }
            }
            //此时父亲是一个中间结点
            else if (middleNode.father.keys.size() < halfM) {
                combineMiddleNode(middleNode.father);
            }
            return;
        }
    }

    //当根节点中key过少时，调用这个函数对其进行合并
    void combineRootNode(BPNode rootNode) {

        if (rootNode.keys.size() >= 2) {
            return;
        }
        //此时，root结点下只有一个叶节点
        if (rootNode.chirdNode.get(0).isLeaf) {
            BPNode oneLeaf = rootNode.chirdNode.get(0);
            rootNode.keys.clear();
            rootNode.chirdNode.clear();
            rootNode.value.clear();

            rootNode.keys.addAll(oneLeaf.keys);
            rootNode.value.addAll(oneLeaf.value);
            return;
        }
        //此时root结点下，只有一个中间结点
        BPNode oneMiddle = rootNode.chirdNode.get(0);

        rootNode.keys.clear();
        rootNode.chirdNode.clear();
        rootNode.value.clear();

        rootNode.keys.addAll(oneMiddle.keys);
        rootNode.chirdNode.addAll(oneMiddle.chirdNode);

        for (int i = 0; i < rootNode.chirdNode.size(); i++) {
            rootNode.chirdNode.get(i).father = rootNode;
        }
    }

    //检查左兄弟是否有多余的节点
    boolean isLeftBrotherHasSurplusNode(BPNode node) {
        if (node.lastNode == null) {
            return false;
        }
        if (node.lastNode.keys.size() > halfM) {
            return true;
        }
        return false;
    }

    //检查右兄弟是否有多余的节点
    boolean isRightBrotherHasSurplusNode(BPNode node) {
        if (node.nextNode == null) {
            return false;
        }
        if (node.nextNode.keys.size() > halfM) {
            return true;
        }
        return false;
    }

    //从节点中只移除key
    int removeKey(BPNode node, int key) {
        int pos = node.keys.indexOf(key);
        if (pos == -1) {
            return -1;
        }
        node.keys.remove(pos);
        return pos;
    }

    //从节点中移除key和value
    void removeKeyAndValue(BPNode node, int key) {
        int pos = removeKey(node, key);
        if (pos == -1) {
            return;
        }
        node.value.remove(pos);
    }

    //根据key找到对应的叶节点
    BPNode findNode(int key) {
        int pos = -1;
        for (int i = 0; i < root.keys.size(); i++) {
            if (root.keys.get(i) >= key) {
                pos = i;
                break;
            }
        }
        if (pos == -1) {
            return null;
        }
        if (root.chirdNode.size() <= 0) {
            return root;
        }
        BPNode currentNode = root.chirdNode.get(pos);
        while (!currentNode.isLeaf) {
            pos = -1;
            for (int i = 0; i < currentNode.keys.size(); i++) {
                if (currentNode.keys.get(i) >= key) {
                    pos = i;
                    break;
                }
            }
            if (pos == -1) {
                return null;
            }
            currentNode = currentNode.chirdNode.get(pos);
        }
        if (currentNode.keys.indexOf(key) != -1) {
            return currentNode;
        }
        return null;
    }

    //根据key找到树中的value
    String find(int key) {
        BPNode node = findNode(key);
        if (node == null) {
            return null;
        }
        int pos = node.keys.indexOf(key);
        return node.value.get(pos);
    }

}

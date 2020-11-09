import java.util.ArrayList;
import java.util.Collections;

public class AVL<K extends Comparable<K>,V> {

    private Node l;

    public static void main(String[] args) {
        ArrayList<String> words=new ArrayList<>();
        Collections.addAll(words,"aaa","bbb","ccc","aaa","ddd","eee","aaa","bbb","fff");
        AVL<String,Integer> map=new AVL<>();
        for(String s:words){
            if(map.contains(s))
                map.set(s,map.get(s)+1);
            else
                map.add(s,1);
        }
        System.out.println("total different words:"+map.size());
        System.out.println("aaa的次数："+map.get("aaa"));
        System.out.println("bbb的次数："+map.get("bbb"));
        System.out.println("ccc的次数："+map.get("ccc"));
        System.out.println("是否为平衡二叉树："+map.isBalanced());


        map.delete("aaa");
       // System.out.println("aaa的次数："+map.get("aaa"));
        //System.out.println("total different words:"+map.size());

        map.delete("bbb");
       // System.out.println("bbb的次数："+map.get("bbb"));
        //System.out.println("total different words:"+map.size());

        map.delete("ccc");
        //System.out.println("ccc的次数："+map.get("ccc"));
        //System.out.println("total different words:"+map.size());

        map.delete("ddd");
        //System.out.println("ddd的次数："+map.get("ddd"));
        System.out.println("total different words:"+map.size());
    }
    private class Node{
        public K k;
        public V v;
        private int height;
        public Node left,right;
        public Node(K k,V v){
            this.k=k;
            this.v=v;
            height=1;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "k=" + k +
                    ", v=" + v +
                    '}';
        }
    }

    private Node root;
    private int size;

    public boolean isEmpty(){return size==0;}
    public int size(){return size;}

    //判断是否为平衡二叉树
    public boolean isBalanced(){
        return isBalanced(root);
    }
    private boolean isBalanced(Node node){
        if(node ==null)
            return true;
        if(Math.abs(balancerFactor(node))>1)
            return false;
        return isBalanced(node.left)&&isBalanced(node.right);
    }

    //计算node节点的平衡因子
    public int balancerFactor(Node node){
        if(node ==null)
            return 0;
        return getHeight(node.left)-getHeight(node.right);
    }

    public void add(K k,V v){
        root=add(root,k,v);
    }

    //返回该节点的高度
    public int getHeight(Node node){
        if(node ==null)
            return 0;
        return node.height;
    }

    //加入T1后 对节点y进行右旋转
    //          y                           x
    //        /   \                      /     \
    //       x    T3                   z        y
    //     /   \      ------------>   / \      /   \
    //    z    T2                   T1  (T1)  T2   T3
    //   / \
    //  T1 (T1)
    public Node rightRotate(Node y){
        Node x=y.left;
        Node T2=x.right;
        //向右旋转过程
        x.right=y;
        y.left=T2;
        y.height=Math.max(getHeight(y.left),getHeight(y.right))+1;
        x.height=Math.max(getHeight(x.left),getHeight(x.right))+1;
        return x;
    }

    //加入T3后 对节点y进行左旋转
    //          y                             x
    //        /   \                        /     \
    //       T1    x                      y       z
    //            / \   ------------>   /  \     /  \
    //           T2  z                 T1   T2 (T3)   T3
    //              /  \
    //            (T3) T3
    public Node leftRotate(Node y){
        Node x=y.right;
        Node T2=x.left;
        //向左旋转过程
        x.left=y;
        y.right=T2;
        y.height=Math.max(getHeight(y.left),getHeight(y.right))+1;
        x.height=Math.max(getHeight(x.left),getHeight(x.right))+1;
        return x;
    }

    /**
     * 添加元素递归方法
     * @param node
     * @param k
     * @param v
     * @return
     */
    private Node add(Node node,K k,V v){
        if(node ==null){
            size++;
            return new Node(k,v);
        }
        if(k.compareTo(node.k)<0){
            node.left=add(node.left,k,v);
        }else if(k.compareTo(node.k)>0){
            node.right=add(node.right,k,v);
        }else {
            node.v=v;
        }
        node.height=1+Math.max(getHeight(node.left),getHeight(node.right));
//        if(Math.abs(balancerFactor(node))>1)
//            System.out.println("unbalanced:"+balancerFactor(node));
        //维持平衡
        //添加节点在不平衡节点的左侧的左侧
        if(balancerFactor(node)>1&&balancerFactor(node.left)>=0)
            return rightRotate(node);
        //添加节点在不平衡节点的右侧的右侧
        if(balancerFactor(node)<-1&&balancerFactor(node.left)<=0)
            return leftRotate(node);
        //添加节点在不平衡节点的左侧的右侧
        if(balancerFactor(node)>1&&balancerFactor(node.left)<0){
            node.left=leftRotate(node.left);
            return rightRotate(node);
        }
        //添加节点在不平衡节点的右侧的左侧
        if(balancerFactor(node)<-1&&balancerFactor(node.right)>0){
            node.right=rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }


    //返回以node为根节点的子树，K为k的节点
    public Node getNode(Node node,K k){
        if(node ==null)
            return null;
        if(k.compareTo(node.k)==0)
            return node;
        else if(k.compareTo(node.k)<0){
            return getNode(node.left,k);
        }else {
            return getNode(node.right,k);
        }
    }

    public boolean contains(K k){
        return getNode(root,k)!=null;
    }

    public V get(K k){
        Node node = getNode(root, k);
        return node==null?null:node.v;
    }

    public void set(K k,V v){
        Node node = getNode(root, k);
        if(node==null)
            throw new IllegalArgumentException(k+"doesn't exist");
        node.v=v;
    }

    public void midOrder(){
        midOrder(root);
    }

    /**
     * 中序遍历递归方法
     */
    private void midOrder(Node node){
        if(node ==null)
            return;
        midOrder(node.left);
        System.out.print(node.toString()+"\t");
        midOrder(node.right);
    }



    //找出最小的节点
    public Node minNode(){
        return  minNode(root);
    }
    private Node minNode(Node node){
        if(node ==null)
            return null;
        while(node.left!=null) node = node.left;
        return node;
    }





//    public void deleteMin(){
//        deleteMin(root);
//    }
//    private Node deleteMin(Node node){
//        if(node.left==null){
//            size--;
//            Node right= node.right;
//            node.right=null;
//            return right;
//        }
//        node.left=deleteMin(node.left);
//        return node;
//    }

    /**
     * 删除任意节点
     */
    public V delete(K k){
        Node node = getNode(root, k);
        if(node!=null){
            root=delete(root,k);
            return node.v;
        }
        return null;
    }
    private Node delete(Node node,K k){
        if(node ==null)
            return null;
        Node retNode;
        if (k.compareTo(node.k) == 0) {
            if(node.left==null){ //待删除结点左子树为空
                Node right= node.right;
                node.right=null;
                size--;
                retNode= right;
            }
            else if(node.right==null){ //待删除结点右子树为空
                Node left= node.left;
                node.left=null;
                size--;
                retNode= left;
            }else{
                //待删除结点左右子树均不为空，则找到比待删除节点大的最小节点，即待删除结点右子树的最小节点
                //用该最小节点顶替待删除节点
                Node rightMin=minNode(node.right);
                rightMin.right =delete(node.right,rightMin.k);
                rightMin.left= node.left;
                node.left= node.right=null;
                retNode= rightMin;
            }

        }else if(k.compareTo(node.k)<0){
            node.left=delete(node.left,k);
            retNode=node;
        }else {
            node.right=delete(node.right,k);
            retNode=node;
        }

        if(retNode==null)
            return null;
        retNode.height=1+Math.max(getHeight(retNode.left),getHeight(retNode.right));
//        if(Math.abs(balancerFactor(node))>1)
//            System.out.println("unbalanced:"+balancerFactor(node));
        //维持平衡
        //添加节点在不平衡节点的左侧的左侧
        if(balancerFactor(retNode)>1&&balancerFactor(retNode.left)>=0)
            return rightRotate(retNode);
        //添加节点在不平衡节点的右侧的右侧
        if(balancerFactor(retNode)<-1&&balancerFactor(retNode.left)<=0)
            return leftRotate(retNode);
        //添加节点在不平衡节点的左侧的右侧
        if(balancerFactor(retNode)>1&&balancerFactor(retNode.left)<0){
            retNode.left=leftRotate(retNode.left);
            return rightRotate(retNode);
        }
        //添加节点在不平衡节点的右侧的左侧
        if(balancerFactor(retNode)<-1&&balancerFactor(retNode.right)>0){
            retNode.right=rightRotate(retNode.right);
            return leftRotate(retNode);
        }

        return retNode;
    }


}

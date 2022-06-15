import java.util.*;

class Node{
    char Name;          //标记名字
    int freq;           //标记频数
    int Asc;
    String bi_node;
    Huff_Node parent; Huff_Node right; Huff_Node left;
    String huff_code;
    
    public Node(char name){
        this.Name = name;
        this.Asc = (int) name;
        this.freq = 1;          //初始化频数为1，因为添加node的时候这个字母肯定已经出现过了
    }
}

public class HuffmanTree{

    String str;
    String compress;                    //由huffman码0|1的压缩字符
    ArrayList<Huff_Node> node_arr;           //可以改变长度的装node的数组
    Huff_Node root;                          //自带根节点
    String[] dict = new String[128];

    public void get_arr(String str){
        //得到节点数组
        this.str = str;                         //拷贝相应的文件或者字符串作为元字符
        ArrayList<Huff_Node> node_arr = new ArrayList<Huff_Node>();
        //Node lf = new Node('\n');             //换行问题还没有解决
        //node_arr.add(lf);
        for(int i = 0; i < str.length(); i++){
            boolean flage = false;          //标记 node_arr中没有出现过

            for(int j = 0; j<node_arr.size();j++){

                if(node_arr.get(j).Name == str.charAt(i)){
                    node_arr.get(j).freq += 1;
                    flage = true;
                    break;
                }
            }
            if(flage == false){
                Huff_Node newNode = new Huff_Node(str.charAt(i));
                node_arr.add(newNode);
            }
        }
        this.node_arr = node_arr;
        //return node_arr;
    }
    
    public void print_arr(){
        for(int i = 0;i<this.node_arr.size();i++){
            System.out.print("Name " + this.node_arr.get(i).Name + " freq ");
            System.out.print(this.node_arr.get(i).freq + " binary ");
            System.out.print(this.node_arr.get(i).bi_node + " huff ");
            System.out.print(this.node_arr.get(i).huff_code + " ");
            System.out.println();
        }
    }

    public int get_min_index(){
        int min = 1000000000;
        int index=-1;           //没有找到最小的就是返回-1，说明列表空了
        for(int i = 0; i<this.node_arr.size();i++){
            if(this.node_arr.get(i).freq<min){
                min = this.node_arr.get(i).freq;
                index = i;
            }
        }
        return index;
    }

    public void tree_grow(){
        //蒋两个最小节点化成一个支，将枝节点并入node_arr,并删除原来的两个最小节点
        ArrayList<Huff_Node> copy_arr = new ArrayList<Huff_Node>() ;
        for(int i = 0; i<node_arr.size();i++){
            copy_arr.add(node_arr.get(i));          //深拷贝一次node_arr 一会用来还原node_arr
        }
        while(this.node_arr.size() != 1){
            int min1_idx = get_min_index();
            Huff_Node min1 = node_arr.get(min1_idx) ;
            min1.bi_node = "0";                     //记左节点为bi = "0"
            this.node_arr.remove(min1_idx);

            int min2_idx = get_min_index();
            Huff_Node min2 = node_arr.get(min2_idx);
            min2.bi_node = "1";                     //记左节点为bi = "1"
            this.node_arr.remove(min2_idx);
            
            Huff_Node newNode = new Huff_Node('*');
            newNode.freq = min1.freq + min2.freq;
            newNode.left = min1;
            newNode.right = min2;
            min1.parent = newNode;
            min2.parent = newNode;

            node_arr.add(newNode);
            //this.print_arr();
        }
        this.root = this.node_arr.get(0);
        this.root.bi_node = "0";
        this.node_arr = copy_arr;
    }

    public void print_leaf(Huff_Node tempNode){      //用dfs打印叶子
        //System.out.println(tempNode.bi_node);
        if(tempNode.left != null){
            print_leaf(tempNode.left);
            if(tempNode.right != null){
                print_leaf(tempNode.right);
            }
        }
        else if(tempNode.right != null){
            print_leaf(tempNode.right);
        }
        else{
            System.out.print(tempNode.Name + " ");
            //System.out.print(tempNode.bi_node + " ");

            System.out.println(tempNode.freq);

        }
    }

    public void set_huffCode(){
        for(int i = 0;i<this.node_arr.size();i++){
            Huff_Node temp = this.node_arr.get(i);
            String huff_code = "";
            while(temp.parent!= null){
                huff_code = temp.bi_node.concat(huff_code);
                temp = temp.parent;
            }
            //huff_code = temp.bi_node.concat(huff_code);       //优化第一个root一定是0,可以省略
            this.node_arr.get(i).huff_code = huff_code;
        }
    }

    public void set_dict(){
        for(int i = 0;i<node_arr.size();i++){
            int j = this.node_arr.get(i).Asc;
            if(j<128){              //如果超过了128，有中文汉字的情况，先不存
                dict[j] = this.node_arr.get(i).huff_code;
            }
        }
    }

    public void print_dict(){
        System.out.print("DICTIONARY>>??");
        for(int i = 0;i<dict.length;i++){
            if(dict[i] == null){
                System.out.print( "[_] ");
            }
            else{
                System.out.print(dict[i] + " ");
            }
        }
        System.out.println();
    }

    public void print_compress(){
        String cmps = "";
        for(int i = 0;i < str.length();i++){
            int index = (int) str.charAt(i);
            if(index<128)                               //扫描到了汉字，如果超过了128，有中文汉字的情况，先不压入
                cmps = cmps.concat(dict[index]);
        }
        System.out.println("COMPRESS Verison>>>");
        System.out.println(cmps);
        compress = cmps;
    }

    public void decompress(String huffStr){
        String decomp = "";
        Huff_Node temp = root;
        for(int i = 0;i<huffStr.length();i++){
            System.out.print(i + " ");
            if(huffStr.charAt(i) == '0'){                     //向左走
                temp = temp.left;
            }
            else{                                           //向右走
                temp = temp.right;
            }
            if(temp.right== null & temp.left == null){      //走到叶节点了
                decomp = decomp + temp.Name;
                System.out.print(temp.Name + " ");
                temp = root;
            }
            else{
                System.out.println("[_] ");
            }
        }
        System.out.println("DECOMPRESSION>>> " + decomp);
    }
    public static void main(String[] args) {

        String str = "ABCASDA";

        HuffmanTree tree = new HuffmanTree();
        tree.get_arr(str);
        System.out.println();
        tree.tree_grow();
        tree.set_huffCode();
        tree.print_arr();
        tree.set_dict();
        tree.print_dict();
        tree.print_compress();
        System.out.println("String>>>" + tree.str);
        System.out.println("length>>>" + tree.compress.length());
        tree.decompress(tree.compress);
    }
}
import java.io.*;
import java.util.*;

class Huff_Node{
    char Name;          //Tag name
    int freq;           //Mark frequency
    int Asc;
    String bi_node;
    Huff_Node parent; Huff_Node right; Huff_Node left;
    String huff_code;
        
    public Huff_Node(char name){
        this.Name = name;
        this.Asc = (int) name;
        this.freq = 1;          
        //The initialization frequency is 1,the letter must 
        //have already appeared when node was added  
    }    
}

class Huff_Tree{
    String str;
    String compress;                    //By the compression of Huffman code 0 | 1 characters
    ArrayList<Huff_Node> node_arr;           //The length of the node array can be changed
    Huff_Node root;                          //Built-in root node
    String[] dict = new String[128];

    public Huff_Tree(String inputText){
        this.get_arr(inputText);
        this.tree_grow();
        this.set_huffCode();
        this.set_dict();
        this.set_compress();
    }

    public void get_arr(String str){
        //Get an array of nodes
        this.str = str;                         //Copy the corresponding file or string as metacharacters
        ArrayList<Huff_Node> node_arr = new ArrayList<Huff_Node>();

        for(int i = 0; i < str.length(); i++){
            boolean flage = false;          //The flag node ARR does not appear

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
        int index=-1;           //If the smallest is not found, -1 is returned, indicating that the list is empty
        for(int i = 0; i<this.node_arr.size();i++){
            if(this.node_arr.get(i).freq<min){
                min = this.node_arr.get(i).freq;
                index = i;
            }
        }
        return index;
    }

    public void tree_grow(){
        //Merge the two minimum nodes into one branch, merge the branches into node_arr, and delete the original two minimum nodes  
        ArrayList<Huff_Node> copy_arr = new ArrayList<Huff_Node>() ;
        for(int i = 0; i<node_arr.size();i++){
            copy_arr.add(node_arr.get(i));          //A deep copy of node ARr will be used to restore node ARr
        }
        while(this.node_arr.size() != 1){
            int min1_idx = get_min_index();
            Huff_Node min1 = node_arr.get(min1_idx) ;
            min1.bi_node = "0";                     //Let's call the left node bi = "0"
            this.node_arr.remove(min1_idx);

            int min2_idx = get_min_index();
            Huff_Node min2 = node_arr.get(min2_idx);
            min2.bi_node = "1";                     //Let's call the left node bi = "1"
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

    public void print_leaf(Huff_Node tempNode){      //Print leaves with DFS
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
            //huff_code = temp.bi_node.concat(huff_code);       //Optimize the first root must be 0, can be omitted
            this.node_arr.get(i).huff_code = huff_code;
        }
    }

    public void set_dict(){
        for(int i = 0;i<node_arr.size();i++){
            int j = this.node_arr.get(i).Asc;
            if(j<128){              //If it exceeds 128, Chinese characters will not be stored
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

    public void set_compress(){
        String cmps = "";
        for(int i = 0;i < str.length();i++){
            int index = (int) str.charAt(i);
            if(index<128)                               
            //Chinese characters have been scanned. If the number exceeds 128, do not press in the case of Chinese characters  
                cmps = cmps.concat(dict[index]);
        }
        //System.out.println("COMPRESS Verison>>>");
        //System.out.println(cmps);
        compress = cmps;
    }

    public void decompress(String huffStr){
        String decomp = "";
        Huff_Node temp = root;
        for(int i = 0;i<huffStr.length();i++){
            System.out.print(i + " ");
            if(huffStr.charAt(i) == '0'){                     //turn left
                temp = temp.left;
            }
            else{                                           //turn right
                temp = temp.right;
            }
            if(temp.right== null & temp.left == null){      //We're at the leaf
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
}

public class HuffmanCompression {
    public static String getCompressedCode(String inputText, String[] huffmanCodes) {
        String compressedCode = null;      
        Huff_Tree tree = new Huff_Tree(inputText);
        compressedCode = tree.compress;
        return compressedCode;
    }
    public static String[] getHuffmanCode(String inputText) {
        String[] huffmanCodes = new String[128];
        Huff_Tree tree = new Huff_Tree(inputText);
        huffmanCodes = tree.dict;
        return huffmanCodes;
    }

    //public static void main(String[] args) {
    //    String inputText = "ASDFGHJKLQWERTYUIOP!@#$%^&*()1234567890";
    //    System.out.println(getCompressedCode(inputText, getHuffmanCode(inputText)));
    //    
    //}
    public static void main(String[] args) throws Exception {
        // obtain input text from a text file encoded with ASCII code

        String inputText = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(args[0])), "US-ASCII");
        // get Huffman codes for each character and write them to a dictionary file
        String[] huffmanCodes = HuffmanCompression.getHuffmanCode(inputText);
        FileWriter fwriter1 = new FileWriter(args[1], false);
        BufferedWriter bwriter1 = new BufferedWriter(fwriter1);
    
        for (int i = 0; i < huffmanCodes.length; i++) 
            if (huffmanCodes[i] != null) {
                bwriter1.write(Integer.toString(i) + ":" + huffmanCodes[i]);
                bwriter1.newLine();
            }
        bwriter1.flush();
        bwriter1.close();
        // get compressed code for input text based on huffman codes of each character
        String compressedCode = HuffmanCompression.getCompressedCode(inputText, huffmanCodes);
        FileWriter fwriter2 = new FileWriter(args[2], false);
        BufferedWriter bwriter2 = new BufferedWriter(fwriter2);
        if (compressedCode != null) 
            bwriter2.write(compressedCode);
        bwriter2.flush();
        bwriter2.close();
    }
}

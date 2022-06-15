import java.util.ArrayList;
import java.io.*;

class Node{
    int[] triple = new int[3];
    Node pre;
    Node next;

    public Node(int i, int j, int v){
        this.triple[0] = i;
        this.triple[1] = j;
        this.triple[2] = v;
        //this.pre = pre;
        //this.next = next;
    }

    public void show(){
        int m = this.triple[0];
        int n = this.triple[1];
        int v = this.triple[2];
        System.out.println("<"+m+","+n+","+v+">");
    }
}

class Matrix{

    int m;      //row number
    int n;      //column number
    ArrayList<Node> queue = new ArrayList<Node>();

    public Matrix(int m,int n){
        this.m = m;
        this.n = n;
    }

    public void enqueue(Node newNode){
        if(this.queue.isEmpty()){
            this.queue.add(newNode);
        }

        this.queue.get(queue.size()-1).next = newNode;
        newNode.pre = this.queue.get(queue.size()-1);
        this.queue.add(newNode);
    }

    public void printMatrix(){
        int[][] map = new int[this.m][this.n];
        for(Node node:this.queue){
            //In Object Matrix the pos is from 0 to size-1
            //In Node the pos is from 1 to size
            map[node.triple[0]-1][node.triple[1]-1] = node.triple[2];
        }
        for(int i =0;i<map.length;i++){
            System.out.println();
            for(int j = 0;j<map[i].length;j++){
                System.out.print(map[i][j] + " ");
            }
        }
        System.out.println();
    }
    public void show_queue(){
        for(Node node:this.queue){
            int m = node.triple[0];
            int n = node.triple[1];
            int v = node.triple[2];
            System.out.println("<"+m+","+n+","+v+">");
        }
        System.out.println();
    }
}

public class AddSparseMatrix{
    
    public static Matrix AddMatrix(Matrix m1, Matrix m2){
        if(m1.m == m2.m & m1.n == m2.n){
            Matrix m3 = new Matrix(m1.m, m1.n);
            int r = 0; int s = 0;
            while(r <m1.queue.size() & s<m2.queue.size()){
                Node node1 = m1.queue.get(r);
                Node node2 = m2.queue.get(s);
                Node node3 = new Node(0,0,0);
                if(is_same_pos(node1,node2) == true){//if they are of the same position
                    node3 = new Node(node1.triple[0], node1.triple[1],
                    node1.triple[2]+node2.triple[2]);
                    s++; r++;
                }
                else{
                    //pick the smallest index
                    if(node1.triple[0] == node2.triple[0]){
                        if(node1.triple[1] < node2.triple[1]){
                            node3 = node1; r++;
                        }
                        else if(node1.triple[1] > node2.triple[1]){
                            node3 = node2;s++;
                        }
                    }
                    if(node1.triple[0] < node2.triple[0]){
                        node3 = node1;r++;
                    }
                    else if(node1.triple[0] > node2.triple[0]){
                        node3 = node2;s++;
                    }
                }
                //System.out.println("first");
                //node3.show();
                if(node3.triple[2] != 0){
                    m3.queue.add(node3);
                }
            }
            
            while(r < m1.queue.size()){
                m3.queue.add(m1.queue.get(r));
                
                //System.out.println("second");
                //m1.queue.get(r).show();
                r++;

            }
            while(s<m2.queue.size()){
                m3.queue.add(m2.queue.get(s));
                
                //System.out.println("third");
                //m2.queue.get(s).show();
                s++;

            }
            return m3;
        }
        return null;
    }

    public static boolean is_same_pos(Node node1,Node node2){
        if(node1.triple[0] == node2.triple[0] & node1.triple[1] == node2.triple[1]){
            return true;
        }
        else{
            return false;
        } 
    }

    public static Node get_small(Node node1,Node node2){
        if(node1.triple[0] == node2.triple[0]){
            if(node1.triple[1] < node2.triple[1]){
                return node1;
            }
            else{
                return node2;
            }
        }
        if(node1.triple[0] < node2.triple[0]){
            return node1;
        }
        else{
            return node2;
        }
    }

    public static ArrayList<Integer> get_char(String str,char c){
        ArrayList<Integer> spc_pos = new ArrayList<Integer>();
        for(int i = 0;i<str.length();i++){
            if(str.charAt(i) == c){
                spc_pos.add((Integer)i);
            }
        }
        return spc_pos;
    }    

    public static ArrayList<Node> get_nodes(String str){

        ArrayList<Node> nodes = new ArrayList<Node>();
        ArrayList<Integer> spc_pos = get_char(str,' ');
        ArrayList<Integer> colon_pos = get_char(str, ':');
        int m = Integer.parseInt(str.substring(0,spc_pos.get(0) )) ;
        //System.out.println();
        //System.out.println(m);
        int j = 0;
        for(int i = 0;i<spc_pos.size()-1;i++){
            int n = Integer.parseInt(str.substring(spc_pos.get(i)+1,colon_pos.get(j) ));
            int v = Integer.parseInt(str.substring(colon_pos.get(j)+1,spc_pos.get(i+1) ));
            Node newNode = new Node(m, n, v);
            //System.out.println("<"+m+","+n+","+v+">");
            nodes.add(newNode);
            j++;
        }
        return nodes;
    }

    public static Matrix get_matrix(BufferedReader buff)throws Exception{
        String str = "";
        str = buff.readLine();
        //System.out.println(str);
        int comma1 = str.indexOf(",");
        int m1 = Integer.parseInt(str.substring(0, comma1));
        int n1 = Integer.parseInt(str.substring(comma1+2));//comma + space
        Matrix matrix = new Matrix(m1, n1);
        while(str != null){
            str = buff.readLine();
            //System.out.println(str);
            if(str != null){
                //System.out.println(str);
                ArrayList<Node> nodes = get_nodes(str);
                for(Node node:nodes){
                    matrix.enqueue(node);
                }
            }
        }
        matrix.queue.remove(0);
        return matrix;
    }

    public static void main(String[] args)throws Exception {
        FileReader file1 = new FileReader(args[0]);
        BufferedReader buff1 = new BufferedReader(file1);

        FileReader file2 = new FileReader(args[1]);
        BufferedReader buff2 = new BufferedReader(file2);

        Matrix M1 = get_matrix(buff1);
        System.out.println("M1 get!");
        Matrix M2 = get_matrix(buff2);
        System.out.println("M2 get!");
        //matrix1.show_queue();
        //matrix2.show_queue();

        Matrix M3 = AddMatrix(M1, M2);
        //matrix3.show_queue();
        System.out.println("M3 get!");

        FileWriter fwriter = new FileWriter(args[2], false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
            
        StringBuffer strBuff = new StringBuffer();
        strBuff.append(M3.m + ", "+ M3.n);
        bwriter.write(strBuff.toString());
        strBuff.delete(0,strBuff.length());
        bwriter.newLine();

        int w = 0 ;
        
        for(int row = 1;row<M3.m+1;row++){
            strBuff.append(row+" ");
            while(w<M3.queue.size() && M3.queue.get(w).triple[0] == row ){
                Node temp = M3.queue.get(w);
                strBuff.append(Integer.toString(temp.triple[1])+":");
                strBuff.append(Integer.toString(temp.triple[2])+" ");
                w++;
            }
            if(strBuff.indexOf(":") == -1){
                strBuff.append(":");
            }
            bwriter.write(strBuff.toString());
            bwriter.newLine();
            strBuff.delete(0,strBuff.length());     
        }
        System.out.println("Write done!");
        bwriter.flush();
        bwriter.close();
    }
}

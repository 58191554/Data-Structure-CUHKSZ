import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;


class State{
    int[] board;
    int moves;      
    State pre_state;
    int Mht_sum;
    int zero_id;
    int priority;
    Integer hash_code;

    public State(State pre_state, int moves, int[] board){
        this.pre_state = pre_state;
        this.moves = moves;
        this.board = board;
        this.Mht_sum = this.get_Mht_sum();
        this.priority = this.moves + this.Mht_sum;
        this.get_zeroid();
    }

    public int get_Mht_sum(){
        int sum = 0;
        int[] arr = this.board;
        for(int i = 0;i<arr.length;i++){
            if(arr[i] != 0){
                int x = (arr[i]-1)/3;
                int y = (arr[i]-1)%3;
                int x0 = i/3;
                int y0 = i%3;
                int x_dis = Math.abs(x-x0);
                int y_dis = Math.abs(y-y0);
                sum = sum + x_dis + y_dis;
            }            
        }
        this.Mht_sum = sum;
        return sum;
    }

    public int get_zeroid(){
        for(int i = 0;i<this.board.length;i++){
            if(this.board[i] == 0){
                zero_id = i;
            }//Search for the position of 0
        }
        return zero_id;
    }

    public int get_priority(){
        this.priority = this.moves + this.Mht_sum;
        return this.priority;
    }

    public Integer get_hashCode(){
        int hash_code = 0;
        for(int i:this.board){
            hash_code = hash_code*10+i;
        }
        this.hash_code = (Integer) hash_code;
        return (Integer) hash_code;
    }

    public ArrayList<State> derive_son(){
        //for a state, we can derive possible next states
        ArrayList<State> sons = new ArrayList<State>();
        if(zero_id%3 != 0){//mod = 0 can't left
            State leftState = new State(this, this.moves+1, deepCopy(this.board));
            //Make a deep copy of its parent node first
            leftState.board[zero_id] = leftState.board[zero_id-1];
            leftState.board[zero_id-1] = 0;
            leftState.get_Mht_sum();    //renew the data field
            leftState.get_zeroid();
            leftState.get_priority();
            sons.add(leftState);
        }

        if(zero_id%3 != 2){//mod = 2 can't right
            State rightState = new State(this, this.moves+1, deepCopy(this.board));
            //Make a deep copy of its parent node first
            rightState.board[zero_id] = rightState.board[zero_id+1];
            rightState.board[zero_id+1] = 0;
            rightState.get_Mht_sum();
            rightState.get_zeroid();
            rightState.get_priority();
            sons.add(rightState);
        }

        if(zero_id/3 != 0){//The quotient is 0 and can't go up
            State upState = new State(this, this.moves+1, deepCopy(this.board));
            upState.board[zero_id] = upState.board[zero_id-3];
            upState.board[zero_id-3] = 0;
            upState.get_Mht_sum();
            upState.get_zeroid();
            upState.get_priority();
            sons.add(upState);
        }
        
        if(zero_id/3 != 2){//A quotient of two cannot go up
            State downState = new State(this, this.moves+1, deepCopy(this.board));
            downState.board[zero_id] = downState.board[zero_id+ 3];
            downState.board[zero_id+3] = 0;
            downState.get_Mht_sum();
            downState.get_zeroid();
            downState.get_priority();
            sons.add(downState);
        }
        return sons;
    }

    public int[] deepCopy(int[] arr){
        //deep copy the board
        int[] newArr = new int[arr.length];
        for(int i = 0;i<arr.length;i++){
            newArr[i] = arr[i];
        }
        return newArr;
    }

    public void print(){
        for(int i = 0;i<9;i++){
            if(i%3 == 0)
                System.out.println();
            if(this.board[i] == 0)
                System.out.print(" " + " ");
            else
                System.out.print(this.board[i] + " ");
        }
        System.out.println("priority: " + this.priority);
        System.out.println();
    }

    public String[] strOutput(){
        //out put the board as a square string
        String[] Str_arr = new String[3];
        for(int i = 0;i<3;i++){
            String output_str = "";
            for(int j = 0;j<3;j++){
                output_str = output_str + this.board[3*i+j];
                if(j==2){
                    Str_arr[i] = output_str;
                }
                else {
                    output_str = output_str + " ";
                }
            }
        }
        return Str_arr;
    }
}

class Loc_map{
    ArrayList<int[]> map = new ArrayList<int[]>();

    public void add(int K_prio){
        for(int i = 0;i<map.size();i++){
            if(this.map.get(i)[0] == K_prio){
                for(int j = i+1;j<this.map.size();j++){
                    this.map.get(j)[1] += 1;
                }
            return;
            }
        }
        int[] newK_V = {K_prio,this.map.size()};
        this.map.add(newK_V);
    }

    public void remove(int K_prio){
        for(int i = 0;i<map.size();i++){
            if(this.map.get(i)[0] == K_prio){

            }
        }
    }

    public void show_map(){
        for(int[] kv : map){
            System.out.print("<key="+kv[0]+",value="+kv[1]+">");
        }
        System.out.println();
    }
}

class PrioQueue{
    ArrayList<State> list = new ArrayList<State>();
    HashSet<Integer> hash = new HashSet<Integer>();

    public PrioQueue(State ini_state){//initialize the queue with the 
        this.list.add(ini_state);

        this.hash.add(ini_state.get_hashCode());
    }

    public boolean arr_equal(int[] arr1, int[] arr2){
        if(arr1.length != arr2.length){
            return false;
        }
        else{
            for(int i = 0;i<arr1.length;i++){
                if(arr1[i]!= arr2[i]){
                    return false;
                }
            }
            return true;
        }
    }

    public boolean isEnd(){
        int[] end = {1,2,3,4,5,6,7,8,0};
        if(arr_equal(this.list.get(0).board, end)){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean State_repeat(State inputState){
        if(inputState.pre_state!= null&inputState.pre_state.pre_state!=null){
            int[] grandpa = inputState.pre_state.pre_state.board;
            if(arr_equal(grandpa, inputState.board))
                return true;
        }
        return false;
    }

    public void enqueue(State newState){
        
        if(this.list.isEmpty()){
            this.list.add(newState);
        }
        else{
            for(int i = 0;i<this.list.size();i++){
                //this step cost too much time
                if(newState.priority<this.list.get(i).priority){
                    
                    this.list.add(i,newState);
                    this.hash.add(newState.hash_code);
                    return;
                }
            }
            this.list.add(newState);
        }    
    
    }

    public void insert_sons(){
        State cast_state = this.list.get(0);
        ArrayList<State> sons = cast_state.derive_son();
        for(State son:sons){
            if(State_repeat(son) != true &hash.contains(son.get_hashCode())!= true){
                this.enqueue(son);
            }
            //else{
            //    System.out.println("REPEAT");
            //}
            //this.enqueue(son);
        }
        this.list.remove(0);
        //this.cast_list.add(cast_state);
    }

    public ArrayList<String> solution(){
        while(this.isEnd() != true){
            this.insert_sons();
            System.out.println();
            //for(State cast:this.cast_list){
            //    cast.print();
            //}
        }
        System.out.println("Steps>>"+this.list.get(0).moves);
        System.out.println("ENd!");
        
        State temp = this.list.get(0);
        ArrayList<State> solulList = new ArrayList<State>();
        while(temp.pre_state != null){
            solulList.add(0,temp);
            temp = temp.pre_state;
        }
        solulList.add(0,temp);

        for(State e:solulList){
            e.print();//print out in the console
        }

        ArrayList<String> answerline_arr = new ArrayList<String>();
        for(int i = 0;i<solulList.size();i++){
            //solulList.get(i).print();
            String[] answer =  solulList.get(i).strOutput();
            for(int j = 0;j<3;j++){
                answerline_arr.add(answer[j]);
            }
        }
        return answerline_arr;
    }

    public void printQueue(){
        for(State state:this.list){
            state.print();
        }
    }

}

public class Puzzle8{

    public static boolean check_solvable(int[] arr){
        return true;
    }

    public static void main(String[] args)throws Exception {

        FileReader init_file = new FileReader(args[0]);
        BufferedReader init_buff = new BufferedReader(init_file);
        String str = "";
        int[] init_nums = new int[9];
        int j = 0;
        while(str != null){
            str = init_buff.readLine();
            if(str != null){
                System.out.println(str);
                for(int i = 0;i<str.length();i++){
                    char char_i = str.charAt(i);
                    if(Character.isDigit(char_i)){
                        init_nums[j] = (int) char_i-48;
                        j++;
                    }
                }
            }
        }
        for(int e: init_nums){
            System.out.print(e + " ");
        }

        State state = new State(null,0,init_nums);
        PrioQueue queue = new PrioQueue(state);
        state.print();
        System.out.println("state.Mht_sum: "+state.Mht_sum);
        System.out.println("state.priority: " + state.priority);
                //
        //queue.solution();        

        init_buff.close();  
        init_file.close();

        System.out.println(queue.solution());
        

        ArrayList<String> answer_arr = queue.solution();
        FileWriter fwriter = new FileWriter(args[1], false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
        
        for(int i = 0;i<answer_arr.size()-1;i++){
            bwriter.write(answer_arr.get(i));
            bwriter.write("\r\n");
            if(i%3 == 2){
                bwriter.newLine();
            }
        }
        bwriter.write(answer_arr.get(answer_arr.size()-1));
        bwriter.flush();
        bwriter.close();
    }
}

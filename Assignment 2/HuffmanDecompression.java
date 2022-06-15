import java.io.*;
import java.util.ArrayList;

public class HuffmanDecompression{

    public static String decompress(String str, ArrayList<String> huff_arraylist, ArrayList<Integer> ASC_arr){
        
        int i = 0;
        String output_str = "";
        for(int j = 0;j<str.length();j++){
            int index = huff_arraylist.indexOf(str.substring(i,j));
            if(index !=-1){
                int asc =  ASC_arr.get(index);
                char letter = (char) asc;
                //System.out.print(letter + " ");
                output_str = output_str+letter;
                i = j;
            }
        }        
        return output_str;
    }
    public static void main(String[] args) throws Exception {

        FileReader compresFile = new FileReader(args[0]);
        BufferedReader compresRead = new BufferedReader(compresFile);
        String compress_str = compresRead.readLine();

        FileReader freader = new FileReader(args[1]);
        BufferedReader breader = new BufferedReader(freader);
        String dict_str = "";
        ArrayList<Integer> Asc_list = new ArrayList<Integer>();
        ArrayList<String> Huff_list = new ArrayList<String>();
        while(dict_str != null){
            dict_str = breader.readLine();
            if(dict_str != null){
                int asc = Integer.parseInt(dict_str.substring(0, dict_str.indexOf(":"))); 
                Asc_list.add(asc);
                Huff_list.add(dict_str.substring(dict_str.indexOf(":")+1));
            }
        }
        for(int i = 0;i<Huff_list.size();i++){
            System.out.print(Huff_list.get(i) + " ");
        }
        
        System.out.println(decompress(compress_str, Huff_list, Asc_list));
        breader.close();
        compresFile.close();

        String decompression = decompress(compress_str, Huff_list, Asc_list);
        FileWriter fwriter = new FileWriter(args[2], false);
        BufferedWriter bwriter = new BufferedWriter(fwriter);
        if (decompression != null) 
            bwriter.write(decompression);
        bwriter.flush();
        bwriter.close();

    }
}

import java.util.HashSet;
import java.util.Map;


public class t {
    public static void main(String[] args) {
        HashSet<int[]> hash = new HashSet<int[]>();
        int[] ar = {1,2};
        int[] arr = {1,2,3};
        int[] arrr = {1,2,3,4};
        int[] arrrr = {1,2,3,4,5};
        hash.add(ar);
        hash.add(arr);
        hash.add(arrr);
        hash.add(arrrr);
        int[] arrii = {1,2,3,4,5};
        if(hash.contains(arrii))
            System.out.print("Ture");

 
    }
}

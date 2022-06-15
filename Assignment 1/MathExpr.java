import java.util.*;

public class MathExpr {

    public static boolean is_space(String str){
        boolean flag = true;
        for(int i = 0;i<str.length();i++){
            if(str.charAt(i) != ' ')
                flag = false;
        }
        return flag;
    }
    public static int get_opr(String str, char opr){
        Stack<Integer> brak_st = new Stack<Integer>();
        if(opr == '/' | opr == '-'){//find last opr outside the outermost layer ()
            for(int i = str.length()-1;i >= 0; i--){
                char c = str.charAt(i);
                if(c == ')')
                    brak_st.push(i);
                else if(c == '(')
                    brak_st.pop();
                else if(c == opr & brak_st.empty() == true){
                    return i;
                }
            }
        }else if(opr == '+' | opr == '*'){//find first opr outside the outermost layer ()
            for(int i = 0; i<str.length();i++){
                char c = str.charAt(i);
                if(c=='(')
                    brak_st.push(i);
                else if (c == ')')
                    brak_st.pop();
                else if(c == opr & brak_st.empty() == true)
                    return i;
            }
        }
        return -1;
    }
    public static double cal(String str){
        //System.out.println(str);
        //mark every opr or func position
        int plus = get_opr(str, '+'); int mul = get_opr(str, '*');
        int min = get_opr(str, '-'); int div = get_opr(str, '/');
        int sin = str.indexOf("sin"); int cos = str.indexOf("cos");
        int tan = str.indexOf("tan"); int sqrt = str.indexOf("sqrt");
        int l = str.indexOf("("); int r = str.lastIndexOf(")");
        
        if(plus != -1){         //plus exist
            if(is_space(str.substring(0,plus)) == true){
                //only + without previous item: + A
                return cal(str.substring(plus+1));
            }
            //A + B form
            else
                return cal(str.substring(0,plus)) + cal(str.substring(plus+1));
        }
        else if(min!=-1){       //minus exist
            if(is_space(str.substring(0,min)) == true)
                //only - without previous item: - A
                return - cal(str.substring(min+1));    
            //A - B form
            else
                return cal(str.substring(0,min)) - cal(str.substring(min+1));    
        }
            
        else if(mul != -1)      //A*B
            return cal(str.substring(0,mul)) * cal(str.substring(mul+1));     
        else if(div != -1)      //A/B
            return cal(str.substring(0,div)) / cal(str.substring(div+1));    
        else if(l!=-1 & l<r){
            if(is_space(str.substring(0,l)))
                return cal(str.substring(l+1,r));
            else{
                return function(str, sin, cos, tan, sqrt);
            }
        }
        else if(sin != -1 | cos != -1 | tan != -1| sqrt != -1)//func exsit
            return function(str,sin,cos,tan,sqrt);
            
        else if(l<r & l!= -1){//   (A...Z)
            if(is_space(str.substring(0,l))){
                return cal(str.substring(l+1, r));
            }
            //check before "(" clean
                
            else
                return Math.sqrt(-1);
        }
        else{
            try{
                return Double.parseDouble(str);
            }catch(NumberFormatException e){
                return Math.sqrt(-1);
            }
        }       // only number
            
    }
    public static double function(String str, int sin, int cos, int tan, int sqrt){
        int[] arr = {sin,cos,tan,sqrt};int m = 1000000000;int j = 0;
        for(int i = 0; i<4; i++){
            //find the smallest sequence and != -1(exist)
            if(arr[i] != -1 & arr[i] < m){
                m = arr[i];
                j = i;
            }
        }

        int l = str.indexOf("(");int r = str.lastIndexOf(")");
        if(l+1>=r)
            return Math.sqrt(-1);
        if(j == 0){
            //sin is the first func
            if(func_is_clean(str, "sin")){
                return Math.sin(cal(str.substring(l+1, r)));
            }    
        }
        else if(j==1){
            //cos is the first func
            if( func_is_clean(str, "cos"))
            return Math.cos(cal(str.substring(l+1, r)));
        }
        else if(j==2){
            //tan is the first func
            if( func_is_clean(str, "tan"))
            return Math.tan(cal(str.substring(l+1, r)));
        }            
        else if(j==3){
            //sqrt is the first func
            if( func_is_clean(str, "sqrt")){
                return Math.sqrt(cal(str.substring(l+1, r)));
            }
        }
        return Math.sqrt(-1);
    }
    public static boolean func_is_clean(String str, String func){
        int left_brak = str.indexOf("(");

        int func_index = str.indexOf(func);
        if(is_space(str.substring(0, func_index))){
            if(func == "sqrt"){
                //if the func is sqrt, check is the space between sqer and () clean
                if(is_space(str.substring(func_index+4,left_brak))){
                    return true;
                }
            }
            //else if the func is sin or cos or tan, check is the space between func and () clean
            else if(is_space(str.substring(func_index+3,left_brak)))
                return true;
            else
                return false;
        }
        else       
            return false;

        return false;
    }

    public static double parse(String str){
        try{
            double result = cal(str);
            return result;
        }catch (Exception e){
            double result = Math.sqrt(-1);// return NaN
            return result;
        }
    }
    public static void main(String[] args) {
        
        String str = "1/sin(0)";
        
        System.out.print("result>>>" + parse(str));
        
    }    

}

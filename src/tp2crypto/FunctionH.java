package tp2crypto;

public class FunctionH {

    private static long h(String s, int n){
        
        if(n <= 0) {
            return 0;
        } else {
            // BUGGED
            return square( 
                    Long.parseLong(s.split("(?<=\\G.{5})")[n-1]) // Bi 
                    + h(s, n-1) 
                    + 3 * n 
                    + 5 
                   ) % 973;
        }
    }
    
    private static long square(long n){
        return n*n;
    }
    
    public static String hash(String m){
        
        // On découpe en blocs de 5 chiffres
        String codeM = Language.codeL0(m);
        String[] blocks = codeM.split("(?<=\\G.{5})");
        return String.valueOf(h(codeM, blocks.length));
    }
}

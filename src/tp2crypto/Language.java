package tp2crypto;

public class Language {
    
    private final static String alphabet = "0123456789"
            + "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            + "abcdefghijklmnopqrstuvwxyz"
            + " $.";
    
    
    public static String codeL0(int c){
        return Integer.toString(alphabet.indexOf(c));
    }

    public static char charL0(int index){
        return alphabet.charAt(index);
    }

    public static String codeL0(String s){
        
        String res = "";
        
        for(int c: s.toCharArray() ){
            res += codeL0(c);
        }
        
        return res;
    }
    
    public static String charL0(String s){
        
        String res = "";
        
        for(int c: s.toCharArray() ){
            res += charL0(c);
        }
        
        return res;
    }
}

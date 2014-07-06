package tp2crypto;

public class SymetricKey {
    
    private final String[] k;
    private final String iv;
    
    public SymetricKey(String[] k, String iv) {
        this.k = k;
        this.iv = iv;
    }
    
    
    public String crypt(String m) {
        String crypted = iv;
        int i = 0;
        for (char c : m.toCharArray()) {
            crypted += lsfr(i, c);
            i++;
        }
        return crypted;
    }
    
    private char lsfr(int i, char c) {
        String ivi = Language.codeL0(iv.charAt(i % 6));
        String ki = k[i % 6];
        int inner = inner(ki, ivi); // K[i] + CodeL0(IV [i]) + 5
        int squareMod = (inner * inner) % 65; // LSF RK,IV [i]
        int codeL0 = Integer.parseInt(Language.codeL0(c)); // (CodeL0(M[i])
        int result = (codeL0 + squareMod) % 65;
        return Language.charL0(result);
    }
    
    private int inner(String s1, String s2) {
        return Integer.parseInt(s1) + Integer.parseInt(s2) + 5;
    }
    
    void print(String s) {
        System.out.println(s);
    }
}

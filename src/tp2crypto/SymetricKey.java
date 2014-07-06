package tp2crypto;

public class SymetricKey {
    
    private String[] k;
    private String iv;
    
    public SymetricKey(String[] k, String iv) {
        this.k = k;
        this.iv = iv;
    }
    
    
    public String lsfr(String m) {
        String crypted = "";
        int i = 0;
        for (char c : m.toCharArray()) {
            String ivi = getIV(i);
            String ki = k[i % 6];
            int inner = inner(ki, ivi);
            int r = (inner * inner) % 65;
            crypted += Language.charL0(r);
            i++;
        }
        return crypted;
    }
    
    private String getIV(int pos) {
        return Language.codeL0(iv.charAt(pos % 6));
    }
    
    private int inner(String s1, String s2) {
        return Integer.parseInt(s1) + Integer.parseInt(s2) + 5;
    }
}

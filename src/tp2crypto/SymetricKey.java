package tp2crypto;

import java.util.ArrayList;
import java.util.List;

public class SymetricKey {
       
    private List<Integer> flux = new ArrayList<>();
    private String iv;
    private final int[] k;
    private int position;
    
    public SymetricKey(int[] k) {
        this.k = k;
    }
    
    private int initLSFR(int i) {
        String ivi = Language.codeL0(iv.charAt(i)); // CodeL0(IV [i])
        int ki = k[i]; // K[i]
        int inner = ki + Integer.parseInt(ivi) + 5; // K[i] + CodeL0(IV [i]) + 5
        return (inner * inner) % 65; // LSFR K,IV [i]
    }
    
    public String crypt(String iv, String m) {
        this.position = 0;
        this.flux = new ArrayList<>();
        this.iv = iv;
        
        String crypted = iv;
        for (char c : m.toCharArray()) {
            crypted += crypt(c);
        }
        return crypted;
    }
    
    public String decrypt(String crypted) {
        if (crypted.length() <= 6) {
            return "";
        }
        this.position = 0;
        this.flux = new ArrayList<>();
        this.iv = crypted.substring(0, 6);
        crypted = crypted.substring(6);
        
        String message = "";
        for (char c : crypted.toCharArray()) {
            message += decrypt(c);
        }
        return message;
    }
    
    private char decrypt(char c) {
        int lsfr = lsfr();
        int codeL0 = Integer.parseInt(Language.codeL0(c)); // (CodeL0(M[i])
        int result = (codeL0 + 65 - lsfr) % 65;
        return Language.charL0(result);
    }
    
    private char crypt(char c) {
        int lsfr = lsfr();
        int codeL0 = Integer.parseInt(Language.codeL0(c)); // (CodeL0(M[i])
        int result = (codeL0 + lsfr) % 65;
        return Language.charL0(result);
    }
    
    private int lsfr() {
        int ret;
        if (position < 6) {
            ret = initLSFR(position);
            flux.add(ret);
            position ++;
        } else {
            ret = (flux.get(0) + flux.get(2) + flux.get(4)) % 65;
            flux.remove(0);
            flux.add(ret);
        }
        return ret;
    }
    
    public static int[] generateKey(String str) {
        int k[] = new int[6];
        k[0] = Integer.parseInt(FunctionH.hash("0" + str)) % 65;
        k[1] = Integer.parseInt(FunctionH.hash("1" + str)) % 65;
        k[2] = Integer.parseInt(FunctionH.hash("2" + str)) % 65;
        k[3] = Integer.parseInt(FunctionH.hash("3" + str)) % 65;
        k[4] = Integer.parseInt(FunctionH.hash("4" + str)) % 65;
        k[5] = Integer.parseInt(FunctionH.hash("5" + str)) % 65;
        return k;
    }     
}

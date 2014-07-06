package tp2crypto;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SymetricKey {
       
    private List<Integer> flux = new ArrayList<>();
    private final String iv;
    private final String[] k;
    private int position;
    
    public SymetricKey(String[] k, String iv) {
        this.iv = iv;
        this.k = k;
    }
    
    private int initLSFR(int i) {
        String ivi = Language.codeL0(iv.charAt(i)); // CodeL0(IV [i])
        String ki = k[i]; // K[i]
        int inner = Integer.parseInt(ki) + Integer.parseInt(ivi) + 5; // K[i] + CodeL0(IV [i]) + 5
        return (inner * inner) % 65; // LSFR K,IV [i]
    }
    
    public String crypt(String m) {
        String crypted = iv;
        int i = 0;
        for (char c : m.toCharArray()) {
            crypted += crypt(c);
            i++;
        }
        return crypted;
    }
    
    private char crypt(char c) {
        int lsfr = lsfr();
        int codeL0 = Integer.parseInt(Language.codeL0(c)); // (CodeL0(M[i])
        int result = (codeL0 + lsfr) % 65;
        return Language.charL0(result);
    }
    
    private int lsfr() {
        int ret = 0;
        if (position < 6) {
            ret = initLSFR(position);
            flux.add(ret);
            position ++;
        } else {
            ret = flux.get(0) + flux.get(2) + flux.get(4);
            flux.remove(0);
            flux.add(ret);
        }
        return ret;
    }
}

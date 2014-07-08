package tp2crypto;

import java.math.BigInteger;

public final class Crypto {
    
    private Crypto(){}
    
    public static String rsa(String m, Key key) {
        return rsa(m, key.getE(), key.getN());
    }
    
    public static String rsa(String m, int e, int n) {
        
        BigInteger bigE = new BigInteger(Integer.toString(e));
        BigInteger bigN = new BigInteger(Integer.toString(n));
        BigInteger bigM = new BigInteger(m);
        
        return bigM.modPow(bigE, bigN).toString();
    }        
}

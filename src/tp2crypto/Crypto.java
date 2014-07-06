/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp2crypto;

import java.math.BigInteger;

public class Crypto {
    
    public String genRand(int min, int max){
        return Generator.genRand(min, max);
    }
    
    public String crypt(String m, int e, int n) {
        String mL0 = Language.codeL0(m);
        return rsa(mL0, e, n);
    }
    
    public static String rsa(String m, Key key) {
        return rsa(m, key.getE(), key.getN());
    }
    
    public static String rsa(String m, int e, int n) {
        
        BigInteger bigE = new BigInteger(Integer.toString(e));
        BigInteger bigN = new BigInteger(Integer.toString(n));
        BigInteger bigM = new BigInteger(m);
        
        return bigM.modPow(bigE, bigN).toString();
    }
    
    public String hash(String m){
        return FunctionH.hash(m);
    }
        
}

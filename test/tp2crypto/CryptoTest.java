/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author carlbelanger
 */
public class CryptoTest {
    
    private final Crypto instance = new Crypto();
    
    @Test
    public void testEncrypt() {
        String expResult = "2302";
        String result = instance.crypt("1716", 23, 3811);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testVerisign() {
        String expResult = "324";
        String result = instance.rsa("6069", 31,7979);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testEncrypt2() {
        String expResult = "774";
        String result = instance.crypt("3432", 31, 7979);
        assertEquals(expResult, result);
    }

    @Test
    public void testDecrypt() {
        String expResult = "1716";
        String result = instance.crypt("2302", 479, 3811);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRsaEncrypt() {
        String expResult = "2790";
        String result = instance.crypt("65", 17, 3233);
        assertEquals(expResult, result);
    }
    
    @Test
    public void testRsaDecrypt() {
        String expResult = "65";
        String result = instance.crypt("2790", 2753, 3233);
        assertEquals(expResult, result);
    }

    @Test
    public void testHash() {
        long expResult = 44L;
        long result = instance.hash("0123456ABC");
        assertEquals(expResult, result);
    }
    
    @Test
    public void testHash2() {
        long expResult = 774L;
        long result = instance.hash("VERISIGN VERISIGN 2030 01 01 31 7979");
        assertEquals(expResult, result);
    }
    @Test
    public void testHash3() {
        long expResult = 324L;
        long result = instance.hash("www.desjardins.com VERISIGN 2025 01 01 23 3811");
        assertEquals(expResult, result);
    }
}

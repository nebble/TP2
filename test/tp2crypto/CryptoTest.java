package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

public class CryptoTest {
    
    @Test
    public void testVerisign() {
        String expResult = "324";
        String result = Crypto.rsa("6069", 31,7979);
        assertEquals(expResult, result);
    }

    @Test
    public void testHash() {
        String expResult = "44";
        String result = FunctionH.hash("0123456ABC");
        assertEquals(expResult, result);
    }
    
    @Test
    public void testHash2() {
        String expResult = "774";
        String result = FunctionH.hash("VERISIGN VERISIGN 2030 01 01 31 7979");
        assertEquals(expResult, result);
    }
    @Test
    public void testHash3() {
        String expResult = "324";
        String result = FunctionH.hash("www.desjardins.com VERISIGN 2025 01 01 23 3811");
        assertEquals(expResult, result);
    }
}

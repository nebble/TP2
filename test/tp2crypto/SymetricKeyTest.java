package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

public class SymetricKeyTest {

    @Test
    public void testLsfr() {
        int[] key = {1,2,3,4,5,6};
        
        SymetricKey k = new SymetricKey(key);

        String expResult = "b4INtbdrZlFm8PV";
        String result = k.crypt("b4INtb", "A 9$FJILF");
        assertEquals(expResult, result);
    }    
}

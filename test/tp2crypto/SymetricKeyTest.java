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
public class SymetricKeyTest {

    @Test
    public void testLsfr() {
        String[] key = {"1", "2", "3", "4", "5", "6"};
        
        SymetricKey k = new SymetricKey(key, "b4INtb");

        String expResult = "b4INtbdrZlFm8PV";
        String result = k.crypt("A 9$FJILF");
        assertEquals(expResult, result);
    }    
}

package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

public class Example {
    
    Client client = new Client();
    Server server = new Server();
    
    @Test
    public void Step0() {
        String expResult = "30161";
        String result = client.receive(null);
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step1() {
        String expResult = "12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
        String result = server.receive("30161");
        assertEquals(expResult, result);
        
        client.receive(expResult);
    }

    @Test
    public void Step2() {
        client.setStatus("connected");
        
        String message = "12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
        
        String expResult = "1234";
        String result = client.receive(message);
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step3() {
        server.setStatus("connected");
        
        String expResult = "b4INtb7X6";
        String result = server.receive("2302");
        assertEquals(expResult, result);
    }
}

package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

public class Example {
    
    Client client = new Client();
    Server server = new Server();
    
    @Test
    public void Step0() {
        client.inject(new FakeGenerator("30161"));
        
        String expResult = "30161";
        String result = client.receive(null);
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step1() {
        server.inject(new FakeGenerator("12150"));
        
        String expResult = "12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
        String result = server.receive("30161");
        assertEquals(expResult, result);
        
        client.receive(expResult);
    }

    @Test
    public void Step2() {
        client.setStatus("connected");
        client.inject(new FakeGenerator("1716"));
        
        String message = "12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
        
        String expResult = "2302";
        String result = client.receive(message);
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step3() {
        server.setStatus("connected");
        server.setM1("30161");
        server.setM2("12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069");
        server.setM3("2302");
        server.setNc("30161");
        server.setNs("12150");
        server.inject(new FakeGenerator("b4INtb"));
        
        String expResult = "b4INtb7X6";
        String result = server.receive("2302");
        assertEquals(expResult, result);
    }
}

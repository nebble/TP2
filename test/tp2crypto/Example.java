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
        server.setNc("30161");
        server.setNs("12150");
        server.inject(new FakeGenerator("b4INtb"));
        
        String expResult = "b4INtb7X6";
        String result = server.receive("2302");
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step4() {
        
        client.setStatus("negotiating");
        client.setNc("30161");
        client.setNs("12150");
        client.setK0("1716");
        client.setM2("12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069");
        client.setM3("2302");
        client.inject(new FakeGenerator("x3I9AA"));
        
        String expResult = "x3I9AAqH4";
        String result = client.receive("b4INtb7X6");
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step5() {
        server.setStatus("trusted");
        server.setNc("30161");
        server.setNs("12150");
        server.setM3("2302");
        server.setM4("b4INtb7X6");
        server.setK0("1716");
        server.inject(new FakeGenerator("171", "171", "bqlDHJ"));
        
        String expResult = "bqlDHJEorWISWxY3vjfWD$uiYt0UL jrwN6ZhI0";
        String result = server.receive("x3I9AAqH4");
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step6() {
        client.setStatus("authenticating");
        client.setNumCompte("80123");
        client.setPassword("BN12Z");
        client.setNc("30161");
        client.setNs("12150");
        client.setK0("1716");
        client.inject(new FakeGenerator("ovI4.H"));
        
        String expResult = "ovI4.HMp23D$aBbvff3Tj";
        String result = client.receive("bglDHJEKrWISWTYcvofgDmuiYo0ALbj3ww6yhS0");
        assertEquals(expResult, result);
    }
    
    @Test
    public void Step7() {
        server.setStatus("authenticating");
        server.setNc("30161");
        server.setNs("12150");
        server.setK0("1716");
        server.setNS1("171");
        server.setNS2("20731");
        server.inject(new FakeGenerator("PcO$65"));
        
        String expResult = "PcO$65pVNH2vdn9ooDhR7XGtiR7PXAVOmu2GU6aaMlGnNxQ";
        String result = server.receive("ovI4.HMp23D$aBbvff3Tj");
        assertEquals(expResult, result);
    }
}

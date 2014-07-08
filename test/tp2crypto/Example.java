package tp2crypto;

import org.junit.Test;
import static org.junit.Assert.*;

public class Example {
    private static final String MESSAGE_1 = "30161";
    private static final String MESSAGE_2 = "12150 www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private static final String MESSAGE_3 = "2302";
    private static final String MESSAGE_4 = "b4INtb7X6";
    private static final String MESSAGE_5 = "x3I9AAqH4";
    private static final String MESSAGE_6 = "bglDHJEKrWISWTYcvofgDmuiYo0ALbj3ww6yhS0";
    private static final String MESSAGE_7 = "ovI4.HMp23D$aBbvff3Tj";
    private static final String MESSAGE_8 = "PcO$65pVNH2vdn9ooDhR7XGtiR7PXAVOmu2GU6aaMlGnNxQ";
    private static final String MESSAGE_9 = "lu7btIjHoMOF1Gqo9hINgn7jzjlCqhOWnNv";
    private static final String MESSAGE_10 = "sTEkbgNNOeMJ8DwKpVg";
    private static final String MESSAGE_11 = "lu7btIgKwSPEEpOo7hM";
    
    Client client = new Client();
    Server server = new Server();
    
    @Test
    public void Step0() {
        client.inject(new FakeGenerator(MESSAGE_1));
        
        String result = client.receive(null);
        assertEquals(MESSAGE_1, result);
    }
    
    @Test
    public void Step1() {
        server.inject(new FakeGenerator("12150"));
        
        String result = server.receive(MESSAGE_1);
        assertEquals(MESSAGE_2, result);
    }

    @Test
    public void Step2() {
        client.setStatus("connected");
        client.inject(new FakeGenerator("1716"));
        
        String result = client.receive(MESSAGE_2);
        assertEquals(MESSAGE_3, result);
    }
    
    @Test
    public void Step3() {
        server.setStatus("connected");
        server.setNc(MESSAGE_1);
        server.setNs("12150");
        server.inject(new FakeGenerator("b4INtb"));
        
        String result = server.receive(MESSAGE_3);
        assertEquals(MESSAGE_4, result);
    }
    
    @Test
    public void Step4() {
        
        client.setStatus("negotiating");
        initKc();
        client.setM2(MESSAGE_2);
        client.setM3(MESSAGE_3);
        client.inject(new FakeGenerator("x3I9AA"));
        
        String result = client.receive(MESSAGE_4);
        assertEquals(MESSAGE_5, result);
    }
    
    @Test
    public void Step5() {
        server.setStatus("trusted");
        initKs();
        server.setM3(MESSAGE_3);
        server.setM4(MESSAGE_4);
        server.inject(new FakeGenerator("171", "171", "bglDHJ"));
        
        String result = server.receive(MESSAGE_5);
        assertEquals(MESSAGE_6, result);
    }
    
    @Test
    public void Step6() {
        client.setStatus("authenticating");
        client.setNumCompte("80123");
        client.setPassword("BN12Z");
        initKc();
        client.inject(new FakeGenerator("ovI4.H"));
        
        String result = client.receive(MESSAGE_6);
        assertEquals(MESSAGE_7, result);
    }
    
    @Test
    public void Step7() {
        server.setStatus("authenticating");
        initKs();
        server.setNS1("171");
        server.inject(new FakeGenerator("20731", "20731", "PcO$65"));
        
        String result = server.receive(MESSAGE_7);
        assertEquals(MESSAGE_8, result);
    }

    @Test
    public void Step8() {
        client.setStatus("logged");
        client.setNumCompte("80123");
        client.setPassword("BN12Z");
        initKc();
        client.inject(new FakeGenerator("116", "116", "lu7btI"));
        
        String result = client.receive(MESSAGE_8);
        assertEquals(MESSAGE_9, result);
    }

    @Test
    public void Step9() {
        server.setStatus("clientlogged");
        initKs();
        server.setNS1("171");
        server.setNS2("20731");
        server.inject(new FakeGenerator("sTEkbg"));
        
        String result = server.receive(MESSAGE_9);
        assertEquals(MESSAGE_10, result);
    }
    
    @Test
    public void Step10() {
        client.setStatus("logged");
        client.setNumCompte("80123");
        client.setPassword("BN12Z");
        initKc();
        client.inject(new FakeGenerator("116", "116", "lu7btI"));
        
        String expResult = MESSAGE_11;
        String result = client.receive(MESSAGE_10);
        assertEquals(expResult, result);
    }
    
    private void initKc() {
        client.setNc(MESSAGE_1);
        client.setNs("12150");
        client.setK0("1716");
    }
    
    private void initKs() {
        server.setNc(MESSAGE_1);
        server.setNs("12150");
        server.setK0("1716");
    }
    
    
    @Test
    public void allSteps() {
        client.inject(new FakeGenerator(MESSAGE_1));
        
        String result = client.receive(null);
        assertEquals(MESSAGE_1, result);
        
        server.inject(new FakeGenerator("12150"));
        result = server.receive(MESSAGE_1);
        assertEquals(MESSAGE_2, result);


        client.inject(new FakeGenerator("1716"));
        String message = MESSAGE_2;
        result = client.receive(message);
        assertEquals(MESSAGE_3, result);

        server.inject(new FakeGenerator("b4INtb"));
        result = server.receive(MESSAGE_3);
        assertEquals(MESSAGE_4, result);

        client.inject(new FakeGenerator("x3I9AA"));
        result = client.receive(MESSAGE_4);
        assertEquals(MESSAGE_5, result);

        server.inject(new FakeGenerator("171", "171", "bglDHJ"));
        result = server.receive(MESSAGE_5);
        assertEquals(MESSAGE_6, result);

        client.inject(new FakeGenerator("ovI4.H"));
        client.setNumCompte("80123");
        client.setPassword("BN12Z");
        result = client.receive("bqlDHJEorWISWxY3vjfWD$uiYt0UL jrwN6ZhI0");
        assertEquals(MESSAGE_7, result);

        server.inject(new FakeGenerator("20731", "20731", "PcO$65"));
        result = server.receive(MESSAGE_7);
        assertEquals(MESSAGE_8, result);

        client.inject(new FakeGenerator("116", "116", "lu7btI"));
        result = client.receive(MESSAGE_8);
        assertEquals(MESSAGE_9, result);

        server.inject(new FakeGenerator("sTEkbg"));
        result = server.receive(MESSAGE_9);
        assertEquals(MESSAGE_10, result);

        client.inject(new FakeGenerator("116", "116", "lu7btI"));
        result = client.receive(MESSAGE_10);
        assertEquals(MESSAGE_11, result);
    }
    
}

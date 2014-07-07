package tp2crypto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Client {
    private String status = "closed";
    
    private String nc;
    private String ns;
    private String k0;
    
    private Key serverKey;
    private final Key keyCA = new Key(31,7979);
    
    Generator generator = new Generator();
    private String m2;
    private String m3;

    private String initNC0() {
        this.nc = generator.genRandomN();
        status = "connected";
        return nc;
    }

    public String getNC() {
        return this.nc;
    }
    
    public void setNc(String nc) {
        this.nc = nc;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }
    
    public void setK0(String k0) {
        this.k0 = k0;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public void setM3(String m3) {
        this.m3 = m3;
    }

    public String receive(String message) {
        switch (status) {
            case "closed":
                return initNC0();
            case "connected":
                return validateCert(message);
            case "negotiating":
                return process(message);
            case "thrusted":
                return command(message);
        }
        
        return "";
    }

    private String validateCert(String message) {
        this.m2 = message;
        String[] split = message.split(" ");
        this.ns = split[0];
        String website = split[1];
        String auth = split[2];
        String year = split[3];
        String month = split[4];
        String day = split[5];
        String e = split[6];
        String n = split[7];
        String crypt = split[8];
        
        if (!"www.desjardins.com".equals(website)) {
            throw new RuntimeException();
        }
        if (!"VERISIGN".equals(auth)) {
            throw new RuntimeException();
        }
        try {
            Date date = new SimpleDateFormat("yyyymmdd").parse(year + month + day);
            if (date.before(Calendar.getInstance().getTime())) {
                throw new RuntimeException();
            }
        } catch (ParseException ex) {
            throw new RuntimeException();
        }
        
        this.serverKey = new Key(Integer.parseInt(e),Integer.parseInt(n));
        
        String result = Crypto.rsa(crypt, keyCA);
        String hash = FunctionH.hash(website + " " + auth + " " + year + " " + month + " " + day + " " + e + " " + n);
        if (!result.equals(hash)) {
            throw new RuntimeException();
        }
        
        this.k0 = generator.genRandomK();
        
        this.status = "negotiating";
        
        String rsa = Crypto.rsa(k0, serverKey);
        this.m3 = rsa;
        return rsa;
    }

    void setStatus(String status) {
        this.status = status;
    }

    void inject(Generator fakeGenerator) {
        this.generator = fakeGenerator;
    }

    private String process(String message) {
        String iv = message.substring(0, 6);
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        String value = (new SymetricKey(k, iv)).decrypt(message.substring(6));
        
        String m = nc + m2 + m3;
        String h = FunctionH.hash(m);
        
        if (!value.equals(h)) {
            throw new RuntimeException();
        }
        
        m += message;
        h = FunctionH.hash(m);
        
        this.status = "thrusted";
        return (new SymetricKey(k, generator.genRandomIV())).crypt(h);
    }
        
    void print(String s) {
        System.out.println(s);
    }

    private String command(String message) {
        return "";
    }
}

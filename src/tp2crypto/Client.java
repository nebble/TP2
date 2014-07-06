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
    private Key keyCA = new Key(31,7979);
    
    Generator generator = new Generator();

    private String initNC0() {
        this.nc = generator.genRandomN();
        status = "connected";
        return nc;
    }


    public String getNC() {
        return this.nc;
    }
    
    public void setNC(String nc) {
        this.nc = nc;
    }

    public String receive(String message) {
        switch (status) {
            case "closed":
                return initNC0();
            case "connected":
                return validateCert(message);
            case "thrusted":
        }
        
        return "";
    }

    private String validateCert(String message) {
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
        
        this.status = "thrusted";
        
        return Crypto.rsa(k0, serverKey);
    }

    void setStatus(String status) {
        this.status = status;
    }
    
    void print(String s) {
        System.out.println(s);
    }

    void inject(Generator fakeGenerator) {
        this.generator = fakeGenerator;
    }
}

package tp2crypto;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;
    private String ns1;
    private String m3;
    private String m4;
    
    Generator generator = new Generator();
    private String k0;

    public String receive(String value) {
        switch (status) {
            case "waiting":
                return init(value);
            case "connected":
                return accept(value);
            case "thrusted": 
                return process(value);
        }
        
        return "";
    }

    private String init(String value) {
        status = "connected";
        this.nc = value;
        this.ns = generator.genRandomN();
        return ns + " " + cert;
    }

    void setStatus(String status) {
        this.status = status;
    }

    private String accept(String value) {
        this.k0 = Crypto.rsa(value, PRIVATE_KEY);
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        
        String iv = generator.genRandomIV();
        String m = nc + (ns + " " + cert) + value; // m1 + m2 + m3
        String h = FunctionH.hash(m);
        
        status = "thrusted";
        String crypted = (new SymetricKey(k, iv)).crypt(h);
        this.m3 = value;
        this.m4 = crypted;
        return crypted;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }
    
    void inject(Generator fakeGenerator) {
        this.generator = fakeGenerator;
    }

    private String process(String message) {
        String iv = message.substring(0, 6);
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        String value = (new SymetricKey(k, iv)).decrypt(message.substring(6));
        
        String m = nc + (ns + " " + cert) + m3 + m4;
        String h = FunctionH.hash(m);
        
        if (!value.equals(h)) {
            throw new RuntimeException();
        }
        
        this.ns1 = generator.genRandomN();
        String ret = "DONNER NUMERO ET MOT DE PASSE " + ns1;
        
        // DEBUG
        print(ret);print(generator.genRandomIV());print(nc + k0 + ns);
        
        return (new SymetricKey(k, generator.genRandomIV())).crypt(ret);
    }
    
    void print(String s) {
        System.out.println(s);
    }
    
    void setM3(String m3) {
        this.m3 = m3;
    }

    void setM4(String m4) {
        this.m4 = m4;
    }

    public void setK0(String k0) {
        this.k0 = k0;
    }
}

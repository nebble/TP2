package tp2crypto;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;
    
    Generator generator = new Generator();

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
        String k0 = Crypto.rsa(value, PRIVATE_KEY);
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        
        String iv = generator.genRandomIV();
        String m = nc + (ns + " " + cert) + value; // m1 + m2 + m3
        String h = FunctionH.hash(m);
        
        status = "thrusted";
        return (new SymetricKey(k, iv)).crypt(h);
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

    private String process(String value) {
        return "TO IMPLEMENT";
    }
    
    void print(String s) {
        System.out.println(s);
    }
}

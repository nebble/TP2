package tp2crypto;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;
    
    private String m1;
    private String m2;
    private String m3;
    
    Generator generator = new Generator();

    public String receive(String value) {
        switch (status) {
            case "waiting":
                return init(value);
            case "connected":
                return accept(value);
        }
        
        return "";
    }

    private String init(String value) {
        this.m1 = value;
        status = "connected";
        this.nc = value;
        this.ns = generator.genRandomN();
        this.m2 = String.valueOf(ns) + " " + cert;
        return this.m2;
    }

    void setStatus(String status) {
        this.status = status;
    }

    private String accept(String value) {
        this.m3 = value;
        
        String k0 = Crypto.rsa(value, PRIVATE_KEY);
        
        String k[] = new String[6];
        k[0] = String.valueOf(Integer.parseInt(FunctionH.hash("0" + nc + k0 + ns)) % 65);
        k[1] = String.valueOf(Integer.parseInt(FunctionH.hash("1" + nc + k0 + ns)) % 65);
        k[2] = String.valueOf(Integer.parseInt(FunctionH.hash("2" + nc + k0 + ns)) % 65);
        k[3] = String.valueOf(Integer.parseInt(FunctionH.hash("3" + nc + k0 + ns)) % 65);
        k[4] = String.valueOf(Integer.parseInt(FunctionH.hash("4" + nc + k0 + ns)) % 65);
        k[5] = String.valueOf(Integer.parseInt(FunctionH.hash("5" + nc + k0 + ns)) % 65);
        
        String iv = generator.genRandomIV();
        String m = m1 + m2 + m3;
        print(m);
        String h = FunctionH.hash(m);
        
        return iv + (new SymetricKey(k, iv)).lsfr(h);
    }

    public void setM1(String m1) {
        this.m1 = m1;
    }

    public void setM2(String m2) {
        this.m2 = m2;
    }

    public void setM3(String m3) {
        this.m3 = m3;
    }
    
    void inject(Generator fakeGenerator) {
        this.generator = fakeGenerator;
    }
    
    
    void print(String s) {
        System.out.println(s);
    }
}

package tp2crypto;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;
    private String ns1;
    private String ns2;
    private String m3;
    private String m4;
    
    private String numCompte = "80123";
    private String password = "BN12Z";
    
    Generator generator = new Generator();
    private String k0;

    public String receive(String value) {
        switch (status) {
            case "waiting":
                return init(value);
            case "connected":
                return accept(value);
            case "trusted": 
                return process(value);
            case "authenticating":
                return verify(value);
            case "clientlogged":
                return operation(value);
        }
        
        return "";
    }
    
    private SymetricKey getSymKey() {
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        return new SymetricKey(k);
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

        String m = nc + (ns + " " + cert) + value; // m1 + m2 + m3
        String h = FunctionH.hash(m);
        
        status = "trusted";
        String crypted = getSymKey().crypt(generator.genRandomIV(), h);
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
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String m = nc + (ns + " " + cert) + m3 + m4;
        String h = FunctionH.hash(m);
        
        if (!value.equals(h)) {
            throw new RuntimeException();
        }
        
        this.ns1 = generator.genRandomN();
        String ret = "DONNER NUMERO ET MOT DE PASSE " + ns1;
        
        this.status = "authenticating";
        return symKey.crypt(generator.genRandomIV(), ret);
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
    
    public void setNS2(String ns2){
        this.ns2 = ns2;
    }
    
    public void setNS1(String ns1) {
        this.ns1 = ns1;
    }
    
    public String verify(String message){
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] split = value.split(" ");
        
        if(!split[0].equals(this.numCompte)) {
            return "ERROR COMPTE";
        }
        
        if(!split[1].equals(this.password)){
            return "ERROR PASS";
        }
        
        if(!split[2].equals(this.ns1)) {
            return "ERROR NS1";
        }
        
        this.ns2 = generator.genRandomN();
        String ret = "CHOISIR OPERATION TRANSFERT QUITTER " + ns2;
        
        this.status = "clientlogged";
        return symKey.crypt(generator.genRandomIV(), ret);
    }
    
    public String operation(String message) {
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] values = value.split(" ");
        
        if (!this.ns2.equals(values[3])) {
            throw new RuntimeException();
        }

        String nc1 = values[4];
        
        if ("TRANSFERT".equals(values[0])) {
            System.out.println(values[2] + " has been draw from " + values[1]);
        } else if ("QUITTER".equals(values[0])) {
            this.status = "waiting";
        }
        
        this.ns2 = generator.genRandomN();
        
        String response = "REPONSE " + ns2 + " " + nc1;
        
        return symKey.crypt(generator.genRandomIV(), response);
    }
}

package tp2crypto;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String nc1;
    private String ns;
    private String k0;
    private String ns1;
    private String ns2;
    private String m3;
    private String m4;
    private String operation;
    private String destination;
    private String montant;
    
    Generator generator = new Generator();
    
    void setStatus(String status) {
        this.status = status;
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

    public boolean reveive(String value) {
        switch (status) {
            case "waiting":
                return validateNc(value);
            case "connected":
                return validateConnect(value);
            case "trusted": 
                return validateTrusted(value);
            case "authenticating":
                return validateAuth(value);
            case "clientlogged":
                return validateOperation(value);
        }
        return false;
    }
    
    public String sendBack() {
        switch (status) {
            case "waiting":
                return responseToClient();
            case "connected":
                return responseConnected();
            case "trusted": 
                return responseTrusted();
            case "authenticating":
                return responseAuth();
            case "clientlogged":
                return responseOperation();
        }
        
        return "ERROR";
    }
    
    public String reveiveAndSendBack(String value) {
        if (reveive(value)) {
            return sendBack();
        }
        return "ERROR";
    }
    
    private SymetricKey getSymKey() {
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        return new SymetricKey(k);
    }
    
    private boolean validateNc(String message) {
        this.nc = message;
        return true;
    }

    private String responseToClient() {
        status = "connected";
        this.ns = generator.genRandomN();
        return ns + " " + cert;
    }

    private boolean validateConnect(String value) {
        this.k0 = Crypto.rsa(value, PRIVATE_KEY);
        this.m3 = value;
        return true;
    }

    private String responseConnected() {
        String m = nc + (ns + " " + cert) + m3; // m1 + m2 + m3
        String h = FunctionH.hash(m);
        
        status = "trusted";
        this.m4 = getSymKey().crypt(generator.genRandomIV(), h);
        return m4;
    }

    private boolean validateTrusted(String message) {
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String m = nc + (ns + " " + cert) + m3 + m4;
        String h = FunctionH.hash(m);
        
        return value.equals(h);
    }
    
    private String responseTrusted() {
        this.ns1 = generator.genRandomN();
        String ret = "DONNER NUMERO ET MOT DE PASSE " + ns1;
        
        this.status = "authenticating";
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), ret);
    }

    private boolean validateAuth(String message){
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] split = value.split(" ");
        
        if (!Database.validateCredential(split[0], split[1])) {
            return false;
        }
        
        return split[2].equals(this.ns1);
    }
    
    private String responseAuth() {
        this.ns2 = generator.genRandomN();
        String ret = "CHOISIR OPERATION TRANSFERT QUITTER " + ns2;
        
        this.status = "clientlogged";
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), ret);
    }
    
    private boolean validateOperation(String message) {
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] values = value.split(" ");
        
        if (!this.ns2.equals(values[3])) {
            return false;
        }

        this.nc1 = values[4];
        this.operation = values[0];
        if ("TRANSFERT".equals(operation)) {
            this.destination = values[1];
            this.montant = values[2];
            return true;
        }
        return "QUITTER".equals(operation);
    }
        
    private String responseOperation() {
        boolean success = false;
        switch (operation) {
            case "TRANSFERT":
                success = Database.doTransfert(destination, montant);
                break;
            case "QUITTER":
                this.status = "waiting";
                success = true;
                break;
        }
        
        String response = "REPONSE " + (success ? "1" : "0") + " " + nc1;
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), response);
    }
}

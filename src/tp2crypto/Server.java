package tp2crypto;

import static tp2crypto.Status.*;

public class Server {
    private static final Key PRIVATE_KEY = new Key(479, 3811);
    private Status status = WaitingForConnection;
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    Generator generator = new Generator();
    
    private String nc;
    private String nc1;
    private String ns;
    private String ns1;
    private String ns2;
    private String k0;
    
    private String m3;
    private String m4;
    
    private String operation;
    private String username;
    private String destination;
    private int montant;
    
    void setStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return status;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean reveive(String value) {
        switch (status) {
            case WaitingForConnection:
                return validateNc(value);
            case ClientConnected:
                return validateConnect(value);
            case TrustEstablished: 
                return validateTrusted(value);
            case Authenticate:
                return validateAuth(value);
            case ClientLogged:
                return validateOperation(value);
        }
        return false;
    }
    
    public String sendBack() {
        switch (status) {
            case WaitingForConnection:
                return responseToClient();
            case ClientConnected:
                return responseConnected();
            case TrustEstablished: 
                return responseTrusted();
            case Authenticate:
                return responseAuth();
            case ClientLogged:
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
    
    /* Server validation for step 1 */
    private boolean validateNc(String message) {        
        this.nc = message;
        return isValidN(message);
    }

    /* Server operation for step 2 */
    private String responseToClient() {
        status = ClientConnected;
        this.ns = generator.genRandomN();
        return ns + " " + cert;
    }

    /* Server validation for step 3 */
    private boolean validateConnect(String value) {
        this.k0 = Crypto.rsa(value, PRIVATE_KEY);
        
        if (!isValidK(k0)) {
            return false;
        }
        
        this.m3 = value;
        return true;
    }

    /* Server operation for step 4 */
    private String responseConnected() {
        String m = nc + (ns + " " + cert) + m3; // m1 + m2 + m3
        String h = FunctionH.hash(m);
        
        status = TrustEstablished;
        this.m4 = getSymKey().crypt(generator.genRandomIV(), h);
        return m4;
    }

    /* Server validation for step 5 */
    private boolean validateTrusted(String message) {
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String m = nc + (ns + " " + cert) + m3 + m4;
        String h = FunctionH.hash(m);
        
        return value.equals(h);
    }
    
    /* Server operation for step 6 */
    private String responseTrusted() {
        this.ns1 = generator.genRandomN();
        String ret = "DONNER NUMERO ET MOT DE PASSE " + ns1;
        
        this.status = Authenticate;
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), ret);
    }

    /* Server validation for step 7 */
    private boolean validateAuth(String message){
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] split = value.split(" ");
        if (split.length < 3 || !ns1.equals(split[2])) {
            return false;
        }
        
        this.username = split[0];
        return Database.validateCredential(username, split[1]);
    }
    
    /* Server operation for step 8 */
    private String responseAuth() {
        this.ns2 = generator.genRandomN();
        String ret = "CHOISIR OPERATION TRANSFERT QUITTER " + ns2;
        
        this.status = ClientLogged;
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), ret);
    }
    
    /* Server validation for step 9 */
    private boolean validateOperation(String message) {
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] values = value.split(" ");
        if (values.length < 3) {
            return false;
        }
        this.nc1 = values[values.length - 1];
        String clientNs2 = values[values.length - 2];
        
        if (!ns2.equals(clientNs2) || !isValidN(nc1)) {
            return false;
        }

        this.operation = values[0];
        if ("TRANSFERT".equals(operation)) {
            this.destination = values[1];
            if (!isMontantValid(values[2])) {
                return false;
            }
            this.montant = getAsInt(values[2]);
            return true;
        }
        return "QUITTER".equals(operation);
    }
    
    private boolean isMontantValid(String value) {
        return value.length() == 4 && isNumeric(value.charAt(0)) && isNumeric(value.charAt(1)) && 
                isNumeric(value.charAt(2)) && value.charAt(3) == '$';
    }
    
    private boolean isNumeric(char c) {
        return c >= '0' && c <= '9';
    }
    
    private int getAsInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }
        
    private String responseOperation() {
        boolean success = false;
        switch (operation) {
            case "TRANSFERT":
                success = Database.doTransfert(username, destination, montant);
                break;
            case "QUITTER":
                this.status = WaitingForConnection;
                success = true;
                break;
        }
        
        String response = "REPONSE " + (success ? "1" : "0") + " " + nc1;
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), response);
    }
    
    private boolean isValidN(String value) {
        return isBetween(value, 0, 99999);
    }
    
    private boolean isValidK(String value) {
        return isBetween(value, 2, 3810);
    }
    
    private boolean isBetween(String value, int min, int max) {
        try {
            int intValue = Integer.parseInt(value);
            if (intValue < min || intValue > max) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
}

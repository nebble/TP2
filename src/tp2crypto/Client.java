package tp2crypto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import static tp2crypto.Status.*;

public class Client {
    private Status status = ConnectionClosed;
    
    private String nc;
    private String nc1;
    private String ns;
    private String ns1;
    private String ns2;
    private String k0;
    
    private Key publicServerKey;
    private final Key publicCertKey = new Key(31,7979);
    
    Generator generator = new Generator();
    private String m2;
    private String m3;
    private String m4;
    
    private String numCpt;
    private String passwd;
    private String operation = "TRANSFERT";
    private String destination = "3124";
    private String montant = "050$";
    
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
    
    public void setNumCompte(String num){
        this.numCpt = num;
    }
    
    public void setPassword(String password){
        this.passwd = password;
    }
    
    void setStatus(Status status) {
        this.status = status;
    }
    
    public Status getStatus() {
        return this.status;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

    public void setNc1(String nc1) {
        this.nc1 = nc1;
    }

    void inject(Generator fakeGenerator) {
        this.generator = fakeGenerator;
    }

    public String reveiveAndSendBack(String message) {
        if (reveive(message)) {
            return sendBack();
        }
        return "ERROR";
    }
    
    public boolean reveive(String message) {
        switch (status) {
            case ConnectionClosed:
                return true;
            case ServerConnection:
                return validateCert(message);
            case Negociating:
                return validateNego(message);
            case AtemptingLogging:
                return validateAuth(message);
            case Logged:
                return validateOperation(message);
        }
        
        return false;
    }
    
    public String sendBack() {
        switch (status) {
            case ConnectionClosed:
                return initNC0();
            case ServerConnection:
                return reponseCert();
            case Negociating:
                return responseNego();
            case AtemptingLogging:
                return responseAuth();
            case Logged:
                return responseOperation();
        }
        
        return "ERROR";
    }
    
    private SymetricKey getSymKey() {
        int[] k = SymetricKey.generateKey(nc + k0 + ns);
        return new SymetricKey(k);
    }
    
    /* Client operation for step 1 */
    private String initNC0() {
        this.nc = generator.genRandomN();
        status = ServerConnection;
        return nc;
    }
    
    /* Client validation for step 2 */
    private boolean validateCert(String message) {
        this.m2 = message;
        String[] split = message.split(" ");
        if (split.length != 9) {
            return false;
        }
        this.ns = split[0];
        String website = split[1];
        String auth = split[2];
        String year = split[3];
        String month = split[4];
        String day = split[5];
        String e = split[6];
        String n = split[7];
        String crypt = split[8];
        
        // Validation of the server certificate
        if (!"www.desjardins.com".equals(website)) {
            return false;
        }
        if (!"VERISIGN".equals(auth)) {
            return false;
        }
        try {
            Date date = new SimpleDateFormat("yyyymmdd").parse(year + month + day);
            if (date.before(Calendar.getInstance().getTime())) {
                return false;
            }
        } catch (ParseException ex) {
            return false;
        }
        
        if (!(isNumeric(e) && isNumeric(n))) {
            return false;
        }
        
        this.publicServerKey = new Key(Integer.parseInt(e),Integer.parseInt(n));
        
        // Validation que la signature est valide
        String result = Crypto.rsa(crypt, publicCertKey);
        String hash = FunctionH.hash(website + " " + auth + " " + year + " " + month + " " + day + " " + e + " " + n);
        return result.equals(hash);
    }

    /* Client operation for step 3 */
    private String reponseCert() {
        this.k0 = generator.genRandomK();
        
        this.status = Negociating;
        
        String rsa = Crypto.rsa(k0, publicServerKey);
        this.m3 = rsa;
        return rsa;
    }

    /* Client validation for step 4 */
    private boolean validateNego(String message) {
        this.m4 = message;
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String m = nc + m2 + m3;
        String h = FunctionH.hash(m);
        
        return value.equals(h);
    }
    
    /* Client operation for step 5 */
    private String responseNego() {
        String m = nc + m2 + m3 + m4;
        String h = FunctionH.hash(m);
        
        SymetricKey symKey = getSymKey();
        this.status = AtemptingLogging;
        return symKey.crypt(generator.genRandomIV(), h);
    }   
    
    /* Client validation for step 6 */
    private boolean validateAuth(String message){
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        
        String[] split = value.split(" ");
        if (split.length < 1) {
            return false;
        }
        this.ns1 = split[split.length - 1];
        
        return isValidN(ns1) && value.equals("DONNER NUMERO ET MOT DE PASSE " +  ns1);
    }
        
    /* Client operation for step 7 */
    private String responseAuth() {
        SymetricKey symKey = getSymKey();
        String m = this.numCpt + " " + this.passwd + " " + ns1;
        this.status = Logged;
        return symKey.crypt(generator.genRandomIV(), m);
    }
    
    /* Client validation for step 8 and 10 */
    private boolean validateOperation(String message) {        
        SymetricKey symKey = getSymKey();
        String value = symKey.decrypt(message);
        String[] values = value.split(" ");
        if (values.length < 3) {
            return false;
        }
            
        if ("CHOISIR".equals(values[0]) && "OPERATION".equals(values[1])) {
            this.ns2 = values[values.length - 1];
        } else if ("REPONSE".equals(values[0])) {
            String code = values[0];
            this.ns2 = values[1];
            String serverNc1 = values[2];
            if (!nc1.equals(serverNc1)) {
                return false;
            }
            if ("QUITTER".equals(operation) && "1".equals(code)) {
                this.status = ConnectionClosed;
            }
        } else {
            return false;
        }
        return isValidN(ns2);
    }
    
    /* Client operation for step 9 */
    private String responseOperation() {
        this.nc1 = generator.genRandomN();
        String response;
        if ("TRANSFERT".equals(this.operation)) {
            response = "TRANSFERT " + this.destination + " " + this.montant + " " + ns2 + " " + nc1;
        } else if ("QUITTER".equals(this.operation)) {
            response = "QUITTER " + ns2 + " " + nc1;
        } else {
            return "unknown operation";
        }
        SymetricKey symKey = getSymKey();
        return symKey.crypt(generator.genRandomIV(), response);
    }
    
    private boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    
    private boolean isValidN(String value) {
        return isBetween(value, 0, 99999);
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

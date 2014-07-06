package tp2crypto;

public class Server {
    private static Key PRIVATE_KEY = new Key(479, 3811);
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;

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
        status = "connected";
        this.nc = value;
        this.ns = "12150"; // Generator.genRand();
        return String.valueOf(ns) + " " + cert;
    }

    void setStatus(String status) {
        this.status = status;
    }

    private String accept(String value) {
        String k = Crypto.rsa(value, PRIVATE_KEY);
        String k0 = String.valueOf(Integer.parseInt(FunctionH.hash("0" + nc + k + ns)) % 65);
        String k1 = String.valueOf(Integer.parseInt(FunctionH.hash("1" + nc + k + ns)) % 65);
        String k2 = String.valueOf(Integer.parseInt(FunctionH.hash("2" + nc + k + ns)) % 65);
        String k3 = String.valueOf(Integer.parseInt(FunctionH.hash("3" + nc + k + ns)) % 65);
        String k4 = String.valueOf(Integer.parseInt(FunctionH.hash("4" + nc + k + ns)) % 65);
        String k5 = String.valueOf(Integer.parseInt(FunctionH.hash("5" + nc + k + ns)) % 65);
        
        return "to_complete";
    }
}

package tp2crypto;

public class Server {
    private static int PRIVATE_KEY = 479;
    private String status = "waiting";
    private static final String cert = "www.desjardins.com VERISIGN 2025 01 01 23 3811 6069";
    private String nc;
    private String ns;

    public String receive(String value) {
        switch (status) {
            case "waiting":
                return init(value);
            case "connected":
        }
        
        return "";
    }

    private String init(String value) {
        status = "connected";
        this.nc = value;
        this.ns = Generator.genRand();
        return String.valueOf(ns) + " " + cert;
    }

    void setStatus(String status) {
        this.status = status;
    }
}

package tp2crypto;

public class Database {
    private static final String username = "80123";
    private static final String password = "BN12Z";
    
    
    public static boolean validateCredential(String username, String password) {
        return Database.username.equals(username) && Database.password.equals(password);
    }

    static boolean doTransfert(String compte, String amount) {
        System.out.println(amount + " has been draw from " + compte);
        return true;
    }
}

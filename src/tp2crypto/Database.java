package tp2crypto;

public class Database {
    private static final String username1 = "80123";
    private static final String password1 = "BN12Z";
    private static int solde1 = 500;
    
    private static final String username2 = "3124";
    private static final String password2 = "A12SL";
    private static int solde2 = 200;
    
    // NumCompte=80123,MotPasse=BN12Z,SoldeDuCompte=500$)etBob(NumCompte=3124, MotPasse=A12SL, SoldeDuCompte=200$)
    public static boolean validateCredential(String username, String password) {
        return Database.username1.equals(username) && Database.password1.equals(password) || 
                Database.username2.equals(username) && Database.password2.equals(password);
    }

    static boolean doTransfert(String compte, int amount) {
        switch (compte) {
            case Database.username1:
                if (Database.solde1 < amount) {
                    return false;
                }
                Database.solde1 -= amount;
                return true;
            case Database.username2:
                if (Database.solde2 < amount) {
                    return false;
                }
                Database.solde2 -= amount;
                return true;
        }
        return false;
    }
}

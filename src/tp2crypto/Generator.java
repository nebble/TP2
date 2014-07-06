package tp2crypto;

import java.util.Random;

public class Generator {
    
    public static String genRandomN(){
        return genRand(0, 99999);
    }
    
    public static String genRandomK(){
        return genRand(2, 3810);
    }
    
    public static String genRand(int min, int max){
        
        Random rand = new Random();

        // nextInt exlus la valeur limite maximum. On ajoute 1.
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return String.valueOf(randomNum);
    }
}

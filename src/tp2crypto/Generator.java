package tp2crypto;

import java.util.Random;

public class Generator {
    
    public static String genRand(){
        return String.valueOf(genRand(0, Integer.MAX_VALUE - 1));
    }
    
    public static int genRand(int min, int max){
        
        Random rand = new Random();

        // nextInt exlus la valeur limite maximum. On ajoute 1.
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
}

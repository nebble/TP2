package tp2crypto;

import java.util.Random;

public class Generator {
    
    public String genRandomN(){
        return genRand(0, 99999);
    }
    
    public String genRandomK(){
        return genRand(2, 3810);
    }
    
    public String genRandomIV() {
        String iv = "";
        for(int i = 0 ; i < 6 ; i++) {
            iv += Language.charL0(genRand(0, 65));
        }
        return iv;
    }
    
    public static String genRand(int min, int max){
        
        Random rand = new Random();

        // nextInt exlus la valeur limite maximum. On ajoute 1.
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return String.valueOf(randomNum);
    }
}

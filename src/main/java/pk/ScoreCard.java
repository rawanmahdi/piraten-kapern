package pk;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;
import org.apache.logging.log4j.*;

public class ScoreCard{ // class acting as the games score card, used to keep track of player's score during their turn

    public static int computeScore(Player player, ArrayList<Object> card){ // method to compute score regardless of type of card we have 
        player.score = comboScore(player, card); // compute score for this roll 
        player.score = goldAndDiamond(player);    
        return player.score;
    }

    private static final Logger logger = LogManager.getLogger(ScoreCard.class);

    public static int goldAndDiamond(Player player ){ // method to compute score based on number of gold coins and diamonds rolled
        ArrayList<Object> faces = player.rolledFaces;
        for(int i=0; i<faces.size(); i++){
            if(faces.get(i)==Faces.GOLD || faces.get(i)==Faces.DIAMOND){
                player.score += 100;
            }
        }
        return player.score;
    }

    public static boolean threeSkulls(Player player){ // method to check if game should continue based on how many skulls a player has rolled
        int len = player.rolledFaces.size();
        boolean threeSkulls = false;
        for(int i=0; i<len; i++){
            if(player.rolledFaces.get(i) == Faces.SKULL){
                logger.trace("Player rolled a skull");
                player.numSkulls++;
            } 
            if(player.numSkulls >=3){  
                threeSkulls =  true;
                player.score = 0; // remove all of players points
                logger.trace("Player rolled 3 skulls, turn over.");
                break;
            }
        }
        return threeSkulls;
    }

    public static Map<Object,Integer> countFaces(ArrayList<Object> faces){
        Map<Object, Integer> faceCount = new HashMap<Object, Integer>();
        // create map with all faces we want 
        faceCount.put(Faces.MONKEY,0);
        faceCount.put(Faces.PARROT,0);
        faceCount.put(Faces.GOLD,0);
        faceCount.put(Faces.SABER,0);
        faceCount.put(Faces.DIAMOND,0);
        for(Object f: faces){ // for each face
            if(f != Faces.SKULL){
                faceCount.replace(f, faceCount.get(f)+1); // increment value in map where face = key
            }
        }
        return faceCount;

    }

    public static int comboScore(Player player, ArrayList<Object> card){
        ArrayList<Object> faces = player.rolledFaces;
        Map<Object,Integer> frequency = countFaces(faces); // get frequency map
        if(card.get(0) == Cards.MonkeyBuisness){
            int totalMP = frequency.get(Faces.MONKEY) + frequency.get(Faces.PARROT);
            frequency.replace(Faces.MONKEY, totalMP); // add parrot count to monkey count
            frequency.replace(Faces.PARROT, 0); // remove parrot count
        }
        for(Map.Entry<Object,Integer> entry: frequency.entrySet()){ //iterate over map via entries
            Object face = entry.getKey();
            int freq = entry.getValue();
            switch(freq){
                case 3:
                    player.score += 100;
                    break;
                case 4:
                    player.score += 200;
                    break;
                case 5:
                    player.score += 500;
                    break;
                case 6: 
                    player.score += 1000;
                    break;
                case 7:
                    player.score += 2000;
                    break;
                case 8:
                    player.score += 4000;
                    break;
            }
        }
        return player.score;
    }
}
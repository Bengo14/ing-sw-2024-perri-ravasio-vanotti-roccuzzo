package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class StartingCardTest {
       private static ArrayList<StartingCard> startingCardsDeck;
    @BeforeAll
    public static void setUp(){
        try(FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/startingCards.json")){
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<StartingCard>>() {}.getType();
            startingCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructor() {
        StartingCard sc = new StartingCard("back", "front");
        assertEquals("back", sc.getBackImgPath());
        assertEquals("front", sc.getFrontImgPath());

    }

    @Test
    public void testUpdateResourceCounter() {
        StartingCard sc = startingCardsDeck.get(0);

        int[] counter = new int[4];
        for (int i = 0; i < 4; i++) {
            assertEquals(0, counter[i]);
        }
        sc.updateResourceCounter(counter);
        if(sc.isFront()){
            for (int i = 0; i < 4; i++) {
                assertEquals(1, counter[i]);
            }
        }
    }

//        //StartingCard sc = Deck.generateStartingCardsDeck();
//        int[] counter = new int[4];
//        sc.updateResourceCounter(counter);
//        assertEquals(1, counter[0]);
//        assertEquals(1, counter[1]);
//        assertEquals(1, counter[2]);
//        assertEquals(1, counter[3]);
//        assertNotEquals(0,counter[1]);
//    }



    @Test
    public void testGetResourceCenterBack() {
        StartingCard s = new StartingCard("back", "front");
        assertNull(s.getResourcesCentreBack());
    }
}

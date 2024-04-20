package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

class PlaceableCardTest {
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
   /*@Test
    public void testConstructor() {
    PlaceableCard p = new PlaceableCard("back", "front");
    assertNotNull(p);

    }*/


    @Test
    void switchFrontBack() {
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assert(p.isFront());
        p.switchFrontBack();
        assert(!p.isFront());
    }
    @Test
    public void testGetvisibleCorner(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        Corner[] c = p.getVisibleCorners();
        for (int i = 0; i < 4; i++) {
            assert(c[i] == p.getCorners()[i]);
        }
    }
    @Test
    public void testGetcorner(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        Corner[] c = p.getCorners();
        for (int i = 0; i < 8; i++) {
            assert(c[i] == p.getCorners()[i]);
        }
    }
}
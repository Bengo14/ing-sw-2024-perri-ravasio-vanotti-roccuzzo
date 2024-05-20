package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PlaceableCardTest {

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
        Deck.initDeck();
        PlaceableCard p = Deck.drawCardFromResourceDeck();
        Corner[] c = p.getVisibleCorners();
        assertEquals(4,c.length);
        for (int i = 0; i < 4; i++) {
            assertEquals(c[i], p.getVisibleCorners()[i]);
        }
        p.switchFrontBack();
        c = p.getVisibleCorners();
        for (int i = 0; i < 4; i++) {
            assertEquals(c[i], p.getVisibleCorners()[i]);
        }
    }

    @Test
    public void testSetCoordinates(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        p.setCoordinates(1, 2);
        assert(p.getLine() == 1);
        assert(p.getColumn() == 2);
    }
    @Test
    public void testGetImgPath(){
        PlaceableCard p = new PlaceableCard("back", "front") {
            @Override
            public void updateResourceCounter(int[] counter) {
            }
        };
        assert(p.getBackImgPath().equals("back"));
        assert(p.getFrontImgPath().equals("front"));
    }
}
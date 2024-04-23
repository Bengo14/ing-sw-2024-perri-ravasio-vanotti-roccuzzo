package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;


class PlayerTableTest {



    @Test
    void setCardOnHandInTheEmptyPosition() {
        Deck.initDeck();
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        p.setCardOnHandInTheEmptyPosition(Deck.getResourceCardsDeck().get(0));
        assertNotNull(p.getCardOnHand(2));
    }

//    @Test
//    public void testGetelement(){
//        StartingCard start = startingCardsDeck.get(0);
//        start.setCoordinates(29,29);
//        ResourceCard plant_0 = resourceCardsDeck.get(6);
//        ResourceCard plant_1 = resourceCardsDeck.get(7);
//        ResourceCard plant_2 = resourceCardsDeck.get(11);
//        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
//        assertNotNull(p.getElement(29,29));
//    }


    @Test
    public void testIsPlaceable(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);


        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{});
        p.setStartingCard(start);
        start.setCoordinates(29,29);

        boolean val = p.isPlaceable(28,28);

        System.out.println(val);


        //assertTrue(p.isPlaceable(28,28));


    }

//    @Test
//    public void testIsCheap(){
//        StartingCard start = startingCardsDeck.get(0);
//        start.setCoordinates(29,29);
//        GoldCard gold = goldCardsDeck.get(0);
//        ResourceCard plant_0 = resourceCardsDeck.get(6);
//        ResourceCard plant_1 = resourceCardsDeck.get(7);
//        ResourceCard plant_2 = resourceCardsDeck.get(11);
//        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
//        (p.checkAndPlaceCard(3,28,28,7));
//    }




    @Test
    public void testConstructorAndGetterPlayerTable(){
        Deck.initDeck();
        ResourceCard plant_0 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(11);

        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_0,plant_1,plant_2});

        assertNotNull(p);
        assertEquals(1, p.getId());
        assertEquals("name", p.getNickName());
        p.setFirst();
        assertTrue(p.isFirst());
        assertEquals(0, p.getResourceCounter(0));
        assertNotNull(p.getCardOnHand(0));
    }
    @Test
    public void testCheckAndPlaceCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{res});
        p.setStartingCard(start);

      //  assertNotNull(p.getElement(28,30));
        p.checkAndPlaceCard(0, 29, 29, 3);
        assertNotNull(p.getElement(30,30));
    }
    @Test
    public void testPlaceStartingCard(){
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        PlayerTable p = new PlayerTable(0,"name",new ResourceCard[]{});
        p.setStartingCard(start);
        assertNotNull(p.getElement(29,29));
        System.out.println(p.getElement(29,29));
    }

}
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

class GoldCardTest {

    @Test
    public void testConstructorAndGetterGoldCard() {
        GoldCard g = new GoldCard("back", "front", 2, true, Resources.FUNGI, null, false, null);
        assertNotNull(g);
        assertEquals(Resources.FUNGI, g.getResourceCentreBack());
        assertTrue(g.isPointsForCorners());
        assertFalse(g.isPointsForItems());
        assertNotEquals(Items.INKWELL, g.getItemThatGivesPoints());
    }
    @Test
    public void testGetresourcedRequired() {

        Deck.initDeck();

        GoldCard gold = Deck.getGoldCardsDeck().get(0);
        gold.getResourcesRequired();
        System.out.println(gold.getResourcesRequired());
    }

    @Test
    public void testGetCardPointForPlacing() {
        MainTable main = new MainTable();
        Deck.initDeck();
        GoldCard g = Deck.getGoldCardsDeck().get(0);


        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(12);

        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);

        StartingCard start = Deck.getStartingCardsDeck().get(2);


        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setNumOfPlayers(2);

        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});

        main.setPlayerTable(1,p);
        main.switchCardOnHandFrontBack(1,1);
        main.switchCardOnHandFrontBack(1,0);
        main.switchCardOnHandFrontBack(1,2);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);


        main.playCardAndUpdatePoints(0,29,29,0,1);
        System.out.println(p.getResourceCounter(0));
        main.playCardAndUpdatePoints(1,29,29,1,1);
        System.out.println(p.getResourceCounter(0));
        main.playCardAndUpdatePoints(2,29,29,2,1);
        System.out.println(p.getResourceCounter(0));
        System.out.println("stampo tutti i valori degli elementi");
        System.out.println(p.getResourceCounter(0));
        System.out.println(p.getResourceCounter(1));
        System.out.println(p.getResourceCounter(2));
        System.out.println(p.getResourceCounter(3));

        System.out.println(main.getBoardPoints(1));
        assertEquals(5, main.getBoardPoints(1));



    }
    @Test
    public void testGetCardPointForItems() {

    }


}



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
    public void testGetCardPointForCorners() {
        MainTable main = new MainTable();
        GoldCard g = Deck.getGoldCardsDeck().get(0);

        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_3 = Deck.getResourceCardsDeck().get(12);
        StartingCard start = Deck.getStartingCardsDeck().get(5);
        start.switchFrontBack();

        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        main.setNumOfPlayers(2);
//       main.addPlayer(1, "name");
        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_1,plant_2,g});
        main.setPlayerTable(1,p);
        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);
//        p.setSecretObjective(obj);
//        p.setStartingCard(start);
//        p.placeStartingCard();

        main.playCardAndUpdatePoints(0,29,29,1,1);
        main.playCardAndUpdatePoints(1,29,29,3,1);
        System.out.println(p.getResourceCounter(2));
        main.playCardAndUpdatePoints(2,30,30,3,1);
        System.out.println(p.getResourceCounter(2));
        //assertEquals(4,g.getPoints(p));
        System.out.println(main.getBoardPoints(1));



    }
    @Test
    public void testGetCardPointForItems() {

    }


}



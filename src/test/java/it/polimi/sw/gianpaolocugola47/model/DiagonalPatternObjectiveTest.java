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


class DiagonalPatternObjectiveTest {


    @Test
    public void testConstructorFungi() {
        Deck.initDeck();
        //create the card from json file
        DiagonalPatternObjective f = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(8);
        assertNotNull(f);
        assertEquals(true, f.isAscending());
        assertEquals(Resources.FUNGI, f.getResource());
    }
    @Test
    public void testConstructorAnimal() {
        Deck.initDeck();

        //create the card from json file
        DiagonalPatternObjective a = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(9);
        assertNotNull(a);
        assertEquals(true, a.isAscending());
        assertEquals(Resources.ANIMAL, a.getResource());
    }
    @Test
    public void testConstructorPlant() {
        Deck.initDeck();

        //create the card from json file
        DiagonalPatternObjective p = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(10);
        assertNotNull(p);
        assertEquals(false, p.isAscending());
        assertEquals(Resources.PLANT, p.getResource());
    }
    @Test
    public void testConstructor() {
        Deck.initDeck();

        //create the card from json file
        DiagonalPatternObjective i = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(11);
        assertNotNull(i);
        assertEquals(false, i.isAscending());
        assertEquals(Resources.INSECTS, i.getResource());
    }
    @Test
    public void isResourceMatchedAndNotFlagged() {
        Deck.initDeck();

        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(11);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{});



    }
    @Test
    void testIsResourceMatchedAndNotFlagged() {
        Deck.initDeck();

        // Test when the resource matches and the card is not flagged
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(11);
        ResourceCard resourceCard = new ResourceCard("back", "front", 1, Resources.INSECTS);
        assertTrue(obj.isResourceMatchedAndNotFlagged(resourceCard));

        // Test when the resource does not match
        ResourceCard differentResourceCard = new ResourceCard("back", "front", 1, Resources.ANIMAL);
        assertFalse(obj.isResourceMatchedAndNotFlagged(differentResourceCard));

        // Test when the card is flagged
        resourceCard.setFlaggedForObjective(true);
        assertFalse(obj.isResourceMatchedAndNotFlagged(resourceCard));
    }

    @Test
    public void testCheckPatternAndComputePoints(){// da rivedere
        MainTable main = new MainTable();
        DiagonalPatternObjective obj = (DiagonalPatternObjective) Deck.getObjectiveCardsDeck().get(10);
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        ResourceCard plant_1 = Deck.getResourceCardsDeck().get(6);
        ResourceCard plant_2 = Deck.getResourceCardsDeck().get(7);
        ResourceCard plant_3 = Deck.getResourceCardsDeck().get(12);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(13);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(14);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(15);

        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1, plant_2, plant_3});
        main.setNumOfPlayers(2);
        main.setPlayerTable(1, player);
        player.setStartingCard(start);

        assertNotNull(player.getElement(29,29));
        player.checkAndPlaceCard(0, 29, 29, 3);
        player.setCardOnHandInTheEmptyPosition(res_1);
        assertNotNull(player.getElement(30,30));
        player.checkAndPlaceCard(1, 30, 30, 3);
        player.setCardOnHandInTheEmptyPosition(res_2);
        assertNotNull(player.getElement(31,31));
        player.checkAndPlaceCard(2, 31,31 , 3);
        player.setCardOnHandInTheEmptyPosition(res_3);
        assertNotNull(player.getElement(32,32));

        //assertEquals(2, obj.checkPatternAndComputePoints(player));
    }

}

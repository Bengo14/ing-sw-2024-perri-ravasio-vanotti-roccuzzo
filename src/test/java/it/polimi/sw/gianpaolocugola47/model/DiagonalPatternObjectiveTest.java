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
    private static ArrayList<DiagonalPatternObjective> objectiveCardsDeck;
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    @BeforeAll
    public static void setUpCardsDeck() {
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<DiagonalPatternObjective>>() {
            }.getType();
            objectiveCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/resourceCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<ResourceCard>>() {
            }.getType();
            resourceCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/startingCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<StartingCard>>() {
            }.getType();
            startingCardsDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstriuctorFungi() {
        //create the card from json file
        DiagonalPatternObjective f = (DiagonalPatternObjective) objectiveCardsDeck.get(8);
        assertNotNull(f);
        assertEquals(true, f.isAscending());
        assertEquals(Resources.FUNGI, f.getResource());
    }
    @Test
    public void testConstriuctorAnimal() {
        //create the card from json file
        DiagonalPatternObjective a = (DiagonalPatternObjective) objectiveCardsDeck.get(9);
        assertNotNull(a);
        assertEquals(true, a.isAscending());
        assertEquals(Resources.ANIMAL, a.getResource());
    }
    @Test
    public void testConstriuctorPlant() {
        //create the card from json file
        DiagonalPatternObjective p = (DiagonalPatternObjective) objectiveCardsDeck.get(10);
        assertNotNull(p);
        assertEquals(false, p.isAscending());
        assertEquals(Resources.PLANT, p.getResource());
    }
    @Test
    public void testConstriuctor() {
        //create the card from json file
        DiagonalPatternObjective i = (DiagonalPatternObjective) objectiveCardsDeck.get(11);
        assertNotNull(i);
        assertEquals(false, i.isAscending());
        assertEquals(Resources.INSECTS, i.getResource());
    }
    @Test
    public void isResourceMatchedAndNotFlagged() {
        DiagonalPatternObjective obj = (DiagonalPatternObjective) objectiveCardsDeck.get(11);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{});



    }
    @Test
    void testIsResourceMatchedAndNotFlagged() {
        // Test when the resource matches and the card is not flagged
        DiagonalPatternObjective obj = (DiagonalPatternObjective) objectiveCardsDeck.get(11);
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
    public void testCheckPatternAndComputePoints(){
        DiagonalPatternObjective obj = (DiagonalPatternObjective) objectiveCardsDeck.get(10);
        StartingCard start = startingCardsDeck.get(2);
        ResourceCard plant_1 = resourceCardsDeck.get(6);
        plant_1.switchFrontBack();
        ResourceCard plant_2 = resourceCardsDeck.get(7);
        plant_2.switchFrontBack();
        ResourceCard plant_3 = resourceCardsDeck.get(12);
        plant_3.switchFrontBack();
        ResourceCard res_1 = resourceCardsDeck.get(13);
        ResourceCard res_2 = resourceCardsDeck.get(14);
        ResourceCard res_3 = resourceCardsDeck.get(15);
        MainTable main = new MainTable();
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1, plant_2, plant_3});
        main.setNumOfPlayers(2);
        main.setPlayerTable(1, player);
        player.setStartingCard(start);
        player.placeStartingCard();
        assertNotNull(player.getElement(29,29));
        player.checkAndPlaceCard(0, 29, 29, 3);
        player.setCardOnHandInTheEmptyPosition(res_1);
        assertNotNull(player.getElement(30,30));
        player.checkAndPlaceCard(1, 30, 30, 3);
        player.setCardOnHandInTheEmptyPosition(res_2);
        assertNotNull(player.getElement(31,31));
        player.checkAndPlaceCard(2, 31,31 , 3);
        player.setCardOnHandInTheEmptyPosition(res_3);
        //assertNotNull(player.getElement(32,32));

        assertEquals(2, obj.checkPatternAndComputePoints(player));
    }

}

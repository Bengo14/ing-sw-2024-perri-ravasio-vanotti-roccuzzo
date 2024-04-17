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
    private static ArrayList<ResourceObjective> objectiveCardsDeck;
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    private static ArrayList<GoldCard> goldCardDeck;

    @BeforeAll
    public static void setUpObjectives() {
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/goldCards.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<GoldCard>>() {
            }.getType();
            goldCardDeck = gson.fromJson(fileReader, listOfCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<ResourceObjective>>() {
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
    public void testConstructorAndGetterGoldCard() {
        GoldCard g = new GoldCard("back", "front", 2, true, Resources.FUNGI, null, false, null);
        assertNotNull(g);
        assertEquals(Resources.FUNGI, g.getResourceCentreBack());
        assertTrue(g.isPointsForCorners());
        assertFalse(g.isPointsForItems());
        assertNotEquals(Items.INKWELL, g.getItemThatGivesPoints());
    }

//    @Test
//    public void testGetCardPointForCorners() {
//        //create gold card and 2 resource card from json
//        GoldCard g = goldCardDeck.get(1);
//        ResourceCard r1 = resourceCardsDeck.get(0);
//        ResourceCard r2 = resourceCardsDeck.get(1);
//        ResourceCard r3 = resourceCardsDeck.get(2);
//        StartingCard start = startingCardsDeck.get(0);
//        PlayerTable p = new PlayerTable(1, "name", new ResourceCard[]{r1, r2, r3});
//        start.setCoordinates(29, 29);
//        p.setStartingCard(start);
//        r1.setCoordinates(30, 30);
//        r2.setCoordinates(30, 28);
//        g.setCoordinates(31, 29);
//        g.getPoints(p, 31, 29);
//        System.out.println(g.getPoints(p, 31, 29));
//        //assertEquals(2, g.getPoints(p, 31, 29));
//    }

        //    @Test
//    public void testGetCardPointForCorners() {
//        GoldCard g = new GoldCard("back", "front", 2, true, Resources.FUNGI, null, false, null);
//        PlayerTable p = new PlayerTable(1, "nome", 2, 1);
//
//
//    }
//    @Test
//    public void testGetCardPointForItems() {
//        GoldCard g = new GoldCard("back", "front", 2, false, Resources.FUNGI, Items.INKWELL, true, null);
//        PlayerTable p = new PlayerTable(1, "name1",new DiagonalPatternObjective(2,
//                "back", "front", true, Resources.FUNGI),new StartingCard("back","front"),new ResourceCard[]{});
//
//        int points = g.getCardPoints(p,0,0);
//        assertEquals(2, points);
//    }
//    @Test
//    public void testUpdateResourceCounter() {
//        GoldCard g = goldCardDeck.get(0);
//        int[] counter = new int[7];
//        for (int i = 0; i < counter.length; i++) {
//            assertEquals(0, counter[i]);
//        }
//        g.updateResourceCounter(counter);
//        assertEquals(1, counter[5]);
//    }
//    @Test
//    public void testGetCardPointDefault() {
//    }
//
//    @Test
//    public void testUpdateResourceCounter() {
//        GoldCard goldCard = new GoldCard("back", "front", 2, true, Resources.FUNGI, null, false, null);
//        int[] counter = new int[12];
//        for (int i = 0; i < counter.length; i++) {
//            assertEquals(0, counter[i]);
//        }
//        goldCard.updateResourceCounter(counter);
//        assertEquals(1, counter[5]);
//


}



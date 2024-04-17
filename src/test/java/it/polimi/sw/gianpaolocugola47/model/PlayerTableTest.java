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


class PlayerTableTest {
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    private static ArrayList<ResourceObjective> objectiveCardsDeck;

    @BeforeAll
    public static void setUpStartingCArd() {

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
    void setCardOnHandInTheEmptyPosition() {
        ResourceCard plant_0 = resourceCardsDeck.get(6);
        ResourceCard plant_1 = resourceCardsDeck.get(7);
        ResourceCard plant_2 = resourceCardsDeck.get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        p.setCardOnHandInTheEmptyPosition(resourceCardsDeck.get(0));
        assertNotNull(p.getCardOnHand(2));
    }
    @Test
    public void testGetCanPlay(){
        ResourceCard plant_0 = resourceCardsDeck.get(6);
        ResourceCard plant_1 = resourceCardsDeck.get(7);
        ResourceCard plant_2 = resourceCardsDeck.get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        assertTrue(p.getCanPlay());

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
        StartingCard start = startingCardsDeck.get(0);
        start.setCoordinates(29,29);
        ResourceCard plant_0 = resourceCardsDeck.get(6);
        ResourceCard plant_1 = resourceCardsDeck.get(7);
        ResourceCard plant_2 = resourceCardsDeck.get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        assertTrue(p.isPlaceable(28,28));
    }
    @Test
    public void testGetObjectivePoints(){
        StartingCard start = startingCardsDeck.get(0);
        Objectives obj = objectiveCardsDeck.get(4);
        start.setCoordinates(29,29);
        ResourceCard plant_0 = resourceCardsDeck.get(6);
        ResourceCard plant_1 = resourceCardsDeck.get(7);
        ResourceCard plant_2 = resourceCardsDeck.get(11);
        PlayerTable p = new PlayerTable(0, "name",new ResourceCard[]{plant_0,plant_1,plant_2});
        //assertEquals(2,p.getObjectivePoints(obj));
    }




    @Test
    public void testConstructorAndGetterPlayerTable(){
        ResourceCard plant_0 = resourceCardsDeck.get(6);
        ResourceCard plant_1 = resourceCardsDeck.get(7);
        ResourceCard plant_2 = resourceCardsDeck.get(11);

        PlayerTable p = new PlayerTable(1, "name",new ResourceCard[]{plant_0,plant_1,plant_2});

        assertNotNull(p);
        assertEquals(1, p.getId());
        assertEquals("name", p.getNickName());
        p.setFirst();
        assertTrue(p.isFirst());
        assertEquals(0, p.getResourceCounter(0));
        assertNotNull(p.getCardOnHand(0));
    }
}
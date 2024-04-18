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

class LShapePatternObjectiveTest {
    private static ArrayList<LShapePatternObjective> objectiveCardsDeck;
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    @BeforeAll
    public static void setUpObjectives() {
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<LShapePatternObjective>>() {
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
    //in commento tanto non serve ad aumentare la coverage
//    @Test
//    public void testConstructor() {
//        LShapePatternObjective obj = objectiveCardsDeck.get(12);
//        assertNotNull(obj);
//        assertEquals(3, obj.getPoints());
//        assertEquals("imgPathFrn", obj.getImgPathFront());
//        assertEquals("imgPathBck", obj.getImgPathBack());
//
//    }
//    @Test
//    public void testCheckPatternAndComputePoints() {
//        LShapePatternObjective obj = objectiveCardsDeck.get(12);
//        //create 4 card from json file all in back front
//        StartingCard start = startingCardsDeck.get(0);
//        ResourceCard resource_1= resourceCardsDeck.get(0);
//        ResourceCard resource_2= resourceCardsDeck.get(1);
//        ResourceCard resource_3= resourceCardsDeck.get(2);
//        ResourceCard resource_4= resourceCardsDeck.get(3);
//        //sett the card on the table
//
//        PlayerTable playerTable = new PlayerTable(1,"name",new ResourceCard[]{});
//        start.isFront();
//        start.setCoordinates(29,29);
//        //set card on back front
//
//
//        assertEquals(0, obj.checkPatternAndComputePoints(playerTable));
//    }
}
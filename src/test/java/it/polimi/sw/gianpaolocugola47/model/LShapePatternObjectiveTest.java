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
    @Test
    public void testConstructor() {
        LShapePatternObjective obj = objectiveCardsDeck.get(12);
        assertNotNull(obj);
        assertEquals(3, obj.getPoints());
        assertEquals("imgPathFrn", obj.getImgPathFront());
        assertEquals("imgPathBck", obj.getImgPathBack());



    }
}
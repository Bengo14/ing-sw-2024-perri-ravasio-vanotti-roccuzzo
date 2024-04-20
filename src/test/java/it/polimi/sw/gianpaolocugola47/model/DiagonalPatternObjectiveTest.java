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
    //create a list of 16 objectives from json file
    private static ArrayList<DiagonalPatternObjective> objectiveCardsDeck;

    @BeforeAll
    public static void setUpObjectives() {
        try (FileReader fileReader = new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json")) {
            Gson gson = new Gson();
            Type listOfCards = new TypeToken<ArrayList<DiagonalPatternObjective>>() {
            }.getType();
            objectiveCardsDeck = gson.fromJson(fileReader, listOfCards);
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
}
//    @Test
//    public void testCheckPatternAndComputePoints(){
//        PlayerTable player = new PlayerTable(1,"name",);
//    }
//}


    //create a test class for DiagonalPattern using the objectives taken from the json file

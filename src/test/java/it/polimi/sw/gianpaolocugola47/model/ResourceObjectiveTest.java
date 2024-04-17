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

class ResourceObjectiveTest {
    private static ArrayList<ResourceObjective> objectiveCardsDeck;
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    @BeforeAll
    public static void setUpObjectives() {
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
    public void testConstructorPlant(){
        //create card from json file
        ResourceObjective plant = objectiveCardsDeck.get(4);
        assertNotNull(plant);
        assertEquals(Resources.PLANT, plant.getResource());
    }
    @Test
    public void testConstructorInsect(){
        //create card from json file
        ResourceObjective insect = objectiveCardsDeck.get(5);
        assertNotNull(insect);
        assertEquals(Resources.INSECTS, insect.getResource());
    }
    @Test
    public void testConstructorFungi(){
        //create card from json file
        ResourceObjective fungi = objectiveCardsDeck.get(6);
        assertNotNull(fungi);
        assertEquals(Resources.FUNGI, fungi.getResource());
    }
    @Test
    public void testConstructorAnimal(){
        ResourceObjective animal = objectiveCardsDeck.get(7);
        assertNotNull(animal);
        assertEquals(Resources.ANIMAL, animal.getResource());
    }


    @Test
    public void checkPatternAndComputePoints() {
        StartingCard start = startingCardsDeck.get(0);
        //control the points of the objective card
        // put the starting card in the front side

        start.setCoordinates(29,29);
        ResourceObjective plant = objectiveCardsDeck.get(4);
        //control the points of the objective cardù
        //inizialize counter
        int [] counter = new int[7];
        for(int i=0; i<7; i++){
            counter[i]=0;
        }

        assertEquals(2,plant.getPoints());
        ResourceCard plant_1 = resourceCardsDeck.get(6);
        ResourceCard plant_2 = resourceCardsDeck.get(7);
        ResourceCard plant_3 = resourceCardsDeck.get(11);
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1,plant_2,plant_3});
        player.setStartingCard(start);
        //play 3 plant card in player table using checkAndPlaceCard method

        plant_1.switchFrontBack();
//        plant_1.getResourceCentreBack();
        plant_2.switchFrontBack();
//        plant_2.getResourceCentreBack();
        plant_3.switchFrontBack();
//        plant_3.getResourceCentreBack();
        plant_1.setCoordinates(28,30);
        plant_2.setCoordinates(28,28);
        plant_3.setCoordinates(30,28);
        //how to see the counter
        //how to invoke the method update resource counter
        plant_1.updateResourceCounter(counter);
        plant_2.updateResourceCounter(counter);
        plant_3.updateResourceCounter(counter);
        for(int i=0; i<7; i++){
            System.out.println(counter[i]);
        }
          System.out.println("\n");
        System.out.println(player.getResourceCounter(6));
//        player.checkAndPlaceCard(0,71,73,2);
//        player.checkAndPlaceCard(1,71,71,3);
//        player.checkAndPlaceCard(2,73,71,1);
        int resourceTrebleOccurrences;
        resourceTrebleOccurrences= counter[2]/3 * 2;
        assertEquals(2,resourceTrebleOccurrences);
    }
}
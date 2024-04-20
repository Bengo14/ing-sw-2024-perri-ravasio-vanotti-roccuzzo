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
        StartingCard start = startingCardsDeck.get(2);
        //control the points of the objective card


        ResourceObjective obj = objectiveCardsDeck.get(4);
        //control the points of the objective card


        //assertEquals(2,obj.getPoints());
        ResourceCard plant_1 = resourceCardsDeck.get(6);
        ResourceCard plant_2 = resourceCardsDeck.get(7);
        ResourceCard plant_3 = resourceCardsDeck.get(12);
        MainTable main = new MainTable();
        PlayerTable player = new PlayerTable(1,"name",new ResourceCard[]{plant_1,plant_2,plant_3});
        main.setNumOfPlayers(2);
        main.setPlayerTable(1, player);
        player.setSecretObjective(obj);
        player.setStartingCard(start);
        player.placeStartingCard();

        //play 3 plant card in player table using checkAndPlaceCard method

        plant_1.switchFrontBack();
//        plant_1.getResourceCentreBack();
        plant_2.switchFrontBack();
//        plant_2.getResourceCentreBack();
        plant_3.switchFrontBack();

        player.checkAndPlaceCard(0,29,29,1);
        player.checkAndPlaceCard(1,29,29,3);
        player.checkAndPlaceCard(2,29,29,0);
        assertEquals(2,obj.checkPatternAndComputePoints(player));



//        player.checkAndPlaceCard(0,71,73,2);
//        player.checkAndPlaceCard(1,71,71,3);
//        player.checkAndPlaceCard(2,73,71,1);

    }
}
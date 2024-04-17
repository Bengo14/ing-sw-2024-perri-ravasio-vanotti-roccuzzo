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

class MainTableTest {
    private static ArrayList<ResourceObjective> objectiveCardsDeck;
    private static ArrayList<ResourceCard> resourceCardsDeck;
    private static ArrayList<StartingCard> startingCardsDeck;
    @BeforeAll
    public static void setUpCardsDeck() {
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
    public void testConstructor() {
        MainTable mainTable = new MainTable();
        assertNotNull(mainTable);
    }

    @Test
    public void setEndGame() {
        MainTable mainTable = new MainTable();
        mainTable.setEndGame();
        assertTrue(mainTable.isEndGame());

    }

    @Test
    public void setNumOfPlayers() {
        MainTable mainTable = new MainTable();
        mainTable.setNumOfPlayers(2);
    assertEquals(2, mainTable.getNumOfPlayers());
    }
    @Test
    public void drawTwoPossibleSecretObjectives() {
        MainTable mainTable = new MainTable();
        Objectives[] objectives = mainTable.drawTwoPossibleSecretObjectives();
        assertNotNull(objectives);
        assertEquals(2, objectives.length);
    }



    @Test
    public void drawStartingCard() {
        MainTable mainTable = new MainTable();
        StartingCard start = mainTable.drawStartingCard();
        assertNotNull(start);
    }

//    @Test
//    public void testSetPlayerStartingCard(){
//        MainTable table = new MainTable();
//        table.setNumOfPlayers(2);
//        PlayerTable player = new PlayerTable(0,"name",new ResourceCard[]{});
//        table.setPlayerStartingCard(0, startingCardsDeck.get(0));
//    }



//    @Test
//    public void drawCardFrom() {
//        MainTable mainTable = new MainTable();
//        mainTable.drawCardFrom(Deck.getGoldDeck());
//    }


}
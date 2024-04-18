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

    @Test
    public void testSetPlayerStartingCard() {

        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{});
        MainTable table = new MainTable();
        table.setNumOfPlayers(2);
        table.setPlayerTable(0, player);
        StartingCard start = table.drawStartingCard();
        table.setPlayerStartingCard(0, start);
        table.getPlayerTable(0);
        assertEquals(start, table.getPlayerTable(0).getStartingCard());
        boolean[][] matrix = table.checkAllPlayablePositions(0);
        assertEquals(PlayerTable.getMatrixDimension(), matrix.length);
        assertEquals(PlayerTable.getMatrixDimension(), matrix[0].length);
        // Verifica che tutti i valori nella matrice siano false tranne quelli adiacenti alla StartingCard
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (player.isPlaceable(i, j)) {
                    assertTrue(matrix[i][j]);
                } else {
                    assertFalse(matrix[i][j]);
                }
            }
        }
    }
    @Test
    public void testDrawCardFrom(){
        MainTable mainTable = new MainTable();
        ResourceCard res_1 = resourceCardsDeck.get(0);
        ResourceCard res_2 = resourceCardsDeck.get(1);
        ResourceCard res_3 = resourceCardsDeck.get(2);
        mainTable.setNumOfPlayers(2);
        Deck.initDeck();
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1,res_2,res_3});
        mainTable.setPlayerTable(0, player);
        StartingCard start = mainTable.drawStartingCard();
        mainTable.setPlayerStartingCard(0, start);
        player.checkAndPlaceCard(0,28,28,1);
        mainTable.drawCardFrom(0,0);
        assertFalse(mainTable.getPlayerTable(0).getCardOnHand(1).isFront());
    }
    @Test
    public void testSetPlayerSecretObjective(){
        MainTable mainTable = new MainTable();
        Objectives obj = objectiveCardsDeck.get(0);
        Objectives obj2 = objectiveCardsDeck.get(1);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{});
        mainTable.setNumOfPlayers(2);
        mainTable.setPlayerTable(0, player);
        mainTable.setPlayerSecretObjective(0, obj);
        assertEquals(obj, mainTable.getPlayerTable(0).getSecretObjective());
        assertNotEquals(obj2, mainTable.getPlayerTable(0).getSecretObjective());
    }
    @Test
    public void testSwitchCardOnHandFrontBack(){
        MainTable mainTable = new MainTable();
        ResourceCard res_1 = resourceCardsDeck.get(0);
        ResourceCard res_2 = resourceCardsDeck.get(1);
        ResourceCard res_3 = resourceCardsDeck.get(2);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1, res_2, res_3});
        mainTable.setNumOfPlayers(2);
        mainTable.setPlayerTable(0, player);
        mainTable.switchCardOnHandFrontBack(0, 0);
        assertFalse(mainTable.getPlayerTable(0).getCardOnHand(2).isFront());
    }



//    @Test
//    public void drawCardFrom() {
//        MainTable mainTable = new MainTable();
//        mainTable.drawCardFrom(Deck.getGoldDeck());
//    }


}
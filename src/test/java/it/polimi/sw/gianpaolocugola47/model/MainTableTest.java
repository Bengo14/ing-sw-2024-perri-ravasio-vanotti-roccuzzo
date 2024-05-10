package it.polimi.sw.gianpaolocugola47.model;

import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTableTest {

    RMIServer stub = RMIServer.getServer();

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
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(2);
        mainTable.setNumOfPlayers(2);
        Deck.initDeck();
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1,res_2,res_3});
        mainTable.setPlayerTable(0, player);
        StartingCard start = mainTable.drawStartingCard();
        mainTable.setPlayerStartingCard(0, start);
        player.checkAndPlaceCard(0,28,28,1);
        mainTable.drawCardFrom(0,0);
        assertEquals(false,mainTable.getPlayerTable(0).getCardOnHand(1).isFront());
    }
    @Test
    public void testSetPlayerSecretObjective(){
        MainTable mainTable = new MainTable();
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        Objectives obj2 = Deck.getObjectiveCardsDeck().get(1);
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
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(2);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1, res_2, res_3});
        mainTable.setNumOfPlayers(2);
        mainTable.setPlayerTable(0, player);
        mainTable.switchCardOnHandFrontBack(0, 0);
        assertEquals(false,mainTable.getPlayerTable(0).getCardOnHand(2).isFront());
    }
    @Test
    public void testCheckIfPlayerCanPlay(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{});
        PlayerTable player2 = new PlayerTable(1, "surname", new ResourceCard[]{});
        main.setPlayerTable(0, player);
        main.setPlayerTable(1, player2);
        StartingCard start = main.drawStartingCard();
        StartingCard start2 = main.drawStartingCard();
        main.setPlayerStartingCard(0, start);
        main.setPlayerStartingCard(1, start2);
        assertEquals(true,main.checkIfPlayerCanPlay(0));

    }
    @Test
    public void testTurnCardOnHand(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(2);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1, res_2, res_3});
        main.setPlayerTable(0, player);
        main.switchCardOnHandFrontBack(0, 0);
        main.turnCardOnHand(0,1);
        assertEquals(true,main.getPlayerTable(0).getCardOnHand(0).isFront());
    }
    @Test
    public void TestAddPlayer(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{});
        main.addPlayer(0, "name");
        assertNotNull(main.getPlayerTable(0));
    }
    @Test
    public void testComputeWinnerAtEndGame(){
        MainTable main = new MainTable();
        Deck.initDeck();
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(3);
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res_1});
        PlayerTable player2 = new PlayerTable(1, "surname", new ResourceCard[]{res_2});

        main.setPlayerTable(0, player);
        main.setPlayerTable(1, player2);

        StartingCard start = main.drawStartingCard();
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        Objectives obj2 = Deck.getObjectiveCardsDeck().get(1);
        main.setPlayerSecretObjective(0, obj);
        main.setPlayerSecretObjective(1, obj2);
        StartingCard start2 = main.drawStartingCard();
        start.switchFrontBack();
        res_1.switchFrontBack();
        res_2.switchFrontBack();
        main.setPlayerStartingCard(0, start);
        main.setPlayerStartingCard(1, start2);
        main.playCardAndUpdatePoints(0, 29, 29, 1, 0);
        main.playCardAndUpdatePoints(0, 29, 29, 3, 1);
        System.out.println(main.computeWinnerAtEndGame());
    }
    @Test
    public void testGetGlobalPoints(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        ResourceCard res = Deck.getResourceCardsDeck().get(0);
        res.switchFrontBack();
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res});
        main.setPlayerTable(0, player);
        main.setPlayerStartingCard(0, start);
        main.setPlayerSecretObjective(0, obj);
        main.playCardAndUpdatePoints(0, 29, 29, 1, 0);
        assertEquals(1, main.getGlobalPoints(0));
    }
}
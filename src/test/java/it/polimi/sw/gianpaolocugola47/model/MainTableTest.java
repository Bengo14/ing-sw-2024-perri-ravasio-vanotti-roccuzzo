package it.polimi.sw.gianpaolocugola47.model;

import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTableTest {

    final RMIServer stub = RMIServer.getServer();

    @Test
    public void testConstructor() {
        MainTable mainTable = new MainTable();
        assertNotNull(mainTable);
    }

    @Test
    public void testGettersAndSetters() {
        Deck.initDeck();
        MainTable mainTable = new MainTable();
        mainTable.setEndGame(false);
        mainTable.setCardsOnTable(new GoldCard[]{Deck.getGoldCardsDeck().getFirst()});
        mainTable.setGlobalObjectives(Deck.getObjectiveCardsDeck().toArray(new Objectives[0]));
        mainTable.setBoardPoints(new int[]{4, 0});
        mainTable.setGlobalPoints(new int[]{6, 2});
        PlayerTable Mario = new PlayerTable();
        PlayerTable Luigi = new PlayerTable();
        mainTable.setPlayersTables(new PlayerTable[]{Mario, Luigi});

        assertFalse(mainTable.getEndGame());
        assertNotNull(mainTable.getCardsOnTable());
        assertNotNull(mainTable.getGlobalObjectives());
        assertNotNull(mainTable.getBoardPoints());
        assertNotNull(mainTable.getGlobalPoints());
        assertNotNull(mainTable.getPlayersTables());
    }
    @Test
    public void testRemoveObserver() {
        MainTable mainTable = new MainTable();
        mainTable.removeObserver(stub);
        //the observer is in the list of observer, i?ve not rhe getObserver method
        assertFalse(mainTable.getObservers().contains(stub));
    }
    @Test
    public void testSetEndGame() {
        MainTable mainTable = new MainTable();
        mainTable.setEndGame(true);
        assertTrue(mainTable.getEndGame());
    }
    @Test
    public void testGetCardsOnTable() {
        MainTable mainTable = new MainTable();
        mainTable.setCardsOnTable(new GoldCard[]{Deck.getGoldCardsDeck().getFirst()});
        assertNotNull(mainTable.getCardsOnTable());
    }
    @Test
    public void getCardOnTable() {
        MainTable mainTable = new MainTable();
        mainTable.setCardsOnTable(new GoldCard[]{Deck.getGoldCardsDeck().getFirst()});
        assertNotNull(mainTable.getCardOnTable(0));
    }

    @Test
    public void setEndGame() {
        MainTable mainTable = new MainTable();
        mainTable.setEndGame();
        assertTrue(mainTable.getEndGame());
    }

    @Test
    public void setNumOfPlayers() {
        MainTable mainTable = new MainTable();
        mainTable.setNumOfPlayers(2);
        assertEquals(2, mainTable.getNumOfPlayers());
    }

    @Test
    public void drawTwoPossibleSecretObjectives() {
        Deck.initDeck();
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
        // test any matrix value is null except those surrounding StartingCard
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
        player.checkAndPlaceCard(0,9,9,1);
        mainTable.drawCardFrom(0,0);
        assertFalse(mainTable.getPlayerTable(0).getCardOnHand(1).getIsFront());
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
        mainTable.turnCardOnHand(0, 0, true);
        assertFalse(mainTable.getPlayerTable(0).getCardOnHand(2).getIsFront());
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
        assertTrue(main.checkIfPlayerCanPlay(0));
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
        main.turnCardOnHand(0, 0, true);
        main.turnCardOnHand(0,1, true);
        assertTrue(main.getPlayerTable(0).getCardOnHand(0).getIsFront());
    }

    @Test
    public void testInitView(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        main.initView();
        assertNotNull(main.getPlayersTables());
    }

    @Test
    public void testGetNicknames(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        main.addPlayer(0, "name_1");
        main.addPlayer(1, "name_2");
        String[] nicknames = main.getNicknames();
        assertEquals("name_1", nicknames[0]);
        assertEquals("name_2", nicknames[1]);
    }
    @Test
    public void testGetCardsOnHand(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(2);
        PlayerTable player_1 = new PlayerTable(0, "name", new ResourceCard[]{res_1});
        main.setPlayerTable(0, player_1);
        PlayerTable player_2 = new PlayerTable(1, "name", new ResourceCard[]{res_2, res_3});
        main.setPlayerTable(1, player_2);
        assertNotNull(main.getCardsOnHand());
    }
    @Test
    public void testGetResourceCounter(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        ResourceCard res_1 = Deck.getResourceCardsDeck().get(0);
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(1);
        ResourceCard res_3 = Deck.getResourceCardsDeck().get(2);
        PlayerTable player_1 = new PlayerTable(0, "name", new ResourceCard[]{res_1});
        main.setPlayerTable(0, player_1);
        StartingCard start = main.drawStartingCard();
        main.setPlayerStartingCard(0, start);
        assertNotNull(main.getResourceCounter(0));
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
        ResourceCard res_2 = Deck.getResourceCardsDeck().get(7);
        PlayerTable player_1 = new PlayerTable(0, "Adam", new ResourceCard[]{res_1});
        PlayerTable player_2 = new PlayerTable(1, "Bruce", new ResourceCard[]{res_2});
        main.setPlayerTable(0, player_1);
        main.setPlayerTable(1, player_2);
        StartingCard start_1 = main.drawStartingCard();
        StartingCard start_2 = main.drawStartingCard();
        Objectives obj_1 = Deck.getObjectiveCardsDeck().get(0);
        Objectives obj_2 = Deck.getObjectiveCardsDeck().get(1);
        main.setPlayerSecretObjective(0, obj_1);
        main.setPlayerSecretObjective(1, obj_2);
        start_1.switchFrontBack();
        start_2.switchFrontBack();
        res_1.switchFrontBack();
        res_2.switchFrontBack();
        main.setPlayerStartingCard(0, start_1);
        main.setPlayerStartingCard(1, start_2);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 0);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 0, 1);
        assertEquals(1, main.computeWinnerAtEndGame());
    }

    @Test
    public void testGetGlobalPoints(){
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().getFirst();
        ResourceCard res = Deck.getResourceCardsDeck().get(17);
        res.switchFrontBack();
        PlayerTable player = new PlayerTable(0, "name", new ResourceCard[]{res});
        main.setPlayerTable(0, player);
        main.setPlayerStartingCard(0, start);
        main.setPlayerSecretObjective(0, obj);
        main.playCardAndUpdatePoints(0, PlayerTable.getStartingCardPos(), PlayerTable.getStartingCardPos(), 1, 0);
        assertEquals(1, main.getGlobalPoints(0));
    }

}
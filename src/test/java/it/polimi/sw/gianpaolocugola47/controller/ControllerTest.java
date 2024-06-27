package it.polimi.sw.gianpaolocugola47.controller;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.network.rmi.RMIServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {
    private Controller controller;

    @BeforeEach
    void setUp() {
        controller = new Controller();
    }
    RMIServer stab = RMIServer.getServer();

    @Test
    void testResetGame() {
        controller.setNumOfPlayers(2);
        controller.addPlayer(1, "name");
        controller.addPlayer(0, "name");
        controller.resetGame();
        assertEquals(0, controller.getNumOfPlayersCurrentlyAdded());
    }

    @Test
    void testAddPlayer() {
        controller.setNumOfPlayers(2);
        controller.addPlayer(1, "name");
        assertEquals(1, controller.getNumOfPlayersCurrentlyAdded());
    }
    @Test
    public void testAddClientConnected(){
        controller.addClientConnected();
        assertEquals(1,controller.getClientsConnected());
    }

    @Test
    public void testDrawCard() {
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.drawStartingCard();
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setStartingCardAndDrawObjectives(1,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().getFirst());
        controller.drawCard(1,0);
    }

    @Test
    public void testGetterAndSetters(){
        Controller ctrl= new Controller();
        MainTable main= new MainTable();
        ctrl.setMainTable(main);
        assertEquals(main,ctrl.getMainTable());
    }
    @Test
    public void testSetCurrentPlayerId(){
        controller.setCurrentPlayerId(1);
        assertEquals(1,controller.getCurrentPlayerId());
    }
    @Test
    public void testSetClientConnected(){
        controller.setClientsConnected(1);
        assertEquals(1,controller.getClientsConnected());
    }
    @Test
    public void testSetPlayersAdded(){
        controller.setPlayersAdded(2);
        assertEquals(2,controller.getNumOfPlayersCurrentlyAdded());
    }
    @Test
    public void testSetStartingCardsAndObjAdded(){
        controller.setStartingCardsAndObjAdded(2);
        assertEquals(2,controller.getStartingCardsAndObjAdded());
    }
    @Test
    public void testSetLastTurn(){
        controller.setLastTurn(true);
        assertTrue(controller.isLastTurn());
    }
    @Test
    public void testGetPlayablePositions(){
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().getFirst());
        assertNotNull(controller.getPlayablePositions(0));
        controller.drawCard(1,0);
        assertNotNull(controller.getCardsOnHand());
        controller.playCard(0,9,9,0,0,true);
        assertNotNull(controller.getPlacedCards(0));
    }
    @Test
    public void testGetResourceCounter(){
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().getFirst());
        assertNotNull(controller.getSecretObjective(0));
        assertNotNull(controller.getResourceCounter(0));
    }
    @Test
    public void testGetNicknames(){
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        assertNotNull(controller.getNicknames());
        assertEquals(2,controller.getNicknames().length);
        assertEquals("name",controller.getNicknames()[0]);
        assertEquals("name_2",controller.getNicknames()[1]);
    }
    @Test
    public void testLoadGame(){
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().getFirst());
        controller.setStartingCardAndDrawObjectives(1,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(1,Deck.getObjectiveCardsDeck().getFirst());
        controller.drawCard(0,0);
        controller.drawCard(0,0);
        controller.drawCard(0,0);
        controller.drawCard(1,1);
        controller.drawCard(1,1);
        controller.drawCard(1,1);
        controller.loadGame();
        controller.startGameFromFile();
        assertEquals(2,controller.getNumOfPlayersCurrentlyAdded());
        assertNotNull(controller.getMainTable());
        assertTrue(controller.isGameLoaded());
    }
    @Test
    public void testComputeWinner(){
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().getFirst());
        controller.setStartingCardAndDrawObjectives(1,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(1,Deck.getObjectiveCardsDeck().getFirst());
        controller.drawCard(0,0);
        controller.drawCard(0,0);
        controller.drawCard(0,0);
        controller.drawCard(1,1);
        controller.drawCard(1,1);
        controller.drawCard(1,1);
        controller.computeWinner();
    }
}
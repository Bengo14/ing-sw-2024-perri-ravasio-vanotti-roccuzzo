package it.polimi.sw.gianpaolocugola47.controller;

import com.sun.tools.javac.Main;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.rmi.RMIServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.stream.ImageInputStream;

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
    public void testDrawCard() {
        controller.setNumOfPlayers(2);
        controller.addPlayer(0, "name");
        controller.addPlayer(1, "name_2");
        controller.drawStartingCards();
        controller.setStartingCardAndDrawObjectives(0,Deck.drawCardFromStartingDeck());
        controller.setStartingCardAndDrawObjectives(1,Deck.drawCardFromStartingDeck());
        controller.setSecretObjectiveAndUpdateView(0,Deck.getObjectiveCardsDeck().get(0));
        controller.drawCard(1,0);




    }
}
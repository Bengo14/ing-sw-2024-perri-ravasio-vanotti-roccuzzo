package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class StartingCardTest {

    @Test
    public void testGetResourceCenterBack() {
        Deck.initDeck();
        StartingCard s = Deck.drawCardFromStartingDeck();
        assertNotNull(s.getResourcesCentreBack());
        StartingCard start = Deck.getStartingCardsDeck().get(0);
    }

    @Test
    public void testConstructor() {
        StartingCard s = new StartingCard("backPath","frontPath");

        Deck.initDeck();
        StartingCard start = new StartingCard("back", "front");
        start.setCoordinates(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos());
    }

    @Test
    public void testGetResourceCenterFront() {
        Deck.initDeck();
        StartingCard start_1 = Deck.getStartingCardsDeck().get(0);
        StartingCard start_2 = Deck.getStartingCardsDeck().get(1);
        StartingCard start_3 = Deck.getStartingCardsDeck().get(3);
        StartingCard start_4 = Deck.getStartingCardsDeck().get(5);
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);

        MainTable main = new MainTable();
        main.setNumOfPlayers(4);
        PlayerTable player_1 = new PlayerTable(0, "name_1", new ResourceCard[]{});
        PlayerTable player_2 = new PlayerTable(1, "name_2", new ResourceCard[]{});
        PlayerTable player_3 = new PlayerTable(2, "name_3", new ResourceCard[]{});
        PlayerTable player_4 = new PlayerTable(3, "name_4", new ResourceCard[]{});

        main.setPlayerTable(0, player_1);
        main.setPlayerStartingCard(0,start_1);
        main.setPlayerSecretObjective(0,obj);

        main.setPlayerTable(1, player_2);
        main.setPlayerStartingCard(1,start_2);
        main.setPlayerSecretObjective(1,obj);

        start_3.setFront(true);
        main.setPlayerTable(2, player_3);
        main.setPlayerStartingCard(2,start_3);
        main.setPlayerSecretObjective(2,obj);

        main.setPlayerTable(3, player_4);
        main.setPlayerStartingCard(3,start_4);
        main.setPlayerSecretObjective(3,obj);
    }
    @Test
    public void testResourcesCentreBackToString() {
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(0);
        assertEquals("   \u001B[0;35mI\u001B[0m   ", start.resourcesCentreBackToString());
        Deck.initDeck();
        StartingCard start_2 = Deck.getStartingCardsDeck().get(2);
        assertEquals("  \u001B[0;31mF\u001B[0m\u001B[0;32mP\u001B[0m  ", start_2.resourcesCentreBackToString());
        Deck.initDeck();
        StartingCard start_3 = Deck.getStartingCardsDeck().get(4);
        assertEquals("  \u001B[0;34mA\u001B[0m\u001B[0;35mI\u001B[0m\u001B[0;32mP\u001B[0m  ", start_3.resourcesCentreBackToString());
    }

}
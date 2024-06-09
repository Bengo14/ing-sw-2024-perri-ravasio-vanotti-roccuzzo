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
        Deck.initDeck();
        StartingCard start = new StartingCard("back", "front");
        start.setCoordinates(PlayerTable.getStartingCardPos(),PlayerTable.getStartingCardPos());
    }
    @Test
    public void testGetResourceCenterFront() {
        Deck.initDeck();
        StartingCard start = Deck.getStartingCardsDeck().get(2);
        Objectives obj = Deck.getObjectiveCardsDeck().get(0);
        MainTable main = new MainTable();
        main.setNumOfPlayers(2);
        PlayerTable player = new PlayerTable(1, "name", new ResourceCard[]{});
        main.setPlayerTable(1,player);

        main.setPlayerStartingCard(1,start);
        main.setPlayerSecretObjective(1,obj);

        System.out.println(player.getResourceCounter(0));
        System.out.println(player.getResourceCounter(1));
        System.out.println(player.getResourceCounter(2));
        System.out.println(player.getResourceCounter(3));

    }

}

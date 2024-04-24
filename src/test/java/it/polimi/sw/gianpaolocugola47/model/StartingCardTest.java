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
        start.setCoordinates(29,29);
    }

}

package it.polimi.sw.gianpaolocugola47.model;


import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class StartingCardTest {
    @Test
    public void testGetResourceCenterBack() {
        Deck.initDeck();
        StartingCard s = Deck.drawCardFromStartingDeck();
        assertNotNull(s.getResourcesCentreBack());
    }
}

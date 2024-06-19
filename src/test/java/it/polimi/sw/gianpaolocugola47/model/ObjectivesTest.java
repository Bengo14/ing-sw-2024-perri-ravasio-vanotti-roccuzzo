package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectivesTest {
    @Test
    public void testConstructorAndGetterObjectives() {
        Deck.initDeck();
        Objectives o = Deck.getObjectiveCardsDeck().get(13);
        assertNotNull(o);
        assertEquals(2, o.getPoints());
        assertEquals("imgPathFrn", o.getImgPathFront());
        assertEquals("imgPathBck", o.getImgPathBack());
        assertEquals("2 MANUSCRIPT \nPoints: 2",o.getDescription());
    }
}
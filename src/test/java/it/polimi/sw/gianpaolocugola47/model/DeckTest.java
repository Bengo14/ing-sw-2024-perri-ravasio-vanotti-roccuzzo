package it.polimi.sw.gianpaolocugola47.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    public void testDrawCardFromResourceDeck() {
        Deck.initDeck();
        ResourceCard card = Deck.drawCardFromResourceDeck();
        assertNotNull(card);
    }

    @Test
    public void testDrawCardFromGoldDeck() {
        Deck.initDeck();
        GoldCard card = Deck.drawCardFromGoldDeck();
        assertNotNull(card);
    }

    @Test
    public void testDrawCardFromStartingDeck() {
        Deck.initDeck();
        StartingCard card = Deck.drawCardFromStartingDeck();
        assertNotNull(card);
    }

    @Test
    public void testDrawCardFromObjectiveDeck() {
        Deck.initDeck();
        Objectives card = Deck.drawCardFromObjectivesDeck();
        assertNotNull(card);
    }

    @Test
    public void testGetGoldCardsDeck() {
        Deck.initDeck();
        assertNotNull(Deck.getGoldCardsDeck());
        assertFalse(Deck.getGoldCardsDeck().isEmpty());
    }

    @Test
    public void testGetResourceCardsDeck() {
        Deck.initDeck();
        assertNotNull(Deck.getResourceCardsDeck());
        assertFalse(Deck.getResourceCardsDeck().isEmpty());
    }

    @Test
    public void testGetStartingCardsDeck() {
        Deck.initDeck();
        assertNotNull(Deck.getStartingCardsDeck());
        assertFalse(Deck.getStartingCardsDeck().isEmpty());
    }

    @Test
    public void testGetObjectiveCardsDeck() {
        Deck.initDeck();
        assertNotNull(Deck.getObjectiveCardsDeck());
        assertFalse(Deck.getObjectiveCardsDeck().isEmpty());
    }

    @Test
    public void testAreDecksEmpty() {
        Deck.initDeck();
        Deck.areDecksEmpty();
        Deck.getStartingCardsDeck().clear();
        Deck.getResourceCardsDeck().clear();
        Deck.getGoldCardsDeck().clear();
        Deck.getObjectiveCardsDeck().clear();
        assertEquals(null, Deck.drawCardFromResourceDeck());
        assertEquals(null, Deck.drawCardFromGoldDeck());
        assertEquals(null, Deck.drawCardFromStartingDeck());
        assertEquals(null, Deck.drawCardFromObjectivesDeck());
        assertTrue(Deck.areDecksEmpty());
    }

    @Test
    public void testResourceCardOnTop() {
        Deck.initDeck();
        assertNotNull(Deck.getResourceCardOnTop());
    }
    @Test
    public void testGoldCardOnTop () {
        Deck.initDeck();
        assertNotNull(Deck.getGoldCardOnTop());
    }

    @Test
    public void testGetCardFromGivenId() {
        Deck.initDeck();
        assertNotNull(Deck.getCardFromGivenId(1));
        assertEquals(Deck.getCardFromGivenId(1).getId(),1);
        System.out.println(Deck.getCardFromGivenId(1).toString());
        assertNull(Deck.getCardFromGivenId(1000));
        assertNull(Deck.getCardFromGivenId(0));
        assertNull(Deck.getCardFromGivenId(100));
    }

    @Test
    public void testGetObjectiveCardFromGivenId(){
        Deck.initDeck();
        assertNull(Deck.getObjectiveCardFromGivenId(1));
        assertNull(Deck.getObjectiveCardFromGivenId(1000));
        assertNull(Deck.getObjectiveCardFromGivenId(0));
        assertNotNull(Deck.getObjectiveCardFromGivenId(100));
        assertEquals(Deck.getObjectiveCardFromGivenId(100).getId(),100);
        System.out.println(Deck.getObjectiveCardFromGivenId(100).toString());
    }

}





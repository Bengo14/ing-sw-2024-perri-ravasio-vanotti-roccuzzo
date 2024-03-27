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




}
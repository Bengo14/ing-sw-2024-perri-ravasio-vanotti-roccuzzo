package it.polimi.sw.gianpaolocugola47.utils;

import it.polimi.sw.gianpaolocugola47.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSaverTest {
    @Test
    void generateDeckStatusJson() {
        // Test for when the game hasn't started yet
        GameSaver gameSaver = new GameSaver(null);
        Deck.initAndShuffleDeck();
        try{
            assertTrue(gameSaver.generateDeckStatusJson());
        }catch(IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    void loadDeckStatus(){
        Deck.initAndShuffleDeck();
        List<GoldCard> goldDeck = Deck.getGoldCardsDeck();
        List<ResourceCard> resDeck = Deck.getResourceCardsDeck();
        List<StartingCard> startDeck = Deck.getStartingCardsDeck();
        List<Objectives> objDeck = Deck.getObjectiveCardsDeck();
        GameSaver gameSaver = new GameSaver(null);
        try{
            gameSaver.generateDeckStatusJson();
        }catch(IOException e) {
            e.printStackTrace();
        }
        gameSaver.loadDeckStatus();
        for(int i = 0; i < goldDeck.size(); i++){
            assertEquals(goldDeck.get(i).getId(), Deck.getGoldCardsDeck().get(i).getId());
        }
        for(int i = 0; i < resDeck.size(); i++){
            assertEquals(resDeck.get(i).getId(), Deck.getResourceCardsDeck().get(i).getId());
        }
        for(int i = 0; i < startDeck.size(); i++){
            assertEquals(startDeck.get(i).getId(), Deck.getStartingCardsDeck().get(i).getId());
        }
        for(int i = 0; i < objDeck.size(); i++){
            assertEquals(objDeck.get(i).getId(), Deck.getObjectiveCardsDeck().get(i).getId());
        }
    }
}
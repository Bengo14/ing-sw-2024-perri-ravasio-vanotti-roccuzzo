package it.polimi.sw.gianpaolocugola47.utils;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameSaverTest {
    @Test
    void generateDeckStatusJson() {
        // Test for when the game hasn't started yet
        GameSaver gameSaver = new GameSaver(null);
        Deck.initAndShuffleDeck();
        assertTrue(gameSaver.generateDeckStatusJson());
        //assertTrue(gameSaver.resetFiles());
    }
    @Test
    void loadPlayerTableStatusJson(){
        // Test for when the game hasn't started yet
        Controller game = new Controller();
        game.setNumOfPlayers(2); // Set the number of players to 2
        GameSaver gameSaver = new GameSaver(game);
        Controller c = gameSaver.loadControllerStatus();
        System.out.println(Arrays.toString(c.getMainTable().getGlobalPoints()));
        System.out.println(Arrays.toString(c.getMainTable().getBoardPoints()));
        for(Objectives o : c.getMainTable().getGlobalObjectives()){
            System.out.println(o.getDescription());
        }
        for(ResourceCard r : c.getMainTable().getPlayersTables()[0].getCardsOnHand()){
            System.out.println(r.getId());
        }
        for(ResourceCard r : c.getMainTable().getPlayersTables()[1].getCardsOnHand()){
            System.out.println(r.getId());
        }
        //assertTrue(gameSaver.resetFiles());
    }

    @Test
    void loadDeckStatus(){
        Deck.initAndShuffleDeck();
        List<GoldCard> goldDeck = Deck.getGoldCardsDeck();
        List<ResourceCard> resDeck = Deck.getResourceCardsDeck();
        List<StartingCard> startDeck = Deck.getStartingCardsDeck();
        List<Objectives> objDeck = Deck.getObjectiveCardsDeck();
        GameSaver gameSaver = new GameSaver(null);
        gameSaver.generateDeckStatusJson();
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

        assertEquals(goldDeck.size(), Deck.getGoldCardsDeck().size());
        assertEquals(resDeck.size(), Deck.getResourceCardsDeck().size());
        assertEquals(startDeck.size(), Deck.getStartingCardsDeck().size());
        assertEquals(objDeck.size(), Deck.getObjectiveCardsDeck().size());
        assertEquals(goldDeck.getFirst().getId(),Deck.getGoldCardsDeck().getFirst().getId());
        assertEquals(resDeck.getFirst().getId(),Deck.getResourceCardsDeck().getFirst().getId());
        System.out.println("Get First Gold Card Deck: " + Deck.getGoldCardsDeck().getFirst().getId() + " Get gold card on top: " + Deck.getGoldCardOnTop().getId());
        System.out.println("Get First Gold Card Deck: " + Deck.getGoldCardsDeck().getLast().getId() + " Get gold card on top: " + Deck.getGoldCardOnTop().getId());
        assertEquals(goldDeck.getLast().getId(),Deck.getGoldCardOnTop().getId());
        assertEquals(resDeck.getLast().getId(),Deck.getResourceCardOnTop().getId());
        //assertTrue(gameSaver.resetFiles());
    }
}
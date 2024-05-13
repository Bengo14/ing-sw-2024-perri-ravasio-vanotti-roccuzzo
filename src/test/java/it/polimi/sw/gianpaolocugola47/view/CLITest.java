package it.polimi.sw.gianpaolocugola47.view;

import org.junit.jupiter.api.Test;
import it.polimi.sw.gianpaolocugola47.model.*;

import static org.junit.jupiter.api.Assertions.*;

class CLITest {

    @Test
    void printResourceCard() {
        Deck.initAndShuffleDeck();
        ResourceCard resourceCard = Deck.getResourceCardOnTop();
        System.out.println("\nCard id: " + resourceCard.getId());
        CLI cli = new CLI();
        cli.printResourceCard(resourceCard);
        System.out.println("");
        resourceCard.switchFrontBack();
        cli.printResourceCard(resourceCard);
    }

    @Test
    void printGoldCard() {
        Deck.initAndShuffleDeck();
        GoldCard goldCard = Deck.getGoldCardOnTop();
        System.out.println("\nCard id: " + goldCard.getId());
        CLI cli = new CLI();
        cli.printGoldCard(goldCard);
        System.out.println("");
        goldCard.switchFrontBack();
        cli.printGoldCard(goldCard);
    }

    @Test
    void printStartingCard() {
        Deck.initAndShuffleDeck();
        StartingCard startingCard = Deck.getStartingCardsDeck().getFirst();
        System.out.println("\nCard id: " + startingCard.getId());
        CLI cli = new CLI();
        cli.printStartingCard(startingCard);
        System.out.println("");
        startingCard.switchFrontBack();
        cli.printStartingCard(startingCard);
    }
    @Test
    void printObjectiveCard() {
        Deck.initAndShuffleDeck();
        Objectives objectiveCard = Deck.getObjectiveCardsDeck().getFirst();
        CLI cli = new CLI();
        cli.printObjectiveCard(objectiveCard);
    }

    @Test
    void printResourceCounter(){
        int[] counter = new int[8];
        counter[0] = 1;
        counter[1] = 2;
        counter[2] = 3;
        counter[3] = 4;
        counter[4] = 5;
        counter[5] = 6;
        counter[6] = 7;
        counter[7] = 8;
        CLI cli = new CLI();
        cli.printResourceCounter(counter);
    }
}
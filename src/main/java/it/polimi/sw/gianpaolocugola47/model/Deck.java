package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {

    private static List<GoldCard> goldCardsDeck;
    private static List<ResourceCard> resourceCardsDeck;
    private static List<StartingCard> startingCardsDeck;
    private static List<Objectives> objectiveCardsDeck;
    private static Random randomGenerator;
    private static int[] cardsInDeckCounters;

    public static void initDeck(){
        generateResourceCardsDeck();
        generateGoldCardsDeck();
        generateObjectiveCardsDeck();
        generateStartingCardsDeck();
        randomGenerator = new Random(System.currentTimeMillis());
        cardsInDeckCounters = new int[]{40,40,16,6};
    }
    private static void generateResourceCardsDeck() {
        resourceCardsDeck = new ArrayList<ResourceCard>();
        /*todo*/
    }
    private static void generateGoldCardsDeck() {
        goldCardsDeck = new ArrayList<GoldCard>();
        /*todo*/
    }
    private static void generateObjectiveCardsDeck(){
        objectiveCardsDeck = new ArrayList<Objectives>();
        /*todo*/
    }
    private static void generateStartingCardsDeck(){
        startingCardsDeck = new ArrayList<StartingCard>();
        /*todo*/
    }
    protected static ResourceCard drawCardFromResourceDeck(){
        int position = randomGenerator.nextInt(cardsInDeckCounters[0]);
        ResourceCard card = resourceCardsDeck.remove(position);
        cardsInDeckCounters[0]--;
        return card;
    }
    protected static GoldCard drawCardFromGoldDeck(){
        int position = randomGenerator.nextInt(cardsInDeckCounters[1]);
        GoldCard card = goldCardsDeck.remove(position);
        cardsInDeckCounters[1]--;
        return card;
    }
    protected static Objectives drawCardFromObjectivesDeck(){
        int position = randomGenerator.nextInt(cardsInDeckCounters[2]);
        Objectives objective = objectiveCardsDeck.remove(position);
        cardsInDeckCounters[2]--;
        return objective;
    }
    protected static StartingCard drawCardFromStartingDeck(){
        int position = randomGenerator.nextInt(cardsInDeckCounters[3]);
        StartingCard card = startingCardsDeck.remove(position);
        cardsInDeckCounters[3]--;
        return card;
    }
}

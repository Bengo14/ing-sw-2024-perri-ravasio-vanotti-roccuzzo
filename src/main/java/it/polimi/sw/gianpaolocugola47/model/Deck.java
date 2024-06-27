package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.utils.ObjectiveDeserializer;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * This class represents the deck of cards.
 * It contains the methods to initialize and shuffle the deck, to draw a card from the deck and to get the card on top of the deck.
 */
public class Deck {

    private static List<GoldCard> goldCardsDeck;
    private static List<ResourceCard> resourceCardsDeck;
    private static List<StartingCard> startingCardsDeck;
    private static List<Objectives> objectiveCardsDeck;
    private static Random randomGenerator;
    private static final HashMap<Integer, PlaceableCard> placeableCardIdMap = new HashMap<>();
    private static final HashMap<Integer, Objectives> objectiveCardIdMap = new HashMap<>();

    /**
     * This method initializes the deck and shuffles it.
     * */
    public static void initAndShuffleDeck() {
        initDeck();
        shuffleDeck();
    }

    /**
     * This method initializes the deck.
     * HashMaps with the card id as key and the card itself as value are also initialized here.
     * The latter are later used for network communication purposes.
     * At last, this method generates the seed used to randomize the shuffle process.
     * */
    public static void initDeck() {
        randomGenerator = new Random(System.currentTimeMillis());
        generateStartingCardsDeck();
        generateGoldCardsDeck();
        generateResourceCardsDeck();
        generateObjectiveCardsDeck();
        initHashMaps();
    }

    /**
     * This method shuffles the deck using the previously initialized seed.
     * */
    private static void shuffleDeck() {
        Collections.shuffle(goldCardsDeck,randomGenerator);
        Collections.shuffle(resourceCardsDeck,randomGenerator);
        Collections.shuffle(startingCardsDeck,randomGenerator);
        Collections.shuffle(objectiveCardsDeck,randomGenerator);
    }

    /**
     * This method initializes the hashmaps that map the card id to the card itself.
     * */
    private static void initHashMaps(){
        for (GoldCard goldCard : goldCardsDeck) {
            placeableCardIdMap.put(goldCard.getId(),goldCard);
        }
        for (ResourceCard resourceCard : resourceCardsDeck) {
            placeableCardIdMap.put(resourceCard.getId(),resourceCard);
        }
        for (StartingCard startingCard : startingCardsDeck) {
            placeableCardIdMap.put(startingCard.getId(),startingCard);
        }
        for (Objectives objectives : objectiveCardsDeck) {
            objectiveCardIdMap.put(objectives.getId(),objectives);
        }
    }

    /**
     * This method generates the resource cards deck by parsing the cards from the respective json file.
     * */
    private static void generateResourceCardsDeck() {
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<ResourceCard>>() {}.getType();
            InputStream is = Deck.class.getResourceAsStream("/it/polimi/sw/gianpaolocugola47/resourceCards.json");
            if (is == null) {
                throw new FileNotFoundException("Cannot find resource file " + "/it/polimi/sw/gianpaolocugola47/resourceCards.json");
            }
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            resourceCardsDeck = gson.fromJson(reader, listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method generates the gold cards deck by parsing the cards from the respective json file.
     * */
    private static void generateGoldCardsDeck() {
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<GoldCard>>() {}.getType();
            InputStream is = Deck.class.getResourceAsStream("/it/polimi/sw/gianpaolocugola47/goldCards.json");
            if (is == null) {
                throw new FileNotFoundException("Cannot find resource file " + "/it/polimi/sw/gianpaolocugola47/goldCards.json");
            }
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            goldCardsDeck = gson.fromJson(reader, listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method generates the objective cards deck by parsing the cards from the respective json file.
     * The deserializer is used to parse the different types of objectives, respecting inheritance.
     * */
    private static void generateObjectiveCardsDeck(){
        ObjectiveDeserializer deserializer = new ObjectiveDeserializer("type");
        deserializer.registerObjectiveType("ItemObjective", ItemObjective.class);
        deserializer.registerObjectiveType("ResourceObjective",ResourceObjective.class);
        deserializer.registerObjectiveType("LShapePatternObjective",LShapePatternObjective.class);
        deserializer.registerObjectiveType("DiagonalPatternObjective",DiagonalPatternObjective.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Objectives.class, deserializer)
                .create();
        try {
            InputStream is = Deck.class.getResourceAsStream("/it/polimi/sw/gianpaolocugola47/objectives.json");
            if (is == null) {
                throw new FileNotFoundException("Cannot find resource file " + "/it/polimi/sw/gianpaolocugola47/objectives.json");
            }
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            objectiveCardsDeck =  gson.fromJson(reader, new TypeToken<List<Objectives>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method generates the starting cards deck by parsing the cards from the respective json file.
     * */
    private static void generateStartingCardsDeck(){
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<StartingCard>>() {}.getType();
            InputStream is = Deck.class.getResourceAsStream("/it/polimi/sw/gianpaolocugola47/startingCards.json");
            if (is == null) {
                throw new FileNotFoundException("Cannot find resource file " + "/it/polimi/sw/gianpaolocugola47/startingCards.json");
            }
            InputStreamReader reader = new InputStreamReader(is, StandardCharsets.UTF_8);
            startingCardsDeck = gson.fromJson(reader, listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method draws a card and removes it from the resource deck.
     * @return the drawn card, null if the deck is empty.
     * */
    protected static ResourceCard drawCardFromResourceDeck(){
        if(resourceCardsDeck.isEmpty())
            return null;
        resourceCardsDeck.getLast().setFront(true);
        return resourceCardsDeck.removeLast();
    }

    /**
     * This method draws a card  and removes it from the gold deck.
     * @return the drawn card, null if the deck is empty.
     * */
    protected static GoldCard drawCardFromGoldDeck(){
        if(goldCardsDeck.isEmpty())
            return null;
        goldCardsDeck.getLast().setFront(true);
        return goldCardsDeck.removeLast();
    }

    /**
     * This method draws a card  and removes it from the objective deck.
     * @return the drawn card, null if the deck is empty.
     * */
    protected static Objectives drawCardFromObjectivesDeck(){
        if(objectiveCardsDeck.isEmpty())
            return null;
        return objectiveCardsDeck.removeLast();
    }

    /**
     * This method draws a card  and removes it from the starting deck.
     * @return the drawn card, null if the deck is empty.
     * */
    public static StartingCard drawCardFromStartingDeck(){
        if(startingCardsDeck.isEmpty())
            return null;
        return startingCardsDeck.removeLast();
    }

    /**
     * This method returns the gold cards deck.
     * @return the gold cards deck.
     * */
    public static List<GoldCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }

    /**
     * This method returns the resource cards deck.
     * @return the resource cards deck.
     * */
    public static List<ResourceCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }

    /**
     * This method returns the starting cards deck.
     * @return the starting cards deck.
     * */
    public static List<StartingCard> getStartingCardsDeck() {
        return startingCardsDeck;
    }

    /**
     * This method returns the objective cards deck.
     * @return the objective cards deck.
     * */
    public static List<Objectives> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }

    /**
     * This method check if the decks are empty.
     * @return true if the decks are empty, false otherwise.
     * */
    protected static boolean areDecksEmpty() {
        return goldCardsDeck.isEmpty() && resourceCardsDeck.isEmpty();
    }

    /**
     * This method returns the gold card on top of the deck.
     * @return the gold card on top of the deck, null if the deck is empty.
     * */
    public static GoldCard getGoldCardOnTop() {
        if(!goldCardsDeck.isEmpty())
            return goldCardsDeck.getLast();
        else return null;
    }

    /**
     * This method returns the resource card on top of the deck.
     * @return the resource card on top of the deck.
     * */
    public static ResourceCard getResourceCardOnTop() {
        if(!resourceCardsDeck.isEmpty())
            return resourceCardsDeck.getLast();
        else return null;
    }

    /**
     * This method returns the card from id.
     * @param id the id of the card.
     * @return the card with that id.
     * */
    public static PlaceableCard getCardFromGivenId(int id){
        try{
            return placeableCardIdMap.get(id);
        }catch (NullPointerException e){
            return null;
        }
    }

    /**
     * This method returns the objective card from id.
     * @param id the id of the objective card.
     * @return the objective card with that id.
     * */
    public static Objectives getObjectiveCardFromGivenId(int id){
        try{
            return objectiveCardIdMap.get(id);
        }catch (NullPointerException e){
            return null;
        }
    }

    /**
     * This method sets the gold cards deck. Used when parsing a previous match from a json file.
     * @param goldCardsDeck the gold cards deck.
     * */
    public static void setGoldCardsDeck(List<GoldCard> goldCardsDeck) {
        Deck.goldCardsDeck = goldCardsDeck;
    }

    /**
     * This method sets the resource cards deck. Used when parsing a previous match from a json file.
     * @param resourceCardsDeck the resource cards deck.
     * */
    public static void setResourceCardsDeck(List<ResourceCard> resourceCardsDeck) {
        Deck.resourceCardsDeck = resourceCardsDeck;
    }

    /**
     * This method sets the starting cards deck. Used when parsing a previous match from a json file.
     * @param startingCardsDeck the starting cards deck.
     * */
    public static void setStartingCardsDeck(List<StartingCard> startingCardsDeck) {
        Deck.startingCardsDeck = startingCardsDeck;
    }

    /**
     * This method sets the objective cards deck. Used when parsing a previous match from a json file.
     * @param objectiveCardsDeck the objective cards deck.
     * */
    public static void setObjectiveCardsDeck(List<Objectives> objectiveCardsDeck) {
        Deck.objectiveCardsDeck = objectiveCardsDeck;
    }
}
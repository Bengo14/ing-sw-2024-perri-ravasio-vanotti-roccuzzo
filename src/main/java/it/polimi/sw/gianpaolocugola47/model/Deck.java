package it.polimi.sw.gianpaolocugola47.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.utils.ObjectiveDeserializer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

    private static List<GoldCard> goldCardsDeck;
    private static List<ResourceCard> resourceCardsDeck;
    private static List<StartingCard> startingCardsDeck;
    private static List<Objectives> objectiveCardsDeck;
    private static Random randomGenerator;
    private static final int FIRST_OBJECTIVE_ID = 87;
    private static final int FIRST_GOLD_ID = 41;
    private static final int FIRST_RESOURCE_ID = 1;
    private static final int FIRST_STARTING_ID = 81;

    public static void initAndShuffleDeck() {
        initDeck();
        shuffleDeck();
    }
    public static void initDeck() {
        randomGenerator = new Random(System.currentTimeMillis());
        generateStartingCardsDeck();
        generateGoldCardsDeck();
        generateResourceCardsDeck();
        generateObjectiveCardsDeck();
    }
    private static void shuffleDeck() {
        Collections.shuffle(goldCardsDeck,randomGenerator);
        Collections.shuffle(resourceCardsDeck,randomGenerator);
        Collections.shuffle(startingCardsDeck,randomGenerator);
        Collections.shuffle(objectiveCardsDeck,randomGenerator);
    }

    private static void generateResourceCardsDeck() {
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<ResourceCard>>() {}.getType();
            resourceCardsDeck = gson.fromJson(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/resourceCards.json"),listOfCards);
        }
        catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void generateGoldCardsDeck() {
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<GoldCard>>() {}.getType();
            goldCardsDeck = gson.fromJson(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/goldCards.json"),listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }
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
            objectiveCardsDeck =  gson.fromJson(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/objectives.json"),new TypeToken<List<Objectives>>(){}.getType());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    private static void generateStartingCardsDeck(){
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<StartingCard>>() {}.getType();
            startingCardsDeck = gson.fromJson(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/startingCards.json"),listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected static ResourceCard drawCardFromResourceDeck(){
        if(resourceCardsDeck.isEmpty())
            return null;
        return resourceCardsDeck.removeLast();
    }
    protected static GoldCard drawCardFromGoldDeck(){
        if(goldCardsDeck.isEmpty())
            return null;
        return goldCardsDeck.removeLast();
    }
    protected static Objectives drawCardFromObjectivesDeck(){
        if(objectiveCardsDeck.isEmpty())
            return null;
        return objectiveCardsDeck.removeLast();
    }
    public static StartingCard drawCardFromStartingDeck(){
        if(startingCardsDeck.isEmpty())
            return null;
        return startingCardsDeck.removeLast();
    }
    public static List<GoldCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }
    public static List<ResourceCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }
    public static List<StartingCard> getStartingCardsDeck() {
        return startingCardsDeck;
    }
    public static List<Objectives> getObjectiveCardsDeck() {
        return objectiveCardsDeck;
    }
    protected static boolean areDecksEmpty() {
        return goldCardsDeck.isEmpty() && resourceCardsDeck.isEmpty();
    }
    public static GoldCard getGoldCardOnTop() {
        if(!goldCardsDeck.isEmpty())
            return goldCardsDeck.getLast();
        else return null;
    }
    public static ResourceCard getResourceCardOnTop() {
        if(!resourceCardsDeck.isEmpty())
            return resourceCardsDeck.getLast();
        else return null;
    }

    public static PlaceableCard getCardFromGivenId(int id){
        if(id < FIRST_OBJECTIVE_ID){
            if (id >= FIRST_RESOURCE_ID && id < FIRST_GOLD_ID){
                for(ResourceCard card : resourceCardsDeck)
                    if(card.getId() == id)
                        return card;
            }
            else if(id >= FIRST_GOLD_ID && id < FIRST_STARTING_ID){
                for(GoldCard card : goldCardsDeck)
                    if(card.getId() == id)
                        return card;
            }
            else{
                for(StartingCard card : startingCardsDeck)
                    if(card.getId() == id)
                        return card;
            }
        }
        return null;
    }

    public static Objectives getObjectiveCardFromGivenId(int id){
        for(Objectives card : objectiveCardsDeck)
            if(card.getId() == id)
                return card;
        return null;
    }
}

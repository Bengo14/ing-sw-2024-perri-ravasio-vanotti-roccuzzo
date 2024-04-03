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
import java.util.List;
import java.util.Random;

public class Deck {

    private static List<GoldCard> goldCardsDeck;
    private static List<ResourceCard> resourceCardsDeck;
    private static List<StartingCard> startingCardsDeck;
    private static List<Objectives> objectiveCardsDeck;
    private static Random randomGenerator;
    private static int[] cardsInDeckCounters;

    protected static void initDeck(){
        generateStartingCardsDeck();
        generateGoldCardsDeck();
        generateResourceCardsDeck();
        generateObjectiveCardsDeck();
        randomGenerator = new Random(System.currentTimeMillis());
        cardsInDeckCounters = new int[]{40,40,16,6};
    }
    private static void generateResourceCardsDeck() {
        Gson gson = new Gson();
        try {
            Type listOfCards = new TypeToken<ArrayList<ResourceCard>>() {}.getType();
            resourceCardsDeck = gson.fromJson(new FileReader("src/main/resources/it/polimi/sw/gianpaolocugola47/resourceCards.json"),listOfCards);
        } catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
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

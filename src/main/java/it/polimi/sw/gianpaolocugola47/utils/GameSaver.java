package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.exceptions.GameNotStartedException;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class GameSaver {
    private final MainTable game;
    private final Gson gson;
    private final String[] filePaths;

    public GameSaver(MainTable game) {
        this.game = game;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.filePaths = new String[5];
        this.filePaths[0] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/mainTableStatus.json";
        this.filePaths[1] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusResources.json";
        this.filePaths[2] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusGold.json";
        this.filePaths[3] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusStarting.json";
        this.filePaths[4] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusObjectives.json";
    }

    private void generateGameStatusJson() throws IOException {
        try {
            gson.toJson(this.game, new FileWriter(filePaths[0]));
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }

    public boolean generateDeckStatusJson() throws IOException {
        String s;
        DiagonalPatternObjectiveSerializer diagonalPatternObjectiveSerializer = new DiagonalPatternObjectiveSerializer();
        LShapePatternObjectiveSerializer lShapePatternObjectiveSerializer = new LShapePatternObjectiveSerializer();
        ItemObjectiveSerializer itemObjectiveSerializer = new ItemObjectiveSerializer();
        ResourceObjectiveSerializer resourceObjectiveSerializer = new ResourceObjectiveSerializer();
        Gson gsonDpo = new GsonBuilder()
                .registerTypeAdapter(DiagonalPatternObjective.class, diagonalPatternObjectiveSerializer)
                .create();
        Gson gsonLpo = new GsonBuilder()
                .registerTypeAdapter(LShapePatternObjective.class, lShapePatternObjectiveSerializer)
                .create();
        Gson gsonIo = new GsonBuilder()
                .registerTypeAdapter(ItemObjective.class, itemObjectiveSerializer)
                .create();
        Gson gsonRo = new GsonBuilder()
                .registerTypeAdapter(ResourceObjective.class, resourceObjectiveSerializer)
                .create();
        if(Deck.getGoldCardsDeck() == null && Deck.getObjectiveCardsDeck() == null &&
                Deck.getStartingCardsDeck() == null && Deck.getResourceCardsDeck() == null)
        {
            return false; //Game hasn't started yet!
        } else {
            try{
                Writer[] writer = new Writer[4];
                for(int i = 1; i < 5; i++) {
                    writer[i-1] = new FileWriter(filePaths[i]);
                }
                gson.toJson(Deck.getResourceCardsDeck(), writer[0]);
                writer[0].flush();
                writer[0].close();
                gson.toJson(Deck.getGoldCardsDeck(), writer[1]);
                writer[1].flush();
                writer[1].close();
                gson.toJson(Deck.getStartingCardsDeck(), writer[2]);
                writer[2].flush();
                writer[2].close();
                writer[3].append("[");
                for(Objectives obj : Deck.getObjectiveCardsDeck()) {
                    if(obj instanceof DiagonalPatternObjective) {
                        s = gsonDpo.toJson(obj);
                        writer[3].append(s);
                    } else if(obj instanceof LShapePatternObjective) {
                        s = gsonLpo.toJson(obj);
                        writer[3].append(s);
                    } else if(obj instanceof ItemObjective) {
                        s = gsonIo.toJson(obj);
                        writer[3].append(s);
                    } else if(obj instanceof ResourceObjective) {
                        s = gsonRo.toJson(obj);
                        writer[3].append(s);
                    }
                    if(!obj.equals(Deck.getObjectiveCardsDeck().getLast()))
                        writer[3].append(",\n");
                    writer[3].flush();
                }
                writer[3].append("]");
                writer[3].flush();
                writer[3].close();
            } catch(JsonIOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean loadDeckStatus(){
        Reader[] reader = new Reader[4];
        Type listOfCards;
        for(int i = 1; i < 5; i++) {
            try {
                reader[i-1] = new FileReader(filePaths[i]);
            } catch (FileNotFoundException e) {
                System.out.println("File were not found, unable to load the game status.");
                return false;
            }
        }
        listOfCards = new TypeToken<ArrayList<ResourceCard>>() {}.getType();
        Deck.setResourceCardsDeck(gson.fromJson(reader[0], listOfCards));
        listOfCards = new TypeToken<ArrayList<GoldCard>>() {}.getType();
        Deck.setGoldCardsDeck(gson.fromJson(reader[1], listOfCards));
        listOfCards = new TypeToken<ArrayList<StartingCard>>() {}.getType();
        Deck.setStartingCardsDeck(gson.fromJson(reader[2], listOfCards));
        ObjectiveDeserializer deserializer = new ObjectiveDeserializer("type");
        deserializer.registerObjectiveType("ItemObjective", ItemObjective.class);
        deserializer.registerObjectiveType("ResourceObjective",ResourceObjective.class);
        deserializer.registerObjectiveType("LShapePatternObjective",LShapePatternObjective.class);
        deserializer.registerObjectiveType("DiagonalPatternObjective",DiagonalPatternObjective.class);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Objectives.class, deserializer)
                .create();
        listOfCards = new TypeToken<ArrayList<Objectives>>() {}.getType();
        Deck.setObjectiveCardsDeck(gson.fromJson(reader[3], listOfCards));
        for(int i = 0; i < 4; i++) {
            try {
                reader[i].close();
            } catch (IOException e) {
                System.out.println("Unable to close the file reader.");
                return false;
            }
        }
        return true;
    }
    public void saveGameStatus() throws GameNotStartedException{
        try{
            if(!generateDeckStatusJson())
                throw new GameNotStartedException("The game has not started yet, decks are empty.");
            generateGameStatusJson();
        } catch (IOException e) {
            System.out.println("Unable to save the current game status onto the respective files.");
        }
    }
}

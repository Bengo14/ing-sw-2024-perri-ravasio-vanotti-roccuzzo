package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {
    private Controller game; //goldCardOnTop and resCardOnTop are not 'MainTable' attributes, yet can be easily found once deck is loaded
    private final Gson gson;
    public final static String CONTROLLER_FILE_PATH = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/controllerStatus.json";
    private final String[] deckFilePaths;
    private final List<String> boardFilePaths;

    public GameSaver(Controller game) {
        this.game = game;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.boardFilePaths = new ArrayList<>(); //file below has to take nicknames into consideration
        this.boardFilePaths.add(CONTROLLER_FILE_PATH); //controller & main table status
        this.initPlayerTableFiles();
        this.deckFilePaths = new String[4];
        this.deckFilePaths[0] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusResources.json";
        this.deckFilePaths[1] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusGold.json";
        this.deckFilePaths[2] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusStarting.json";
        this.deckFilePaths[3] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatusObjectives.json";
    }
    private void initPlayerTableFiles(){ //even when game ends, file's paths keep getting generated with the original names, since players have to join with their original name
        if(game != null) {
            for(int i = 0; i < game.getNumOfPlayers(); i++) { //playerTable excluding board
                this.boardFilePaths.add("src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/playerTableStatus" + i +".json");
            }
        }
    }
    public boolean generateGameStatusJson(){
        ControllerSerializer controllerSerializer = new ControllerSerializer();
        Gson gsonContr = new GsonBuilder()
                .registerTypeAdapter(Controller.class, controllerSerializer)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        PlayerTableSerializer playerTableSerializer = new PlayerTableSerializer();
        Gson gsonPlayer = new GsonBuilder()
                .registerTypeAdapter(PlayerTable.class, playerTableSerializer)
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        PlayerTable[] pt = game.getMainTable().getPlayersTables();
        try{
            Writer writer = new FileWriter(boardFilePaths.getFirst());
            gsonContr.toJson(game, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            System.out.println("Couldn't save controller status.");
            return false;
        } try {
            for(int i = 1; i < boardFilePaths.size(); i++) {
                Writer ptWr = new FileWriter(boardFilePaths.get(i));
                gsonPlayer.toJson(pt[i-1], ptWr);
                ptWr.flush();
                ptWr.close();
            }
        } catch (IOException e) {
            System.out.println("Couldn't save player table status.");
            return false;
        } catch(StackOverflowError e){
            System.out.println("Stack overflow error.");
            return false;
        }
        return true;
    }
    public Controller loadControllerStatus(){
        Reader reader;
        Type controller;
        try {
            reader = new FileReader(boardFilePaths.getFirst());
        } catch (FileNotFoundException e) {
            System.out.println("File were not found, unable to load the game status.");
            return null;
        }
        controller = new TypeToken<Controller>() {}.getType();
        Gson controllerGson = new GsonBuilder()
                .registerTypeAdapter(Controller.class, new ControllerDeserializer())
                .create();
        game = controllerGson.fromJson(reader, controller);
        try{
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to close the file reader.");
        }
        if(game != null){
            PlayerTable[] pt = loadPlayerTableStatus();
            if(pt != null)
                game.getMainTable().setPlayersTables(pt);
        }
        return game; //game has to be updated in the controller
    }

    public PlayerTable[] loadPlayerTableStatus(){
        Reader[] reader = new Reader[boardFilePaths.size()-1];
        Type playerTable;
        PlayerTable[] pt = new PlayerTable[boardFilePaths.size()-1];
        Gson ptGson = new GsonBuilder()
                .registerTypeAdapter(PlayerTable.class, new PlayerTableDeserializer())
                .create();

        System.err.println("\n");
        for(int i = 0; i < boardFilePaths.size()-1; i++) {
            try {
                System.err.println(boardFilePaths.get(i+1));
                reader[i] = new FileReader(boardFilePaths.get(i+1));
            } catch (FileNotFoundException e) {
                System.out.println("File were not found, unable to load the game status.");
                return null;
            } try{
                pt[i] = ptGson.fromJson(reader[i], new TypeToken<PlayerTable>() {}.getType());
                reader[i].close();
            } catch (IOException e) {
                System.out.println("Unable to close the file reader.");
                return null;
            }
        }
        for(PlayerTable p : pt)
            p.idMatrixToCardMatrix();
        return pt;
    }

    public boolean generateDeckStatusJson()  {
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
                for(int i = 0; i < 4; i++) {
                    writer[i] = new FileWriter(deckFilePaths[i]);
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
            } catch(JsonIOException | IOException e)  {
                System.out.println("Couldn't save deck status.");
                return false;
            }
        }
        return true;
    }
    public boolean loadDeckStatus(){
        Reader[] reader = new Reader[4];
        Type listOfCards;
        for(int i = 0; i < 4; i++) {
            try {
                reader[i] = new FileReader(deckFilePaths[i]);
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
    public boolean resetFiles(){
        boolean correctDeletions = true;
        for(String path : deckFilePaths) {
            File f = new File(path);
            if(f.exists()) {
                if(!f.delete()){
                    correctDeletions = false;
                }
            }
        }
        for(String path : boardFilePaths) {
            File f = new File(path);
            if(f.exists()) {
                if(!f.delete()){
                    correctDeletions = false;
                }
            }
        }
        return correctDeletions;
    }
    public void updateControllerStatus(Controller game){
        this.game = game;
    }

    public boolean checkIfRestarted(String[] nicknames){
        Reader reader;
        Controller lc;
        try {
            reader = new FileReader(CONTROLLER_FILE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File were not found, unable to load the game status.");
            return false;
        }
        /*todo: add nickname controls*/
        try{
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to close the file reader.");
        }
        return false;
    }
}

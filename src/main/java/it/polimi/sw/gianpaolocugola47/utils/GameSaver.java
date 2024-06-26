package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.model.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to save the current status of the game by the controller.
 * It saves the controller status onto 1 + numOfPlayers different files (one for the controller and the mainTable,
 * the other for each player's playerTable) and the deck status onto 4 different files (one for each different deck).
 * This class can also be used to load the last available save onto the server and to check if such save is available in
 * the first place.
 * Lastly, it is used to reset the game files once the game has ended.
 */
public class GameSaver {
    private Controller game; //goldCardOnTop and resCardOnTop are not 'MainTable' attributes, yet can be easily found once deck is loaded
    private final Gson gson;
    public final static String CONTROLLER_FILE_PATH = "controllerStatus.json";
    private final String[] deckFilePaths;
    private final List<String> boardFilePaths;

    /**
     * Constructor. Initializes the GsonBuilder and the file paths onto which the game is saved.
     * @param game : controller instance of a game.
     */
    public GameSaver(Controller game) {
        this.game = game;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.boardFilePaths = new ArrayList<>(); //file below has to take nicknames into consideration
        this.boardFilePaths.add(CONTROLLER_FILE_PATH); //controller & main table status
        this.initPlayerTableFiles();
        this.deckFilePaths = new String[4];
        this.deckFilePaths[0] = "deckStatusResources.json";
        this.deckFilePaths[1] = "deckStatusGold.json";
        this.deckFilePaths[2] = "deckStatusStarting.json";
        this.deckFilePaths[3] = "deckStatusObjectives.json";
    }

    /**
     * Initializes player table files with their file path. The file path is based on the player's id.
     * e.g.: src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/playerTableStatus0.json
     */
    private void initPlayerTableFiles(){
        if(game != null) {
            for(int i = 0; i < game.getNumOfPlayers(); i++) { //playerTable excluding board
                this.boardFilePaths.add("playerTableStatus" + i +".json");
            }
        }
    }

    /**
     * Generates the game status JSON files. It generates the controller status file and the player table status files.
     * @return : true whether the operation was successful, false otherwise.
     */
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
        }
        try {
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

    /**
     * Loads controller and mainTable (bar playerTables attribute) statuses from a json file.
     * @return  the controller instance of a previous save. Null if files are not found.
     */
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
        return game; //game has to be updated in the controller
    }

    /**
     * Loads the player tables of a previous save from the respective json files.
     * @return  an array of player tables. Null if files are not found.
     */
    public PlayerTable[] loadPlayerTableStatus(){
        Reader[] reader = new Reader[boardFilePaths.size()-1];
        Type playerTable;
        PlayerTable[] pt = new PlayerTable[boardFilePaths.size()-1];
        Gson ptGson = new GsonBuilder()
                .registerTypeAdapter(PlayerTable.class, new PlayerTableDeserializer())
                .create();
        for(int i = 0; i < boardFilePaths.size()-1; i++) {
            try {
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
        for(PlayerTable p : pt){
            p.idMatrixToCardMatrix(p.getResourceCounter());
        }
        return pt;
    }

    /**
     * Generates the deck status JSON files. It generates the resource, gold, starting and objective cards deck status files.
     * @return : true whether the operation was successful, false otherwise.
     */
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

    /**
     * Loads the deck status of a previous save from the respective json files.
     * @return : true whether the operation was successful, false otherwise.
     */
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

    /**
     * Resets all the game save related files once the game has ended.
     * @return : true whether the operation was successful, false otherwise.
     */
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

    /**
     * Updates the controller status.
     * @param game : controller with updated parameters.
     */
    public void updateControllerStatus(Controller game){
        this.game = game;
    }

    /**
     * Checks whether a game has to restart from a previous save or not.
     * @param nicknames : nicknames of the freshly started matches.
     * @return : true if the game has to restart from a previous save, false otherwise.
     */
    public boolean checkIfRestarted(String[] nicknames){
        Reader reader;
        Reader deckReader;
        try {
            reader = new FileReader(CONTROLLER_FILE_PATH);
        } catch (FileNotFoundException e) {
            System.out.println("File were not found, unable to load the game status.");
            return false;
        }
        try{
            for(String path : deckFilePaths) {
                deckReader = new FileReader(path);
                try{
                    deckReader.close();
                } catch (IOException _) {}
            }
        } catch (FileNotFoundException e) {
            System.out.println("Deck files were not found, starting a fresh new game.");
            return false;
        }
        ArrayList<String> oldNicknames;
        Type listOfNicknames = new TypeToken<ArrayList<String>>() {}.getType();
        Gson gsonNick = new GsonBuilder().registerTypeAdapter(ArrayList.class, new OldNicknamesDeserializer()).create();
        oldNicknames = gsonNick.fromJson(reader, listOfNicknames);
        if(oldNicknames == null)
            return false;
        if(oldNicknames.size() != nicknames.length)
            return false;
        for(int i = 0; i < oldNicknames.size(); i++) {
            if(!oldNicknames.get(i).equals(nicknames[i]))
                return false;
        }
        try{
            reader.close();
        } catch (IOException e) {
            System.out.println("Unable to close the file reader.");
        }
        return true;
    }
}
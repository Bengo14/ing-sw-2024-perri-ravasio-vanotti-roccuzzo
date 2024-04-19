package it.polimi.sw.gianpaolocugola47.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import it.polimi.sw.gianpaolocugola47.model.Deck;
import it.polimi.sw.gianpaolocugola47.model.MainTable;
import it.polimi.sw.gianpaolocugola47.exceptions.GameNotStartedException;

import java.io.FileWriter;
import java.io.IOException;

public class GameSaver {
    private final MainTable game;
    private final Gson gson;
    private final String[] filePaths;

    public GameSaver(MainTable game) {
        this.game = game;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        this.filePaths = new String[2];
        this.filePaths[0] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/mainTableStatus.json";
        this.filePaths[1] = "src/main/resources/it/polimi/sw/gianpaolocugola47/gameStatus/deckStatus.json";
    }

    private void generateGameStatusJson() throws IOException {
        try {
            gson.toJson(this.game, new FileWriter(filePaths[0]));
        } catch (JsonIOException e) {
            e.printStackTrace();
        }
    }

    private boolean generateDeckStatusJson() throws IOException {
        if(Deck.getGoldCardsDeck() == null || Deck.getObjectiveCardsDeck() == null ||
                Deck.getStartingCardsDeck() == null || Deck.getResourceCardsDeck() == null)
        {
            return false; //Game hasn't started yet!
        }
        /*try {
            gson.toJson( , new FileWriter(filePaths[1]));
        } catch (JsonIOException e) {
            e.printStackTrace();
        }*/
        return true;
    }
    private void saveGameStatus() throws GameNotStartedException{
        try{
            if(!generateDeckStatusJson())
                throw new GameNotStartedException("The game has not started yet, decks are empty.");
            generateGameStatusJson();
        } catch (IOException e) {
            System.out.println("Unable to save the current game status onto the respective files.");
        }
    }
}

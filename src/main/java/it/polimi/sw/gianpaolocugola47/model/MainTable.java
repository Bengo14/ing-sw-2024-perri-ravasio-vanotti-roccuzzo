package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents the common board, and it contains the scoreboard,the two decks(resources card and gold card) and the common objectives. The status changes
 * are managed by the {@link //Controller}.
 */
public class MainTable {
    private boolean endGame;
    private ArrayList<GoldCard> goldCardsDeck;
    private ArrayList<ResourceCard> resourceCardsDeck;
    private Colours playerColour;
    private ArrayList<Integer> playerPositions;
    private ArrayList<Integer> globalPlayerPoints;
    /**
     * Corner constructor.
     *
     * @param endGame the boolean that start the end game.
     * @param globalPlayerPoints all the points of a player.
     * @param goldCardsDeck the deck of the gold cards on the board.
     * @param playerColour the pawn of a player.
     * @param playerPositions the points on the board of a player.
     * @param resourceCardsDeck the deck of the resource cards on the board.
     */
    public MainTable(boolean endGame, ArrayList<GoldCard> goldCardsDeck, ArrayList<ResourceCard> resourceCardsDeck, Colours playerColour, ArrayList<Integer> playerPositions, ArrayList<Integer> globalPlayerPoints) {
        this.endGame = endGame;
        this.goldCardsDeck = goldCardsDeck;
        this.resourceCardsDeck = resourceCardsDeck;
        this.playerColour = playerColour;
        this.playerPositions = playerPositions;
        this.globalPlayerPoints = globalPlayerPoints;
    }

    public boolean isEndGame() {
        return endGame;
    }
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }
    public ArrayList<GoldCard> getGoldCardsDeck() {
        return goldCardsDeck;
    }
    public void setGoldCardsDeck(ArrayList<GoldCard> goldCardsDeck) {
        this.goldCardsDeck = goldCardsDeck;
    }
    public ArrayList<ResourceCard> getResourceCardsDeck() {
        return resourceCardsDeck;
    }
    public void setResourceCardsDeck(ArrayList<ResourceCard> resourceCardsDeck) {
        this.resourceCardsDeck = resourceCardsDeck;
    }
    /**
     * Returns the player's colour.
     *
     * @return the player's colour.
     */
    public Colours getPlayerColour() {
        return playerColour;
    }
    public void setPlayerColour(Colours playerColour) {
        this.playerColour = playerColour;
    }/**
     * Returns the player's position.
     *
     * @return the player's position.
     */
    public ArrayList<Integer> getPlayerPositions() {
        return playerPositions;
    }
    public void setPlayerPositions(ArrayList<Integer> playerPositions) {
        this.playerPositions = playerPositions;
    }
    public ArrayList<Integer> getGlobalPlayerPoints() {
        return globalPlayerPoints;
    }
    public void setGlobalPlayerPoints(ArrayList<Integer> globalPlayerPoints) {
        this.globalPlayerPoints = globalPlayerPoints;
    }
}

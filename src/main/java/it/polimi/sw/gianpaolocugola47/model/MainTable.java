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
    private PlaceableCard[] cardsOnTable;
    private int[] playerPoints;
    private int[] globalPlayerPoints;
    private PlayerTable[] playersTables;
    private final int numOfPlayers;
    /**
     * Corner constructor.
     *
     * @param numOfPlayers
     *
     */
    public MainTable(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.endGame = false;
        this.goldCardsDeck = new ArrayList<GoldCard>();
        this.resourceCardsDeck = new ArrayList<ResourceCard>();
        this.cardsOnTable = new PlaceableCard[4];
        this.playerPoints = new int[]{0,0,0,0};
        this.globalPlayerPoints = new int[]{0,0,0,0};
        this.playersTables = new PlayerTable[numOfPlayers];
        initDecksAndTable();
    }

    private void initDecksAndTable(){
        generateResourceCardsDeck();
        generateGoldCardsDeck();
        /*todo*/
    }
    private void generateResourceCardsDeck() {
        /*todo*/
    }
    private void generateGoldCardsDeck() {
        /*todo*/
    }
    public boolean isEndGame() {
        return endGame;
    }
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }
    public void addPlayer(int id, String nickName){
        /*todo*/
    }
    /**
     * Returns the player's points.
     * @param player player id
     * @return the player's points.
     */
    public int getPlayerPoints(int player) {
        return playerPoints[player];
    }
    public void addPlayerPoints(int player, int points) {
        this.playerPoints[player] += points;
    }
    public int getGlobalPlayerPoints(int player) {
        return globalPlayerPoints[player];
    }
    public void addGlobalPlayerPoints(int player, int points) {
        this.globalPlayerPoints[player] += points;
    }
}

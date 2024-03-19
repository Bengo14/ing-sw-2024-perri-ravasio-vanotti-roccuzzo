package it.polimi.sw.gianpaolocugola47.model;

import java.util.ArrayList;
/**
 * This class represents the common board, and it contains the scoreboard,the two decks(resources card and gold card) and the common objectives. The status changes
 * are managed by the {@link //Controller}.
 */
public class MainTable {

    private static MainTable mainTable = null;
    private boolean endGame;
    private ArrayList<GoldCard> goldCardsDeck;
    private ArrayList<ResourceCard> resourceCardsDeck;
    private PlaceableCard[] cardsOnTable;
    private int[] playerPoints;
    private int[] globalPlayerPoints;
    private PlayerTable[] playersTables;
    private int numOfPlayers;
    private StartingCard[] startingCardsDeck;
    private Objectives[] objectiveCardsDeck;
    /**
     * MainTable constructor.
     *
     */
    private MainTable() {
        this.endGame = false;
        this.goldCardsDeck = new ArrayList<GoldCard>();
        this.resourceCardsDeck = new ArrayList<ResourceCard>();
        this.startingCardsDeck = new StartingCard[6];
        this.objectiveCardsDeck = new Objectives[16];
        this.cardsOnTable = new PlaceableCard[4];
        this.playerPoints = new int[]{0,0,0,0};
        this.globalPlayerPoints = new int[]{0,0,0,0};
        this.playersTables = new PlayerTable[4];
        initDecksAndTable();
    }

    private void initDecksAndTable(){
        generateResourceCardsDeck();
        generateGoldCardsDeck();
        generateObjectiveCardsDeck();
        generateStartingCardsDeck();
        /*todo*/
    }
    private void generateResourceCardsDeck() {
        /*todo*/
    }
    private void generateGoldCardsDeck() {
        /*todo*/
    }
    private void generateObjectiveCardsDeck(){
        /*todo*/
    }
    private void generateStartingCardsDeck(){
        /*todo*/
    }
    public boolean isEndGame() {
        return endGame;
    }
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    public void addPlayer(int id, String nickName, Colours color){
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

    public void replaceCardOnTable(int drawCode){
        /*todo*/
    }

    public static MainTable getInstance(){
        if (mainTable == null)
            mainTable = new MainTable();
        return mainTable;
    }
}

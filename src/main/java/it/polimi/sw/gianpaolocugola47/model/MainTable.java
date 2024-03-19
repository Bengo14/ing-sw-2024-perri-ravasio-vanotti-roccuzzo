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
    private Objectives[] globalObjectives;
    private int[] playersPoints;
    private int[] globalPlayersPoints;
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
        this.playersPoints = new int[]{0,0,0,0};
        this.globalPlayersPoints = new int[]{0,0,0,0};
        this.playersTables = new PlayerTable[4];
        this.globalObjectives = new Objectives[2];
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
    public int getPlayersPoints(int player){
        return playersPoints[player];
    }
    public void addPlayersPoints(int player, int points){
        this.playersPoints[player] += points;
    }
    public int getGlobalPlayersPoints(int player){
        return globalPlayersPoints[player];
    }
    public void addGlobalPlayersPoints(int player, int points){
        this.globalPlayersPoints[player] += points;
    }
    /**
     * @param position 0: draw first resource card,
     *                 1: draw second resource card,
     *                 2: draw first gold card,
     *                 3: draw second gold card,
     *                 4: draw from resource deck,
     *                 5: draw from gold deck,
     * @param playerId the ID of the player who makes this action.
     */
    public void drawCardFrom(int position, int playerId){
        PlaceableCard choice = null;

        if(position==0||position==1||position==2||position==3){
            choice = cardsOnTable[position];
            cardsOnTable[position] = null;
            replaceCardOnTable(position);
        }
        if(position == 4)
            choice = drawCardFromResourceDeck();
        if(position == 5)
            choice = drawCardFromGoldDeck();

        playersTables[playerId].setCardOnHandInTheEmptyPosition(choice);
    }
    private void replaceCardOnTable(int position){
        if(position == 0 || position == 1)
            cardsOnTable[position] = drawCardFromResourceDeck();
        if(position == 2 || position == 3)
            cardsOnTable[position] = drawCardFromGoldDeck();
    }
    private PlaceableCard drawCardFromResourceDeck(){
        /*todo*/
        return null;
    }
    private PlaceableCard drawCardFromGoldDeck(){
        /*todo*/
        return null;
    }

    /*todo*/
    private void updatePlayerPoints(int playerId){
        /*todo*/
    }

    public static MainTable getInstance(){
        if (mainTable == null)
            mainTable = new MainTable();
        return mainTable;
    }
}

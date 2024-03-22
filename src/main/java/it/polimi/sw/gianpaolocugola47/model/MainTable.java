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
    private ResourceCard[] cardsOnTable;
    private Objectives[] globalObjectives;
    private int[] boardPoints;
    private int[] globalPoints;
    private PlayerTable[] playersTables;
    private int numOfPlayers;
    private StartingCard[] startingCardsDeck;
    private Objectives[] objectiveCardsDeck;


    public MainTable() {
        this.endGame = false;
        this.goldCardsDeck = new ArrayList<GoldCard>();
        this.resourceCardsDeck = new ArrayList<ResourceCard>();
        this.startingCardsDeck = new StartingCard[6];
        this.objectiveCardsDeck = new Objectives[16];
        this.cardsOnTable = new ResourceCard[4];
        this.globalObjectives = new Objectives[2];
        initDecksAndTable();
    }

    private void initDecksAndTable(){
        generateResourceCardsDeck();
        generateGoldCardsDeck();
        generateObjectiveCardsDeck();
        generateStartingCardsDeck();
        initTable();
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
    private void initTable(){
        cardsOnTable[0] = drawCardFromResourceDeck();
        cardsOnTable[1] = drawCardFromResourceDeck();
        cardsOnTable[2] = drawCardFromGoldDeck();
        cardsOnTable[3] = drawCardFromGoldDeck();
        globalObjectives[0] = drawCardFromObjectivesDeck();
        globalObjectives[1] = drawCardFromObjectivesDeck();
    }
    private ResourceCard drawCardFromResourceDeck(){
        /*todo*/
        return null;
    }
    private GoldCard drawCardFromGoldDeck(){
        /*todo*/
        return null;
    }
    private Objectives drawCardFromObjectivesDeck(){
        /*todo*/
        return null;
    }
    private StartingCard drawCardFromStartingDeck(){
        /*todo*/
        return null;
    }

    /**
     * number of players is set by the first player connected
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.boardPoints = new int[numOfPlayers];
        this.globalPoints = new int[numOfPlayers];
        this.playersTables = new PlayerTable[numOfPlayers];
    }

    public void addPlayer(int id, String nickName, Colours color, boolean isStartingCardFront){

        if(playersTables[id]==null){
            Objectives objective = drawCardFromObjectivesDeck();
            StartingCard startingCard = drawCardFromStartingDeck();
            if(!isStartingCardFront)
                startingCard.switchFrontBack();
            ResourceCard[] cardsOnHand = new ResourceCard[3];
            cardsOnHand[0] = drawCardFromResourceDeck();
            cardsOnHand[1] = drawCardFromResourceDeck();
            cardsOnHand[2] = drawCardFromGoldDeck();
            playersTables[id] = new PlayerTable(id, color, nickName, objective, startingCard, cardsOnHand);
        }
    }

    public int getBoardPoints(int player){
        return boardPoints[player];
    }
    public int getGlobalPoints(int player){
        return globalPoints[player];
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
        ResourceCard choice = null;

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

    public boolean playCardAndUpdatePoints(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId){
        int points = playersTables[playerId].checkAndPlaceCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
        if(points == -1)
            return false;
        addBoardPoints(playerId, points);
        int objectivePoints = playersTables[playerId].getObjectivePoints(this.globalObjectives);
        int globalPoints = objectivePoints + points;
        addGlobalPoints(playerId, globalPoints);
        if(getBoardPoints(playerId)>=20)
            setEndGame();
        return true;
    }
    private void addBoardPoints(int player, int points){
        this.boardPoints[player] += points;
    }
    private void addGlobalPoints(int player, int points){
        this.globalPoints[player] += points;
    }
    protected void setEndGame() {
        this.endGame = true;
    }

    public boolean isEndGame() {
        return endGame;
    }
    public int computeWinnerAtEndGame(){
        int winnerPlayerId = 0;
        int max = 0;
        boardPoints = globalPoints;
        for(int i=0; i<numOfPlayers; i++){
            if(boardPoints[i] > max){
                max = boardPoints[i];
                winnerPlayerId = i;
            }
        }
        return winnerPlayerId;
    }

}

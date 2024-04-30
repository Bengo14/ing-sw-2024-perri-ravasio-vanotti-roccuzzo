package it.polimi.sw.gianpaolocugola47.model;

import it.polimi.sw.gianpaolocugola47.rmi.RMIServer;

/**
 * This class represents the common board, and it contains the scoreboard,
 * the two decks(resources card and gold card) and the common objectives. The status changes
 * are managed by the {@link //Controller}.
 */
public class MainTable {

    private boolean endGame;
    private ResourceCard[] cardsOnTable;
    private Objectives[] globalObjectives;
    private int[] boardPoints;
    private int[] globalPoints;
    private PlayerTable[] playersTables;
    private int numOfPlayers;


    public MainTable() {
        this.endGame = false;
        this.cardsOnTable = new ResourceCard[4];
        this.globalObjectives = new Objectives[2];
        //Deck.initDeck();
        Deck.initAndShuffleDeck();
        initTable();
    }

    private void initTable(){
        cardsOnTable[0] = Deck.drawCardFromResourceDeck();
        cardsOnTable[1] = Deck.drawCardFromResourceDeck();
        cardsOnTable[2] = Deck.drawCardFromGoldDeck();
        cardsOnTable[3] = Deck.drawCardFromGoldDeck();
        globalObjectives[0] = Deck.drawCardFromObjectivesDeck();
        globalObjectives[1] = Deck.drawCardFromObjectivesDeck();
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

    public StartingCard drawStartingCard(int playerId) {
        StartingCard card = Deck.drawCardFromStartingDeck();
        new Thread(()->{RMIServer.getServer().startingCardDrawn(card, playerId);}).start();
        return card;
    }
    public void setPlayerStartingCard(int playerId, StartingCard startingCard){
        this.getPlayerTable(playerId).setStartingCard(startingCard);
    }
    public Objectives[] drawTwoPossibleSecretObjectives(int playerId) {
        Objectives[] obj = new Objectives[]{Deck.drawCardFromObjectivesDeck(), Deck.drawCardFromObjectivesDeck()};
        new Thread(()->{RMIServer.getServer().secretObjectivesDrawn(obj, playerId);}).start();
        return obj;
    }
    public void setPlayerSecretObjective(int playerId, Objectives secretObjective){
        this.getPlayerTable(playerId).setSecretObjective(secretObjective);
    }

    public void switchCardOnHandFrontBack(int playerId, int position){
        this.getPlayerTable(playerId).turnCardOnHand(position);
    }

    public void addPlayer(int id, String nickName) {

        if(playersTables[id] == null) {
            ResourceCard[] cardsOnHand = new ResourceCard[3];
            cardsOnHand[0] = Deck.drawCardFromResourceDeck();
            cardsOnHand[1] = Deck.drawCardFromResourceDeck();
            cardsOnHand[2] = Deck.drawCardFromGoldDeck();
            playersTables[id] = new PlayerTable(id, nickName, cardsOnHand);
        }
    }

    public void setViewFixedParams(){
        new Thread(()->{
            RMIServer.getServer().setViewFixedParams(playersTables, globalObjectives);
        }).start();
    }
    public void updateView() {
        new Thread(()->{
            RMIServer.getServer().updateView(playersTables, boardPoints, globalPoints, cardsOnTable);
        }).start();
    }
    protected void updateView(int playerId) {
        new Thread(()->{
            RMIServer.getServer().updateView(getPlayerTable(playerId), boardPoints, globalPoints, cardsOnTable);
        }).start();
    }
    public void showTurn(int playerId) {
        new Thread(()->{
            RMIServer.getServer().showTurn(playerId);
        }).start();
    }
    protected void showWinner(int winner) {
        new Thread(()->{
            RMIServer.getServer().showWinner(winner);
        }).start();
    }
    protected void showPlayablePositions(int playerId, boolean[][] matrix) {
        new Thread(()->{
            RMIServer.getServer().showPlayablePositions(playerId, matrix);
        }).start();
    }

    public void turnCardOnHand(int playerId, int cardPosition) {
        playersTables[playerId].turnCardOnHand(cardPosition);
    }

    public int getBoardPoints(int playerId) {
        return boardPoints[playerId];
    }
    public int getGlobalPoints(int playerId) {
        return globalPoints[playerId];
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
    public void drawCardFrom(int position, int playerId) {
        ResourceCard choice = null;

        if(position==0||position==1||position==2||position==3){
            choice = cardsOnTable[position];
            cardsOnTable[position] = null;
            replaceCardOnTable(position);
        }
        if(position == 4)
            choice = Deck.drawCardFromResourceDeck();
        if(position == 5)
            choice = Deck.drawCardFromGoldDeck();

        if(choice != null)
            playersTables[playerId].setCardOnHandInTheEmptyPosition(choice);
        if(Deck.areDecksEmpty())
            setEndGame();
        this.updateView(playerId);
    }

    private void replaceCardOnTable(int position){
        if(position == 0 || position == 1)
            cardsOnTable[position] = Deck.drawCardFromResourceDeck();
        if(position == 2 || position == 3)
            cardsOnTable[position] = Deck.drawCardFromGoldDeck();
    }

    public boolean checkIfPlayerCanPlay(int playerId) {
        if(playersTables[playerId].getCanPlay()) {
            playersTables[playerId].checkIfCanPlay();
        }
        return playersTables[playerId].getCanPlay();
        // if canPlay==false it can't be set to true again
    }

    public boolean[][] checkAllPlayablePositions(int playerId) {
        PlayerTable playerTable = playersTables[playerId];
        boolean[][] matrix = new boolean[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++){
            for(int j = 0; j < PlayerTable.getMatrixDimension(); j++){
                matrix[i][j] = playerTable.isPlaceable(i, j); // boolean
            }
        }
        this.showPlayablePositions(playerId, matrix);
        return matrix;
    }

    public boolean playCardAndUpdatePoints(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) {
        if(playersTables[playerId].getPlacedCard(onTableCardX, onTableCardY) != null) {
            int points = playersTables[playerId].checkAndPlaceCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
            if(points == -1)
                return false; // GoldCard requisites not matched OR position is not buildable
            addBoardPoints(playerId, points);
            int objectivePoints = playersTables[playerId].getObjectivePoints(this.globalObjectives);
            int globalPoints = this.getBoardPoints(playerId) + objectivePoints;
            setGlobalPoints(playerId, globalPoints);
            if(getBoardPoints(playerId)>=20 && !isEndGame())
                setEndGame();
            this.updateView(playerId);
            return true; // correct placement and points added
        } else {
            return false; // incorrect input: onTableCard is null
        }
    }
    private void addBoardPoints(int player, int points) {
        this.boardPoints[player] += points;
    }
    private void setGlobalPoints(int player, int points) {
        this.globalPoints[player] = points;
    }

    protected void setEndGame() {
        this.endGame = true;
    }
    public boolean isEndGame() {
        return this.endGame;
    }
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }
    protected PlayerTable getPlayerTable(int index) {
        return this.playersTables[index];
    }

    public int computeWinnerAtEndGame() {

        int winnerPlayerId = 0;
        int max = 0;
        boolean draw = false;
        int[] objectivePoints = new int[numOfPlayers];

        for(int i=0; i<numOfPlayers; i++){
            objectivePoints[i] = globalPoints[i]-boardPoints[i];
            if(globalPoints[i] >= max){
                if(globalPoints[i] == max){
                    draw = true;
                }
                else {
                    max = globalPoints[i];
                    winnerPlayerId = i;
                    draw = false;
                }
            }
        }
        if(draw){
            int max1 = 0;
            for(int i=0; i<numOfPlayers; i++){
                if(globalPoints[i]==max && objectivePoints[i]>max1){
                    max1 = objectivePoints[i];
                    winnerPlayerId = i;
                }
            }
        }
        this.showWinner(winnerPlayerId);
        return winnerPlayerId;
    }

    /**
     * method used only for testing purposes
     */
    protected void setPlayerTable(int i, PlayerTable player) {
        playersTables[i] = player;
    }
}

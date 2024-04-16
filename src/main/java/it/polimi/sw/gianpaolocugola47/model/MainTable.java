package it.polimi.sw.gianpaolocugola47.model;

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
    private Objectives[] possibleSecretObjectives;
    private StartingCard chosenStartingCard;


    public MainTable() {
        this.endGame = false;
        this.cardsOnTable = new ResourceCard[4];
        this.globalObjectives = new Objectives[2];
        this.possibleSecretObjectives = new Objectives[2];
        Deck.initDeck();
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

    public void drawTwoPossibleSecretObjectives(){
        this.possibleSecretObjectives[0] = Deck.drawCardFromObjectivesDeck();
        this.possibleSecretObjectives[1] = Deck.drawCardFromObjectivesDeck();
        //update view
    }
    public void drawStartingCard(){
        this.chosenStartingCard = Deck.drawCardFromStartingDeck();
        //update view
    }

    public void switchCardOnHandFrontBack(int playerId, int position){
        this.playersTables[playerId].turnCardOnHand(position);
    }

    public void addPlayer(int id, String nickName, boolean isStartingCardFront, int chosenObjective){

        if(playersTables[id]==null){

            Objectives objective = possibleSecretObjectives[chosenObjective];
            if(!isStartingCardFront)
                chosenStartingCard.switchFrontBack();
            ResourceCard[] cardsOnHand = new ResourceCard[3];
            cardsOnHand[0] = Deck.drawCardFromResourceDeck();
            cardsOnHand[1] = Deck.drawCardFromResourceDeck();
            cardsOnHand[2] = Deck.drawCardFromGoldDeck();
            playersTables[id] = new PlayerTable(id, nickName, objective, chosenStartingCard, cardsOnHand);
        }
    }
    public void turnCardOnHand(int playerId, int cardPosition){
        playersTables[playerId].turnCardOnHand(cardPosition);
    }

    public int getBoardPoints(int playerId){
        return boardPoints[playerId];
    }
    public int getGlobalPoints(int playerId){
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
    public void drawCardFrom(int position, int playerId){
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

        playersTables[playerId].setCardOnHandInTheEmptyPosition(choice);
        /*todo
        if(both decks are empty)
            setEndGame();
        */
    }
    private void replaceCardOnTable(int position){
        if(position == 0 || position == 1)
            cardsOnTable[position] = Deck.drawCardFromResourceDeck();
        if(position == 2 || position == 3)
            cardsOnTable[position] = Deck.drawCardFromGoldDeck();
    }

    public boolean checkIfPlayerCanPlay(int playerId){
        if(playersTables[playerId].getCanPlay()) {
            playersTables[playerId].checkIfCanPlay();
        }
        return playersTables[playerId].getCanPlay();
    }

    public void checkAllPlayablePositions(PlayerTable playerTable){
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++){
            for(int j = 0; j < PlayerTable.getMatrixDimension(); j++){
                if(playerTable.isPlaceable(i, j))
                    showPlayablePosition(i,j);
            }
        }
    }
    private void showPlayablePosition(int x, int y){
        ResourceCard card=new ResourceCard("none","none",0, null);
        card.setCoordinates(x,y);
        /*todo*/
        // update view
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
        return winnerPlayerId;
    }
}

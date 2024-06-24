package it.polimi.sw.gianpaolocugola47.model;

import it.polimi.sw.gianpaolocugola47.controller.Controller;
import it.polimi.sw.gianpaolocugola47.observer.Observable;
import it.polimi.sw.gianpaolocugola47.observer.Observer;

import java.rmi.RemoteException;
import java.util.*;

/**
 * This class represents the common board, and it contains the scoreboard,
 * the drawable cards present on the table and the common objectives.
 * The status changes are managed by the {@link Controller}.
 */
public class MainTable implements Observable {

    private boolean endGame;
    private ResourceCard[] cardsOnTable;
    private Objectives[] globalObjectives;
    private int[] boardPoints;
    private int[] globalPoints;
    private PlayerTable[] playersTables;
    private int numOfPlayers;
    private final List<Observer> observers;

    /**
     * Constructor. It initializes the main table key parameters and the decks.
     */
    public MainTable() {
        this.endGame = false;
        this.cardsOnTable = new ResourceCard[4];
        this.globalObjectives = new Objectives[2];
        this.observers = new ArrayList<>();
        Deck.initAndShuffleDeck();
        initTable();
    }

    /**
     * Sets the endGame attribute.
     * @param endGame : true if the game has entered final phase, false otherwise.
     */
    public void setEndGame(boolean endGame) {
        this.endGame = endGame;
    }

    /**
     * Returns the drawable cards on the table.
     * @return : the drawable cards on the table.
     */
    public ResourceCard[] getCardsOnTable() {
        return cardsOnTable;
    }

    /**
     * Returns the drawable card on the table for a specific position.
     * @param index : the position identifier.
     * @return : the specific drawable card on the table.
     */
    public ResourceCard getCardOnTable(int index) {
        return cardsOnTable[index];
    }

    /**
     * Sets the drawable cards on the table.
     * @param cardsOnTable : the drawable cards on the table.
     */
    public void setCardsOnTable(ResourceCard[] cardsOnTable) {
        this.cardsOnTable = cardsOnTable;
    }

    /**
     * Returns the global objectives.
     * @return : the global objectives.
     */
    public Objectives[] getGlobalObjectives() {
        return globalObjectives;
    }

    /**
     * Sets the global objectives.
     * @param globalObjectives : the global objectives.
     */
    public void setGlobalObjectives(Objectives[] globalObjectives) {
        this.globalObjectives = globalObjectives;
    }

    /**
     * Returns the board points (corresponding to the position on the points board).
     * @return : the board points.
     */
    public int[] getBoardPoints() {
        return boardPoints;
    }

    /**
     * Sets the board points (corresponding to the position on the points board).
     * @param boardPoints : the board points.
     */
    public void setBoardPoints(int[] boardPoints) {
        this.boardPoints = boardPoints;
    }

    /**
     * Returns the global points (corresponding to the points obtained by satisfying the objective's requests).
     * @return : the global points.
     */
    public int[] getGlobalPoints() {
        return globalPoints;
    }

    /**
     * Sets the global points (corresponding to the points obtained by satisfying the objective's requests).
     * @param globalPoints : the global points.
     */
    public void setGlobalPoints(int[] globalPoints) {
        this.globalPoints = globalPoints;
    }

    /**
     * Returns all the player tables.
     * @return : player table array.
     */
    public PlayerTable[] getPlayersTables() {
        return playersTables;
    }

    /**
     * Sets all the player tables.
     * @param playersTables : player table array.
     */
    public void setPlayersTables(PlayerTable[] playersTables) {
        this.playersTables = playersTables;
    }

    /**
     * Adds an observer to the list of observers.
     * @param observer : the observer to add.
     */
    @Override
    public void addObserver(Observer observer) {
        synchronized (observers) {
            this.observers.add(observer);
        }
    }

    /**
     * Removes an observer from the list of observers.
     * @param observer : the observer to remove.
     */
    @Override
    public void removeObserver(Observer observer) {
        synchronized (observers) {
            this.observers.remove(observer);
        }
    }

    /**
     * Draws a starting card from the starting deck.
     * @return : the starting card drawn.
     */
    public StartingCard drawStartingCard() {
        return Deck.drawCardFromStartingDeck();
    }

    /**
     * Sets the starting card for a player.
     * @param playerId : the ID of the player.
     * @param startingCard : the starting card to set.
     */
    public void setPlayerStartingCard(int playerId, StartingCard startingCard) {
        this.getPlayerTable(playerId).setStartingCard(startingCard);
    }

    /**
     * Draws two possible secret objectives from the objectives deck.
     * @return : the two possible secret objectives to be picked by a player.
     */
    public Objectives[] drawTwoPossibleSecretObjectives() {
        return new Objectives[]{Deck.drawCardFromObjectivesDeck(), Deck.drawCardFromObjectivesDeck()};
    }

    /**
     * Sets the secret objective for a player.
     * @param playerId : the ID of the player.
     * @param secretObjective : the secret objective to set.
     */
    public void setPlayerSecretObjective(int playerId, Objectives secretObjective) {
        this.getPlayerTable(playerId).setSecretObjective(secretObjective);
    }

    /**
     * Initializes the view for all the observers.
     * It sends the nicknames, the global objectives, the cards on hand and the cards on table.
     * Observers are used to update the view of each player.
     */
    @Override
    public void initView() {
        synchronized (observers) {
            for (Observer observer : observers)
                observer.initView(getNicknames(), getGlobalObjectives(), getCardsOnHand(), getCardsOnTable());
        }
    }

    /**
     * Updates the decks for all the observers.
     * @param drawPos : the position from which the card was drawn, be it of the drawable on table cards
     *                or of one of the two decks (Resource and Gold).
     */
    @Override
    public void updateDecks(int drawPos) {
        new Thread(()->{
            synchronized (observers) {
                for (Observer observer : observers)
                    observer.updateDecks(Deck.getResourceCardOnTop(), Deck.getGoldCardOnTop(), drawPos);
            }
        }).start();
    }

    /**
     * Updates the points (both global and board) for all the observers.
     */
    @Override
    public void updatePoints() {
        new Thread(()->{
            synchronized (observers) {
                for (Observer observer : observers)
                    observer.updatePoints(getBoardPoints(), getGlobalPoints());
            }
        }).start();
    }

    /**
     * Shows the turn for all the observers.
     * @param playerId : the ID of the player who has the turn.
     */
    @Override
    public void showTurn(int playerId) {
        new Thread(()->{
            synchronized (observers) {
                for (Observer observer : observers)
                    observer.showTurn(playerId);
            }
        }).start();
    }

    /**
     * Shows the winner for all the observers.
     * @param winner : the ID of the winner.
     */
    @Override
    public void showWinner(int winner) {
        new Thread(()->{
            synchronized (observers) {
                for (Observer observer : observers)
                    observer.showWinner(winner);
            }
        }).start();
    }

    /**
     * Starts the game for all the observers.
     */
    @Override
    public void startGame() {
        new Thread(()->{
            synchronized (observers) {
                for (Observer observer : observers) {
                    try {
                        observer.startGame();
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }

    /**
     * Initializes the table by drawing the resourceCards, the drawable board cards and the
     * shared objectives.
     */
    private void initTable(){
        cardsOnTable[0] = Deck.drawCardFromResourceDeck();
        cardsOnTable[1] = Deck.drawCardFromResourceDeck();
        cardsOnTable[2] = Deck.drawCardFromGoldDeck();
        cardsOnTable[3] = Deck.drawCardFromGoldDeck();
        globalObjectives[0] = Deck.drawCardFromObjectivesDeck();
        globalObjectives[1] = Deck.drawCardFromObjectivesDeck();
    }

    /**
     * Sets the number of players in a specific match.
     * The number of players is set by the first player connected.
     * It initializes the PlayerTable array with the right dimension.
     * @param numOfPlayers : the number of players in the match.
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.boardPoints = new int[numOfPlayers];
        this.globalPoints = new int[numOfPlayers];
        this.playersTables = new PlayerTable[numOfPlayers];
    }

    /**
     * Adds a player to the table and initializes its hand by drawing 2 resource cards and a gold card from the
     * respective deck. The nickname of the player is also set.
     * @param id : the ID of the player.
     * @param nickName : the nickname of the player.
     */
    public void addPlayer(int id, String nickName) {
        if(getPlayerTable(id) == null) {
            ResourceCard[] cardsOnHand = new ResourceCard[3];
            cardsOnHand[0] = Deck.drawCardFromResourceDeck();
            cardsOnHand[1] = Deck.drawCardFromResourceDeck();
            cardsOnHand[2] = Deck.drawCardFromGoldDeck();
            playersTables[id] = new PlayerTable(id, nickName, cardsOnHand);
        }
    }

    /**
     * Returns the nicknames of all the players by retrieving them from the player tables.
     * @return : the nicknames of all the players.
     */
    public String[] getNicknames() {
        String[] nicknames = new String[getNumOfPlayers()];
        for(int i = 0; i<getNumOfPlayers(); i++) {
            if (getPlayerTable(i) != null)
                nicknames[i] = getPlayerTable(i).getNickName();
            else nicknames[i] = "";
        }
        return nicknames;
    }

    /**
     * Returns the cards on hand for all the players by retrieving them from the player tables.
     * @return : the cards on hand for all the players.
     */
    public ResourceCard[][] getCardsOnHand() {
        ResourceCard[][] cards = new ResourceCard[getNumOfPlayers()][3];
        for(int i=0; i<getNumOfPlayers(); i++)
            cards[i] = getPlayerTable(i).getCardsOnHand();
        return cards;
    }

    /**
     * Returns the placed cards for a specific player by retrieving them from the player table.
     * @param playerId : the ID of the player.
     * @return : the placed cards for the player, representing its board.
     */
    public PlaceableCard[][] getPlacedCards(int playerId) {
        return getPlayerTable(playerId).getPlacedCards();
    }

    /**
     * Returns the resource counter for a specific player by retrieving it from the player table.
     * @param playerId : the ID of the player.
     * @return : the resource counter for the player.
     */
    public int[] getResourceCounter(int playerId) {
        return Arrays.copyOf(getPlayerTable(playerId).getResourceCounter(), 7);
    }

    /**
     * Turns the specified player's card in the given hand position on its front or back.
     * @param playerId : the ID of the player.
     * @param cardPosition : the position of the card in the hand.
     * @param isFront : true if the card is front, false otherwise.
     */
    public void turnCardOnHand(int playerId, int cardPosition, boolean isFront) {
        getPlayerTable(playerId).turnCardOnHand(cardPosition, isFront);
    }

    /**
     * Returns the board points for a specific player.
     * @param playerId : the ID of the player.
     * @return :
     */
    public int getBoardPoints(int playerId) {
        return boardPoints[playerId];
    }

    /**
     * Returns the global points for a specific player.
     * @param playerId : the ID of the player.
     * @return : the global points for the player.
     */
    public int getGlobalPoints(int playerId) {
        return globalPoints[playerId];
    }

    /**
     * Draws a card from either the deck or the board.
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

        if(position==0||position==1||position==2||position==3) {
            choice = getCardOnTable(position);
            cardsOnTable[position] = null;
            replaceCardOnTable(position);
        }
        if(position == 4)
            choice = Deck.drawCardFromResourceDeck();
        if(position == 5)
            choice = Deck.drawCardFromGoldDeck();

        if(choice != null)
            getPlayerTable(playerId).setCardOnHandInTheEmptyPosition(choice);
        if(Deck.areDecksEmpty())
            setEndGame();

        this.updateDecks(position);
    }

    /**
     * Replaces a card on the table with a new one drawn from the respective deck.
     * @param position : the position of the card to replace.
     */
    private void replaceCardOnTable(int position){
        if(position == 0 || position == 1)
            cardsOnTable[position] = Deck.drawCardFromResourceDeck();
        if(position == 2 || position == 3)
            cardsOnTable[position] = Deck.drawCardFromGoldDeck();
    }

    /**
     * Checks if a player can play its move.
     * @param playerId : the ID of the player.
     * @return : true if the player can play, false otherwise.
     */
    public boolean checkIfPlayerCanPlay(int playerId) {
        if(getPlayerTable(playerId).getCanPlay()) {
            getPlayerTable(playerId).checkIfCanPlay();
        }
        return getPlayerTable(playerId).getCanPlay();
        // if canPlay==false it can't be set to true again
    }

    /**
     * Checks where on the board a player can play its cards.
     * @param playerId : the ID of the player.
     * @return : a matrix of booleans representing the playable positions.
     */
    public boolean[][] checkAllPlayablePositions(int playerId) {
        PlayerTable playerTable = getPlayerTable(playerId);
        boolean[][] matrix = new boolean[PlayerTable.getMatrixDimension()][PlayerTable.getMatrixDimension()];
        for(int i = 0; i < PlayerTable.getMatrixDimension(); i++) {
            for(int j = 0; j < PlayerTable.getMatrixDimension(); j++){
                matrix[i][j] = playerTable.isPlaceable(i, j);
            }
        }
        return matrix;
    }

    /**
     * Checks if a player can play a card on the table (over an already existing card's corner).
     * If the card is placed, the respective points (both global board) and  are updated.
     * @param onHandCard : the position of the card in the hand (0-2).
     * @param onTableCardX : the row coordinate of the card on the table.
     * @param onTableCardY : the column coordinate of the card on the table.
     * @param onTableCardCorner : the corner of the card on the table.
     * @param playerId : the ID of the player.
     * @return : true if the player can play the card, false otherwise.
     */
    public boolean playCardAndUpdatePoints(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) {
        if(getPlayerTable(playerId).getPlacedCard(onTableCardX, onTableCardY) != null) {
            int points = getPlayerTable(playerId).checkAndPlaceCard(onHandCard, onTableCardX, onTableCardY, onTableCardCorner);
            if(points == -1)
                return false; // GoldCard requisites not matched OR position is not buildable
            getPlayerTable(playerId).getCardsOnHand()[onHandCard] = null; // card that will be replaced drawing
            addBoardPoints(playerId, points);
            int objectivePoints = getPlayerTable(playerId).getObjectivePoints(getGlobalObjectives());
            int globalPoints = getBoardPoints(playerId) + objectivePoints;
            setGlobalPoints(playerId, globalPoints);
            if(getBoardPoints(playerId)>=20 && !getEndGame())
                setEndGame();
            updatePoints();
            return true; // correct placement and points added
        } else {
            return false; // incorrect input: onTableCard is null
        }
    }
    /**
     * Adds points to the board points of a player.
     * @param player : the ID of the player.
     * @param points : the points to add.
     */
    private void addBoardPoints(int player, int points) {
        this.boardPoints[player] += points;
    }
    /**
     * Sets the global points of a player.
     */
    private void setGlobalPoints(int player, int points) {
        this.globalPoints[player] = points;
    }
    /**
     * Sets the endGame attribute to true.
     */
    protected void setEndGame() {
        this.endGame = true;
    }

    /**
     * Returns the endGame attribute.
     * @return : true if the game has entered final phase, false otherwise.
     */
    public boolean getEndGame() {
        return this.endGame;
    }

    /**
     * Returns the number of players in the match.
     * @return : the number of players in the match.
     */
    public int getNumOfPlayers() {
        return this.numOfPlayers;
    }

    /**
     * Returns the player table of a specific player.
     * @param index : the ID of the player.
     * @return : the player table of the specified player.
     */
    public PlayerTable getPlayerTable(int index) {
        return this.playersTables[index];
    }

    /**
     * Computes the winner at the end of the game.
     * @return : the ID of the winner.
     */
    public int computeWinnerAtEndGame() {
        int winnerPlayerId = 0;
        int max = 0;
        boolean draw = false;
        int[] objectivePoints = new int[getNumOfPlayers()];

        for(int i=0; i<getNumOfPlayers(); i++){
            objectivePoints[i] = getBoardPoints(i)-getBoardPoints(i);
            if(getGlobalPoints(i) >= max){
                if(getGlobalPoints(i) == max){
                    draw = true;
                }
                else {
                    max = getGlobalPoints(i);
                    winnerPlayerId = i;
                    draw = false;
                }
            }
        }
        if(draw){
            int max1 = 0;
            for(int i=0; i<getNumOfPlayers(); i++){
                if(getGlobalPoints(i)==max && objectivePoints[i]>max1){
                    max1 = objectivePoints[i];
                    winnerPlayerId = i;
                }
            }
        }
        this.showWinner(winnerPlayerId);
        return winnerPlayerId;
    }

    /**
     * Sets the player table in a specified position.
     * Only used for testing purposes.
     */
    public void setPlayerTable(int i, PlayerTable player) {
        playersTables[i] = player;
    }

    /**
     * toString method.
     * @return : the string representation of the main table.
     */
    @Override
    public String toString() {
        return "MainTable{" +
                "endGame=" + endGame +
                ", cardsOnTable=" + Arrays.toString(cardsOnTable) +
                ", globalObjectives=" + Arrays.toString(globalObjectives) +
                ", boardPoints=" + Arrays.toString(boardPoints) +
                ", globalPoints=" + Arrays.toString(globalPoints) +
                ", playersTables=" + Arrays.toString(playersTables) +
                ", numOfPlayers=" + numOfPlayers +
                ", observers=" + observers +
                '}';
    }
    /**
     * Returns the observers.
     * @return : the observers.
     */
    public Collection<Object> getObservers() {
        return Collections.singleton(observers);
    }
}
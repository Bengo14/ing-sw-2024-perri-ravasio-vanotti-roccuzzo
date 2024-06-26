package it.polimi.sw.gianpaolocugola47.controller;

import com.google.gson.annotations.Expose;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.utils.GameSaver;

/**
 * Controller class is the main class of the game. It is responsible for managing the game flow and the interactions between the model and the views.
 */
public class Controller { //has to include nicknames of players

    @Expose
    private MainTable mainTable;
    @Expose
    private int currentPlayerId;
    @Expose
    private int clientsConnected; // max number of clients in a given game
    @Expose
    private int numOfPlayers;
    @Expose
    private int playersAdded;
    @Expose
    private int startingCardsAndObjAdded;
    @Expose
    private boolean isLastTurn;
    private GameSaver gameSaver;
    private boolean isGameLoaded = false;

    /**
     * Returns the mainTable of the controller.
     * @return : the mainTable of the controller.
     */
    public MainTable getMainTable() {
        return mainTable;
    }

    /**
     * Sets the mainTable of the controller.
     * @param mainTable : the mainTable to be set.
     */
    public void setMainTable(MainTable mainTable) {
        this.mainTable = mainTable;
    }

    /**
     * Returns the id of the current player.
     * @return : id of the current player.
     */
    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    /**
     * Returns the number of players added to the game.
     * @return : number of players added to the game.
     */
    public int getPlayersAdded() {
        return playersAdded;
    }

    /**
     * Returns the number of starting cards and objectives added to the game.
     * @return : number of starting cards and objectives added to the game.
     */
    public int getStartingCardsAndObjAdded() {
        return startingCardsAndObjAdded;
    }

    /**
     * Returns whether the current turn is the last one or not.
     * @return : true if the current turn is the last one, false otherwise.
     */
    public boolean isLastTurn() {
        return isLastTurn;
    }

    /**
     * Constructor of the Controller class. It initializes the mainTable, the currentPlayerId, the playersAdded, the startingCardsAndObjAdded and the clientsConnected.
     */
    public Controller() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
        this.clientsConnected = 0;
        this.numOfPlayers = -1;
        this.isLastTurn = false;
    }

    /**
     * Method that resets the game. It brings the game to the original status, fresh out of the constructor.
     */
    public void resetGame() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
        this.clientsConnected = 0;
        this.numOfPlayers = -1;
        this.isLastTurn = false;
    }

    /**
     * Adds an observer to the mainTable.
     * @param observer: the observer to be added.
     */
    public void addModelObserver(Observer observer) {
        this.mainTable.addObserver(observer);
    }
    /**
     * Sets the number of players in the game both on the controller and on the mainTable.
     * @param num : number of players.
     */
    public void setNumOfPlayers(int num) {
        this.numOfPlayers = num;
        this.mainTable.setNumOfPlayers(num);
    }
    /**
     * Returns the number of players.
     * @return : number of players in the game.
     */
    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }
    /**
     * Adds a client to the game.
     * Increments the number of clients connected by one.
     */
    public void addClientConnected(){
        this.clientsConnected++;
    }
    /**
     * Returns the number of clients connected to the game.
     * @return : number of clients connected to the game.
     */
    public int getClientsConnected(){
        return this.clientsConnected;
    }

    /**
     * Sets the id of the current player. Used when loading from file.
     * @param currentPlayerId : id of the current player.
     */
    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    /**
     * Sets the number of clients connected to the game. Used when loading from file.
     * @param clientsConnected : number of clients connected to the game.
     */
    public void setClientsConnected(int clientsConnected) {
        this.clientsConnected = clientsConnected;
    }
    /**
     * Sets the number of players added to the game. Used when loading from file.
     * @param playersAdded : number of players added to the game.
     */
    public void setPlayersAdded(int playersAdded) {
        this.playersAdded = playersAdded;
    }
    /**
     * Sets the number of starting cards and objectives added to the game. Used when loading from file.
     * @param startingCardsAndObjAdded : number of starting cards and objectives added to the game.
     */
    public void setStartingCardsAndObjAdded(int startingCardsAndObjAdded) {
        this.startingCardsAndObjAdded = startingCardsAndObjAdded;
    }
    /**
     * Sets whether the current turn is the last one or not. Used when loading from file.
     * @param lastTurn : true if the current turn is the last one, false otherwise.
     */
    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    /**
     * Adds a player to the game. Checks whether the game is loaded or not and starts the game.
     * @param id : id of the player.
     * @param nickname: nickname of the player.
     */
    public void addPlayer(int id, String nickname) {
        mainTable.addPlayer(id, nickname);
        this.playersAdded++;
        if(this.numOfPlayers == this.playersAdded){
            this.gameSaver = new GameSaver(this); //if(gameSaver.checkIfRestarted(mainTable.getNicknames()))
            if(gameSaver.checkIfRestarted(mainTable.getNicknames())){
                this.isGameLoaded = true;
                System.err.println("game loaded from disk");
            }
            else{
                gameSaver.resetFiles();
            }
            mainTable.startGame();
        }
    }

    /**
     * Returns the number of players currently added to the game.
     * @return : number of players currently added to the game.
     */
    public int getNumOfPlayersCurrentlyAdded(){
        return this.playersAdded;
    }

    /**
     * Calls mainTable.drawStartingCard(), which draws the starting card from the deck.
     * @return : the starting card drawn.
     */
    public StartingCard drawStartingCard() {
        return mainTable.drawStartingCard();
    }

    /**
     * Sets the starting card of a player and draws two possible secret objectives.
     * @param playerId : id of the player who needs to pick objectives.
     * @param card : starting card of the player that is set.
     * @return : two possible secret objectives drawn, one will be picked by the player.
     */
    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        mainTable.setPlayerStartingCard(playerId, card);
        return mainTable.drawTwoPossibleSecretObjectives();
    }

    /**
     * Sets the secret objective of a player and updates the view.
     * @param playerId : id of the player who has picked its objective.
     * @param obj: secret objective picked by the player.
     */
    public void setSecretObjectiveAndUpdateView(int playerId, Objectives obj) {
        mainTable.setPlayerSecretObjective(playerId, obj);
        startingCardsAndObjAdded++;
        if(startingCardsAndObjAdded == mainTable.getNumOfPlayers()) {
            mainTable.initView();
            mainTable.updateDecks(5);
            mainTable.showTurn(currentPlayerId);
        }
    }

    /**
     * Starts the game from the file. Loads the game and initializes the view.
     * Updates the deck and shows the turn to the correct player.
     */
    public void startGameFromFile(){
        loadGame();
        mainTable.initView();
        mainTable.updateDecks(5);
        mainTable.showTurn(currentPlayerId);
    }

    /**
     * Returns the empty positions where a card can be played on a given player board.
     * @param playerId : id of the player who asked the playablePositions.
     * @return : a matrix of booleans representing the empty positions where a card can be played.
     */
    public boolean[][] getPlayablePositions(int playerId) {
        return mainTable.checkAllPlayablePositions(playerId);
    }

    /**
     * Plays a card on the table. If the card is played successfully, the points are updated and the board updated.
     * @param onHandCard : hand position of the card to be placed.
     * @param onTableCardX : row position of the board card over which the hand card is placed.
     * @param onTableCardY : column position of the board card over which the hand card is placed.
     * @param onTableCardCorner : corner position of the board card over which the hand card is placed.
     * @param playerId : id of the player who is playing the card.
     * @param isFront : true if the card is played on the front, false otherwise.
     * @return : true if the card is played successfully, false otherwise.
     */
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        if(playerId == currentPlayerId) {
            mainTable.turnCardOnHand(playerId, onHandCard, isFront);
            return mainTable.playCardAndUpdatePoints(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId);
        }
        else return false;
    }

    /**
     * Draws a card from either the deck or the board position.
     * @param position : deck or board position.
     * @param playerId : player who's drawing the card.
     */
    public void drawCard(int position, int playerId) {
        if(playerId == currentPlayerId) {
            mainTable.drawCardFrom(position, currentPlayerId);
            endTurn();
        }
    }

    /**
     * Handles the end of the turn: picks next player's turn, checks whether the game is ending or not
     * and eventually computes the winner.
     */
    private void endTurn() {
        updateCurrentPlayer();
        if(currentPlayerId == 0 && mainTable.getEndGame()) {
            if(isLastTurn) {
                currentPlayerId = -1;
                computeWinner();
                gameSaver.resetFiles();
                return;
            } else {
                this.isLastTurn = true;
            }
        }
        for(int i=0; i<numOfPlayers; i++) {
            if(!mainTable.checkIfPlayerCanPlay(currentPlayerId))
                updateCurrentPlayer();
            else {
                mainTable.showTurn(currentPlayerId);
                return;
            }
        }
        currentPlayerId = -1;
        computeWinner();
    }

    /**
     * Updates the current player. If the current player is the last one, the first player is picked.
     * If the current player is not the last one, the next one up is the player with the next id.
     * At the end of each turn, the game and deck statuses are saved onto the respective files.
     */
    private void updateCurrentPlayer() {
        if(currentPlayerId == mainTable.getNumOfPlayers()-1){
            currentPlayerId = 0;
        }
        else this.currentPlayerId++;
        gameSaver.updateControllerStatus(this);
        gameSaver.generateGameStatusJson();
        gameSaver.generateDeckStatusJson();
    }

    /**
     * Computes the winner of the game. It calls the mainTable.computeWinnerAtEndGame() method.
     */
    private void computeWinner() {
        mainTable.computeWinnerAtEndGame();
    }

    /**
     * Returns the cards on hand of every player.
     * @return : the cards on hand of every player.
     */
    public ResourceCard[][] getCardsOnHand() {
        return mainTable.getCardsOnHand();
    }

    /**
     * Returns the placed cards of a player.
     * @param playerId : id of the player who's placed cards are requested.
     * @return : the placed cards of the player.
     */
    public PlaceableCard[][] getPlacedCards(int playerId) {
        return mainTable.getPlacedCards(playerId);
    }

    /**
     * Returns the global objectives of the game.
     * @param playerId : id of the player who's global objectives are requested.
     * @return : the global objectives of the player.
     */
    public int[] getResourceCounter(int playerId) {
        return mainTable.getResourceCounter(playerId);
    }

    /**
     * Returns the nicknames of the players in the game.
     * @return : the nicknames of the players in the game.
     */
    public String[] getNicknames() {
        return mainTable.getNicknames();
    }

    /**
     * Loads the game from the respective files by calling the respective gameSaver functions.
     * Once the decks, playerTables, mainTable and controller are loaded, the respective parameters are
     * set onto the current controller and mainTable attributes. At the end of this, gameSaver's status is updated.
     */
    public void loadGame(){
        gameSaver.updateControllerStatus(this);
        Controller c = gameSaver.loadControllerStatus();
        if(c != null){
            mainTable.setNumOfPlayers(c.getMainTable().getNumOfPlayers()); //HAS TO GO FIRST!
            mainTable.setCardsOnTable(c.getMainTable().getCardsOnTable());
            mainTable.setGlobalObjectives(c.getMainTable().getGlobalObjectives());
            mainTable.setEndGame(c.getMainTable().getEndGame());
            mainTable.setBoardPoints(c.getMainTable().getBoardPoints());
            mainTable.setGlobalPoints(c.getMainTable().getGlobalPoints());
            PlayerTable[] pt = gameSaver.loadPlayerTableStatus();
            if(gameSaver.loadPlayerTableStatus() != null){
                for(int i = 0; i<mainTable.getNumOfPlayers(); i++){
                    mainTable.setPlayerTable(i, pt[i]);
                }
            }
            this.currentPlayerId = c.getCurrentPlayerId();
            this.clientsConnected = c.getClientsConnected();
            this.numOfPlayers = c.getNumOfPlayers();
            this.playersAdded = c.getPlayersAdded();
            this.startingCardsAndObjAdded = c.getStartingCardsAndObjAdded();
            this.isLastTurn = c.isLastTurn();
            gameSaver.loadDeckStatus();
        }
        gameSaver.updateControllerStatus(this);
    }

    /**
     * Returns the secret objective of a specific player.
     * @param playerId : the specific player's id.
     * @return : the player's secret objective.
     */
    public Objectives getSecretObjective(int playerId) {
        return mainTable.getPlayerTable(playerId).getSecretObjective();
    }

    /**
     * Checks whether a game is loaded from a file or not.
     * @return : true is the game is loaded from a file, false otherwise.
     */
    public boolean isGameLoaded() {
        return isGameLoaded;
    }
}

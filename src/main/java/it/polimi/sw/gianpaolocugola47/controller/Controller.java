package it.polimi.sw.gianpaolocugola47.controller;

import com.google.gson.annotations.Expose;
import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.observer.Observer;
import it.polimi.sw.gianpaolocugola47.utils.GameSaver;

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

    public MainTable getMainTable() {
        return mainTable;
    }

    public void setMainTable(MainTable mainTable) {
        this.mainTable = mainTable;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public int getPlayersAdded() {
        return playersAdded;
    }

    public int getStartingCardsAndObjAdded() {
        return startingCardsAndObjAdded;
    }

    public boolean isLastTurn() {
        return isLastTurn;
    }

    public Controller() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
        this.clientsConnected = 0;
        this.numOfPlayers = -1;
        this.isLastTurn = false;
    }
    public void resetGame() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
        this.clientsConnected = 0;
        this.numOfPlayers = -1;
        this.isLastTurn = false;
    }

    public void addModelObserver(Observer observer) {
        this.mainTable.addObserver(observer);
    }
    public void removeModelObserver(Observer observer) {
        mainTable.removeObserver(observer);
    }

    public void setNumOfPlayers(int num) {
        this.numOfPlayers = num;
        this.mainTable.setNumOfPlayers(num);
    }

    public int getNumOfPlayers(){
        return this.numOfPlayers;
    }

    public void addClientConnected(){
        this.clientsConnected++;
    }

    public int getClientsConnected(){
        return this.clientsConnected;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public void setClientsConnected(int clientsConnected) {
        this.clientsConnected = clientsConnected;
    }

    public void setPlayersAdded(int playersAdded) {
        this.playersAdded = playersAdded;
    }

    public void setStartingCardsAndObjAdded(int startingCardsAndObjAdded) {
        this.startingCardsAndObjAdded = startingCardsAndObjAdded;
    }

    public void setLastTurn(boolean lastTurn) {
        isLastTurn = lastTurn;
    }

    public GameSaver getGameSaver() {
        return gameSaver;
    }

    public void setGameSaver(GameSaver gameSaver) {
        this.gameSaver = gameSaver;
    }

    public void addPlayer(int id, String nickname) {
        mainTable.addPlayer(id, nickname);
        this.playersAdded++;
        if(this.numOfPlayers == this.playersAdded){
            this.gameSaver = new GameSaver(this); //if(gameSaver.checkIfRestarted(mainTable.getNicknames()))
            if(gameSaver.checkIfRestarted(mainTable.getNicknames())){
                //this.isGameLoaded = true; breaks everything
                System.err.println("game loaded from disk");
            }
            else{
                gameSaver.resetFiles();
            }
            mainTable.startGame();
        }
    }

    public int getNumOfPlayersCurrentlyAdded(){
        return this.playersAdded;
    }

    public StartingCard drawStartingCard() {
        return mainTable.drawStartingCard();
    }

    public Objectives[] setStartingCardAndDrawObjectives(int playerId, StartingCard card) {
        mainTable.setPlayerStartingCard(playerId, card);
        return mainTable.drawTwoPossibleSecretObjectives();
    }

    public void setSecretObjectiveAndUpdateView(int playerId, Objectives obj) {
        mainTable.setPlayerSecretObjective(playerId, obj);
        startingCardsAndObjAdded++;
        if(startingCardsAndObjAdded == mainTable.getNumOfPlayers()) {
            if(isGameLoaded)
                loadGame();
            mainTable.initView();
            mainTable.updateDecks(5);
            mainTable.showTurn(currentPlayerId);
        }
    }

    public void startGameFromFile(){
        mainTable.initView();
        mainTable.updateDecks(5);
        mainTable.showTurn(currentPlayerId);
    }

    public boolean[][] getPlayablePositions(int playerId) {
        return mainTable.checkAllPlayablePositions(playerId);
    }

    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId, boolean isFront) {
        if(playerId == currentPlayerId) {
            mainTable.turnCardOnHand(playerId, onHandCard, isFront);
            return mainTable.playCardAndUpdatePoints(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId);
        }
        else return false;
    }

    public void drawCard(int position, int playerId) {
        if(playerId == currentPlayerId) {
            mainTable.drawCardFrom(position, currentPlayerId);
            endTurn();
        }
    }

    private void endTurn() {

        updateCurrentPlayer();
        if(currentPlayerId == 0 && mainTable.isEndGame()) {
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
    private void updateCurrentPlayer() {
        if(currentPlayerId == mainTable.getNumOfPlayers()-1){
            currentPlayerId = 0;
        }
        else this.currentPlayerId++;
        gameSaver.updateControllerStatus(this);
        gameSaver.generateDeckStatusJson();
        gameSaver.generateGameStatusJson();
    }
    private void computeWinner() {
        mainTable.computeWinnerAtEndGame();
    }
    public ResourceCard[][] getCardsOnHand() {
        return mainTable.getCardsOnHand();
    }
    public PlaceableCard[][] getPlacedCards(int playerId) {
        return mainTable.getPlacedCards(playerId);
    }
    public int[] getResourceCounter(int playerId) {
        return mainTable.getResourceCounter(playerId);
    }
    public String[] getNicknames() {
        return mainTable.getNicknames();
    }

    public void loadGame(){
        gameSaver.updateControllerStatus(this);
        Controller c = gameSaver.loadControllerStatus();
        this.mainTable.notifyObservers(c.getMainTable());
        this.currentPlayerId = c.getCurrentPlayerId();
        this.clientsConnected = c.getClientsConnected();
        this.numOfPlayers = c.getNumOfPlayers();
        this.playersAdded = c.getPlayersAdded();
        this.startingCardsAndObjAdded = c.getStartingCardsAndObjAdded();
        this.isLastTurn = c.isLastTurn();
        gameSaver.updateControllerStatus(this);

    }

    public boolean isGameLoaded(){ //to be communicated to the respective views via the start() methods
        return this.isGameLoaded;
    }
}

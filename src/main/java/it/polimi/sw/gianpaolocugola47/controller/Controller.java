package it.polimi.sw.gianpaolocugola47.controller;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.observer.Observer;

public class Controller {

    private MainTable mainTable;
    private int currentPlayerId;
    private int clientsConnected; // max number of clients in a given game. DOES NOT TAKE INTO ACCOUNT DISCONNECTIONS
    private int numOfPlayers;
    private int playersAdded;
    private int startingCardsAndObjAdded;
    private boolean isLastTurn;

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
        mainTable.addObserver(observer);
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

    public void addPlayer(int id, String nickname) {
        mainTable.addPlayer(id, nickname);
        this.playersAdded++;
        if(this.numOfPlayers == this.playersAdded)
            mainTable.startGame();
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
            mainTable.initView();
            mainTable.updateDecks(5);
            mainTable.showTurn(currentPlayerId);
        }
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
        if(currentPlayerId == mainTable.getNumOfPlayers()-1)
            currentPlayerId = 0;
        else this.currentPlayerId++;
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
}

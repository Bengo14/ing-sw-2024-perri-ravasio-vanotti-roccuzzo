package it.polimi.sw.gianpaolocugola47.controller;

import it.polimi.sw.gianpaolocugola47.model.*;

public class Controller {

    private MainTable mainTable;
    private int currentPlayerId;
    private int playersAdded;
    private int startingCardsAndObjAdded;
    private boolean isLastTurn = false;

    public Controller() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
    }
    public void resetGame() {
        this.mainTable = new MainTable();
        this.currentPlayerId = 0;
        this.playersAdded = 0;
        this.startingCardsAndObjAdded = 0;
        this.isLastTurn = false;
    }

    public void setNumOfPlayers(int num){
        this.mainTable.setNumOfPlayers(num);
    }
    public int getNumOfPlayersCurrentlyAdded(){
        return this.playersAdded;
    }
    public void addPlayer(int id, String nickname) {
        this.mainTable.addPlayer(id, nickname);
        playersAdded++;
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
            mainTable.showTurn(currentPlayerId);
        }
    }

    public boolean[][] getPlayablePositions(int playerId) {
        if(playerId == currentPlayerId)
            return mainTable.checkAllPlayablePositions(playerId);
        else return null;
    }
    public void turnCardOnHand(int playerId, int position){
        mainTable.turnCardOnHand(playerId, position);
    }
    public boolean playCard(int onHandCard, int onTableCardX, int onTableCardY, int onTableCardCorner, int playerId) {
        if(playerId == currentPlayerId)
            return mainTable.playCardAndUpdatePoints(onHandCard, onTableCardX, onTableCardY, onTableCardCorner, playerId);
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
                computeWinner();
                currentPlayerId = -1;
                return;
            } else {
                this.isLastTurn = true;
            }
        }
        for(int i=0; i<mainTable.getNumOfPlayers(); i++) {
            if(!mainTable.checkIfPlayerCanPlay(currentPlayerId))
                updateCurrentPlayer();
            else {
                mainTable.showTurn(currentPlayerId);
                return;
            }
        }
        computeWinner();
        currentPlayerId = -1;
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
    public PlaceableCard[][] getPlacedCards(int playerId){
        return mainTable.getPlacedCards(playerId);
    }

    public String[] getNicknames() {
        return mainTable.getNicknames();
    }
}

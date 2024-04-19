package it.polimi.sw.gianpaolocugola47.controller;

import it.polimi.sw.gianpaolocugola47.model.Deck;
import it.polimi.sw.gianpaolocugola47.model.MainTable;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;

public class Controller {

    private MainTable mainTable;
    private PlayerTable currentPlayer;
    private int playersAdded = 0;

    public Controller() {
        this.mainTable = new MainTable();
    }
    public void resetGame(){
        this.mainTable = new MainTable();
        this.currentPlayer = null;
        this.playersAdded = 0;
        Deck.initDeck();
    }
    public void drawCard(int position) {
        mainTable.drawCardFrom(position, currentPlayer.getId());
    }

    public PlayerTable getCurrentPlayer() {
        return currentPlayer;
    }

    public void updateCurrentPlayer(){
        int id = currentPlayer.getId();
        if(id == mainTable.getNumOfPlayers()-1)
            this.currentPlayer = mainTable.getPlayerTable(0);
        else this.currentPlayer = mainTable.getPlayerTable(id + 1);
    }
    public int getWinner(){
        return mainTable.computeWinnerAtEndGame();
    }
    public void setNumOfPlayers(int num){
        this.mainTable.setNumOfPlayers(num);
    }
    public int getNumOfPlayersCurrentlyAdded(){
        return this.playersAdded;
    }
    public void addPlayer(int id, String nickname){
        this.mainTable.addPlayer(id, nickname);
        playersAdded++;
    }
    public void startGame() {
        this.currentPlayer = mainTable.getPlayerTable(0);
        /*todo*/
    }
}

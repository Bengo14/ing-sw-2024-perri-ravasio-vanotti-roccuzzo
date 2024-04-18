package it.polimi.sw.gianpaolocugola47.controller;

import it.polimi.sw.gianpaolocugola47.model.MainTable;
import it.polimi.sw.gianpaolocugola47.model.PlayerTable;

public class Controller {

    private final MainTable currentGame;
    private PlayerTable currentPlayer;

    public Controller()
    {
        this.currentGame = new MainTable();
    }
    /*todo*/

    public void drawCard(int position) throws IndexOutOfBoundsException
    {
        if(position >= 6 || position < 0)
            throw new IndexOutOfBoundsException();
        else
            currentGame.drawCardFrom(position, currentPlayer.getId());
    }

    public PlayerTable getCurrentPlayer() {
        return currentPlayer;
    }

    //NB: l'id deve essere progressivo?
    public void updateCurrentPlayer(){
        if(currentPlayer.getId() == currentGame.getNumOfPlayers()-1) {
            this.currentPlayer = currentGame.getPlayerTable(0);
        } else {
            this.currentPlayer = currentGame.getPlayerTable(this.currentPlayer.getId() + 1);
        }
    }

    public int getWinner(){
        return currentGame.computeWinnerAtEndGame();
    }
}

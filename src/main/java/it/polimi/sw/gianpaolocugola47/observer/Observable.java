package it.polimi.sw.gianpaolocugola47.observer;

import it.polimi.sw.gianpaolocugola47.model.MainTable;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void notifyObservers(MainTable mainTable);
    void initView();
    void updateDecks(int drawPos);
    void showTurn(int playerId);
    void showWinner(int winner);
    void updatePoints();
    void startGame();
}

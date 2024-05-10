package it.polimi.sw.gianpaolocugola47.observer;

public interface Observable {

    void addObserver(Observer observer);
    void removeObserver(Observer observer);
    void initView();
    void updateDecks();
    void showTurn(int playerId);
    void showWinner(int winner);
    void updatePoints();
    void startGame();
}

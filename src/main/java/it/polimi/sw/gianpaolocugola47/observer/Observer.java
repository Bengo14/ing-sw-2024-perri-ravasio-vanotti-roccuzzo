package it.polimi.sw.gianpaolocugola47.observer;

import it.polimi.sw.gianpaolocugola47.model.GoldCard;
import it.polimi.sw.gianpaolocugola47.model.Objectives;
import it.polimi.sw.gianpaolocugola47.model.ResourceCard;

import java.rmi.RemoteException;

public interface Observer {
    void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[][] cardsOnHand, ResourceCard[] cardsOnTable);
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos);
    void updatePoints(int[] boardPoints, int[] globalPoints);
    void showTurn(int playerId);
    void showWinner(int winnerId);
    void startGame() throws RemoteException;

}
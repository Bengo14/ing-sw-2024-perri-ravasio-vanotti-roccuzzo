package it.polimi.sw.gianpaolocugola47.rmi;

import it.polimi.sw.gianpaolocugola47.model.*;
import it.polimi.sw.gianpaolocugola47.utils.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView extends Remote {
    /*todo metodi remoti della view, invocati dal server*/

    void terminate() throws RemoteException;
    void ping() throws RemoteException;
    void startGame() throws RemoteException;
    void showTurn() throws RemoteException;
    void gameOver() throws RemoteException;
    void showWinner() throws RemoteException;
    String getNickname() throws RemoteException;
    int getId() throws RemoteException;
    void receiveMessage(ChatMessage message) throws RemoteException;
    void receivePrivateMessage(ChatMessage message) throws RemoteException;
    void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException;
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop) throws RemoteException;
    void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException;

}

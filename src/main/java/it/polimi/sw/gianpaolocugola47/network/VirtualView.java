package it.polimi.sw.gianpaolocugola47.network;

import it.polimi.sw.gianpaolocugola47.model.*;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface is used to define the methods that the virtual view must implement.
 * The virtual view is the entity that manages the communication between the server and the clients.
 */
public interface VirtualView extends Remote {

    void terminate() throws RemoteException;
    void ping() throws RemoteException;
    void startGame(boolean isLoaded) throws RemoteException;
    void setMyTurn() throws RemoteException;
    void gameOver() throws RemoteException;
    void showWinner(int id) throws RemoteException;
    int getId() throws RemoteException;
    void receiveMessage(ChatMessage message) throws RemoteException;
    void receivePrivateMessage(ChatMessage message) throws RemoteException;
    void initView(String[] nicknames, Objectives[] globalObjectives, ResourceCard[] cardsOnHand, ResourceCard[] cardsOnTable) throws RemoteException;
    void updateDecks(ResourceCard resourceCardOnTop, GoldCard goldCardOnTop, int drawPos) throws RemoteException;
    void updatePoints(int[] boardPoints, int[] globalPoints) throws RemoteException;
}
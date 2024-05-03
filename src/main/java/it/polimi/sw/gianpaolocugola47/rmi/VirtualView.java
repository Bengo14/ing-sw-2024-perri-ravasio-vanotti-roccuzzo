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
    void secretObjectivesDrawn(Objectives[] obj) throws RemoteException;
    void startingCardDrawn(StartingCard card) throws RemoteException;
    void showTurn() throws RemoteException;
    void setViewFixedParams(String[] nicknames, Objectives[] globalObj, Objectives secretObj) throws RemoteException;
    void updateView(ResourceCard[] cardsOnHand, PlaceableCard[][] placedCards) throws RemoteException;
    void updateView(int[] boardPoints, int[] globalPoints, ResourceCard[] cardsOnTable) throws RemoteException;
    void showPlayablePositions(boolean [][] matrix) throws RemoteException;
    void gameOver() throws RemoteException;
    void showWinner() throws RemoteException;
    String getNickname() throws RemoteException;
    int getId() throws RemoteException;
    void receiveMessage(ChatMessage message) throws RemoteException;
    void receivePrivateMessage(ChatMessage message) throws RemoteException;

}
